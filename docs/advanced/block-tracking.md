# Custom Block Tracking

Learn how zItems tracks custom blocks to ensure they drop the correct custom item when broken, and how the system handles persistence and performance.

---

## Overview

When a player places a custom item as a block, zItems needs to remember which custom item was placed. This allows effects like **Hammer** and **Vein Mining** to drop the correct custom item instead of vanilla drops.

**Example problem without tracking**:
1. Player places a custom "Ruby Ore" block (custom item ID: `ruby_ore`)
2. Player breaks it with Hammer
3. Without tracking: Drops vanilla `STONE` (wrong!)
4. With tracking: Drops custom `ruby_ore` item (correct!)

---

## How It Works

### Architecture

zItems uses a **two-layer system**:

1. **Memory Cache** (Guava Table) - Fast lookups during gameplay
2. **Chunk PDC** (PersistentDataContainer) - Persistent storage across restarts

```
┌─────────────────────────────────────────────────┐
│ Player places custom block                      │
└─────────────────┬───────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────┐
│ BlockTracker.trackBlock()                       │
│ - Add to memory cache (Guava Table)             │
│ - Key: WorldChunkKey + packed position          │
│ - Value: Custom item ID                         │
└─────────────────┬───────────────────────────────┘
                  │
         ┌────────┴────────┐
         │                 │
         ▼                 ▼
  ┌────────────┐    ┌─────────────┐
  │ ChunkUnload│    │ Player mines│
  └──────┬─────┘    └──────┬──────┘
         │                 │
         ▼                 ▼
  ┌─────────────────────────────────┐
  │ Save to chunk PDC               │
  │ (persists across restarts)      │
  └─────────────────────────────────┘
```

### Memory Cache

**Data Structure**: `Table<WorldChunkKey, Integer, String>`

- **Row**: `WorldChunkKey` - Identifies chunk across all worlds (world UUID + chunk key)
- **Column**: `Integer` - Packed block position (x, y, z encoded in 32 bits)
- **Value**: `String` - Custom item ID

**Why this structure?**
- Fast chunk-based lookups (O(1) for most operations)
- Efficient memory usage (only loaded chunks are cached)
- Automatically grouped by chunk for easy save/load

### Persistent Storage

**Storage**: Chunk PersistentDataContainer (PDC)

Each chunk stores a `List<TrackedBlock>` where:
```java
record TrackedBlock(int packedPosition, String itemId)
```

**When saved**:
- On `ChunkUnloadEvent` - Chunk is unloading
- Memory cache → PDC

**When loaded**:
- On `ChunkLoadEvent` - Chunk is loading
- PDC → Memory cache

---

## Position Packing

To save memory, block positions are **packed** into a single 32-bit integer.

### Bit Layout

```
Bits 0-8   (9 bits):  Y coordinate (0-511, supports -64 to 319 with offset)
Bits 9-12  (4 bits):  Z coordinate (0-15, chunk-relative)
Bits 13-16 (4 bits):  X coordinate (0-15, chunk-relative)
Bits 17-31 (unused):  Always 0
```

### Why No Collisions?

Each coordinate occupies **separate, non-overlapping bits**:
- **X**: Bits 13-16 (mask `0xF000` when shifted)
- **Z**: Bits 9-12 (mask `0x0F00` when shifted)
- **Y**: Bits 0-8 (mask `0x01FF`)

**Total**: 4 + 4 + 9 = 17 bits used out of 32 available

### Packing Algorithm

```java
private int packBlockPosition(Block block) {
    // Extract chunk-relative coordinates (0-15 for x and z)
    int x = block.getX() & 0xF;  // Mask with 0x0F (last 4 bits)
    int z = block.getZ() & 0xF;  // Mask with 0x0F (last 4 bits)

    // Convert Y from [-64, 319] to [0, 383]
    int y = (block.getY() + 64) & 0x1FF; // Mask with 0x1FF (9 bits)

    // Combine using bit shifting
    return (x << 13) | (z << 9) | y;
}
```

### Example

Block at chunk-relative position **(5, 100, 10)**:

```
x = 5:     0000 0101
z = 10:    0000 1010
y = 164:   1010 0100  (100 + 64 offset)

Packed: 0001 0100 1010 1010 0100
        ^^^^ ^^^^ ^^^^ ^^^^ ^^^^
        x    z    y

Result: 0x14A4 = 5,284
```

**Why this works**:
- X shifted left 13 bits: `5 << 13 = 0x2000`
- Z shifted left 9 bits: `10 << 9 = 0x1400`
- Y stays in place: `164 = 0xA4`
- OR them together: `0x2000 | 0x1400 | 0xA4 = 0x34A4`

---

## WorldChunkKey

Chunks are identified using a composite key:

```java
private record WorldChunkKey(UUID worldUid, long chunkKey) {
    WorldChunkKey(Chunk chunk) {
        this(chunk.getWorld().getUID(), chunk.getChunkKey());
    }
}
```

**Why include world UUID?**

Without it, chunks at the same (X, Z) in different worlds would collide:
- Overworld chunk (0, 0)
- Nether chunk (0, 0)

With world UUID, they are distinct:
- `WorldChunkKey(overworld_uuid, chunkKey(0, 0))`
- `WorldChunkKey(nether_uuid, chunkKey(0, 0))`

---

## Block Lifecycle

### Tracking a Block

**When**: Player places a custom item as a block

```java
@EventHandler
public void onBlockPlace(BlockPlaceEvent event) {
    ItemStack item = event.getItemInHand();
    Optional<Item> customItem = itemsManager.getCustomItem(item);

    if (customItem.isPresent()) {
        BlockTracker.get().trackBlock(event.getBlock(), customItem.get().id());
    }
}
```

**Implementation**: `BlockTracker.trackBlock()`

```java
public void trackBlock(Block block, String itemId) {
    WorldChunkKey worldChunkKey = new WorldChunkKey(block.getChunk());
    int packedPosition = packBlockPosition(block);

    cache.put(worldChunkKey, packedPosition, itemId);

    Logger.debug("Tracked block at {} with item ID: {}", formatBlockLocation(block), itemId);
}
```

### Breaking a Block

**When**: Effect handler (Hammer, VeinMiner) breaks a tracked block

```java
// In effect handler
Optional<ItemStack> customDrop = BlockTracker.get().getCustomBlockDrop(block, player);

if (customDrop.isPresent()) {
    // Custom block - drop the custom item
    context.addDrops(List.of(customDrop.get()));
    BlockTracker.get().untrackBlock(block); // Clean up
} else {
    // Vanilla block - use normal drops
    context.addDrops(block.getDrops(tool));
}
```

**Implementation**: `BlockTracker.getCustomBlockDrop()`

```java
public Optional<ItemStack> getCustomBlockDrop(Block block, Player player) {
    Optional<String> itemId = getTrackedItemId(block);

    if (itemId.isEmpty()) {
        return Optional.empty();
    }

    // Get custom item from registry
    var itemsRegistry = Registry.get(ItemsRegistry.class);
    var customItem = itemsRegistry.getById(itemId.get());

    if (customItem == null) {
        Logger.debug("Tracked block has invalid item ID: {}", itemId.get());
        return Optional.empty();
    }

    // Build and return the custom item
    ItemStack customItemStack = customItem.build(player, 1);
    return Optional.of(customItemStack);
}
```

### Untracking a Block

**When**: Block is successfully broken

```java
public void untrackBlock(Block block) {
    WorldChunkKey worldChunkKey = new WorldChunkKey(block.getChunk());
    int packedPosition = packBlockPosition(block);

    String removed = cache.remove(worldChunkKey, packedPosition);
    if (removed != null) {
        Logger.debug("Untracked block at {}", formatBlockLocation(block));
    }
}
```

**Important**: Always call `untrackBlock()` after breaking a tracked block to prevent memory leaks!

---

## Chunk Load/Unload

### Loading a Chunk

**When**: `ChunkLoadEvent` fires

```java
@EventHandler
public void onChunkLoad(ChunkLoadEvent event) {
    BlockTracker.get().loadChunk(event.getChunk());
}
```

**Implementation**:

```java
public void loadChunk(Chunk chunk) {
    WorldChunkKey worldChunkKey = new WorldChunkKey(chunk);
    PersistentDataContainer pdc = chunk.getPersistentDataContainer();

    List<TrackedBlock> trackedBlocks = Keys.TRACKED_BLOCKS.get(pdc, new ArrayList<>());

    if (!trackedBlocks.isEmpty()) {
        for (TrackedBlock trackedBlock : trackedBlocks) {
            cache.put(worldChunkKey, trackedBlock.packedPosition(), trackedBlock.itemId());
        }

        Logger.debug("Loaded {} tracked blocks from chunk {}",
            trackedBlocks.size(), chunk.getChunkKey());
    }
}
```

### Unloading a Chunk

**When**: `ChunkUnloadEvent` fires

```java
@EventHandler
public void onChunkUnload(ChunkUnloadEvent event) {
    BlockTracker.get().unloadChunk(event.getChunk());
}
```

**Implementation**:

```java
public void unloadChunk(Chunk chunk) {
    WorldChunkKey worldChunkKey = new WorldChunkKey(chunk);
    Map<Integer, String> chunkData = cache.row(worldChunkKey);

    PersistentDataContainer pdc = chunk.getPersistentDataContainer();

    if (!chunkData.isEmpty()) {
        // Convert map to list of TrackedBlock
        List<TrackedBlock> trackedBlocks = new ArrayList<>(chunkData.size());
        for (Map.Entry<Integer, String> entry : chunkData.entrySet()) {
            trackedBlocks.add(new ZTrackedBlock(entry.getKey(), entry.getValue()));
        }

        Keys.TRACKED_BLOCKS.set(pdc, trackedBlocks);

        Logger.debug("Saved {} tracked blocks for chunk {}",
            trackedBlocks.size(), chunk.getChunkKey());

        // Clear from cache after saving
        cache.row(worldChunkKey).clear();
    } else {
        // Cleanup: remove PDC data if no blocks tracked
        pdc.remove(Keys.TRACKED_BLOCKS.getNamespacedKey());
    }
}
```

**Key points**:
- Only chunks with tracked blocks save PDC data
- Empty chunks have their PDC data removed (cleanup)
- Cache is cleared after successful save

---

## Integration with Effects

### Hammer Effect

```java
@Override
public void handle(Player player, ItemStack item, BlockBreakEvent event, EffectContext context) {
    // ... find blocks in 3x3x3 area ...

    for (Block block : blocksToBreak) {
        // Check if it's a custom block
        Optional<ItemStack> customDrop = BlockTracker.get().getCustomBlockDrop(block, player);

        if (customDrop.isPresent()) {
            // Custom block from zItems
            context.addDrops(List.of(customDrop.get()));
            BlockTracker.get().untrackBlock(block);
        } else {
            // Check other custom block providers (ItemsAdder, Oraxen, Nexo)
            CustomBlockProviderRegistry registry = Registry.get(CustomBlockProviderRegistry.class);
            Optional<List<ItemStack>> providerDrop = registry.getCustomBlockDrop(block, player);

            if (providerDrop.isPresent()) {
                context.addDrops(providerDrop.get());
            } else {
                // Vanilla block
                context.addDrops(block.getDrops(tool));
            }
        }

        context.addBlock(block); // Queue for breaking
    }
}
```

**Order of checks**:
1. `BlockTracker` - zItems custom blocks
2. `CustomBlockProviderRegistry` - Third-party custom blocks (ItemsAdder, etc.)
3. Vanilla drops - Fallback

---

## Performance Considerations

### Memory Usage

**Per tracked block**:
- WorldChunkKey: 16 bytes (UUID) + 8 bytes (long) = 24 bytes
- Packed position: 4 bytes (int)
- Item ID: ~20 bytes (String)
- **Total**: ~50 bytes per block

**Example server**:
- 1000 tracked blocks = ~50 KB
- 10,000 tracked blocks = ~500 KB
- 100,000 tracked blocks = ~5 MB

**Very efficient** even for large servers!

### Lookup Speed

All operations are **O(1)** average case:
- `trackBlock()` - Guava Table put
- `getTrackedItemId()` - Guava Table get
- `untrackBlock()` - Guava Table remove

### Chunk Load/Unload

**Load**: O(n) where n = tracked blocks in chunk
- Typically < 100 blocks per chunk
- Happens asynchronously during chunk load

**Unload**: O(n) where n = tracked blocks in chunk
- Happens during chunk unload
- No gameplay impact (player isn't in chunk)

---

## Debugging

### Enable Debug Logging

```yaml
# config.yml
debug: true
```

### Debug Messages

**When tracking**:
```
[zItems] [DEBUG] Tracked block at world:100,64,-50 with item ID: ruby_ore
```

**When breaking**:
```
[zItems] [DEBUG] Retrieved custom block drop at world:100,64,-50: ruby_ore
[zItems] [DEBUG] Untracked block at world:100,64,-50
```

**When loading chunk**:
```
[zItems] [DEBUG] Loaded 15 tracked blocks from chunk 1234567890 in world world
```

**When unloading chunk**:
```
[zItems] [DEBUG] Saved 15 tracked blocks for chunk 1234567890 in world world
```

### Checking Cache

Clear cache (for testing):
```java
BlockTracker.get().clearCache();
```

This clears memory but **does not affect** persistent data in chunk PDC.

---

## Common Issues

### Custom blocks drop vanilla items

**Symptoms**:
- Place custom "Ruby Ore" block
- Break it
- Drops vanilla `STONE` instead of `ruby_ore`

**Causes**:
1. Block wasn't tracked when placed
2. BlockTracker was cleared (server restart without chunk unload)
3. Item ID in registry doesn't match tracked ID

**Debug**:
```yaml
debug: true
```

Check logs for:
```
[zItems] [DEBUG] Tracked block at ... with item ID: <id>
```

If missing, the block placement event isn't being handled.

### Tracked blocks not persisting

**Symptoms**:
- Place custom blocks
- Restart server
- Blocks drop vanilla items

**Causes**:
1. Chunk didn't unload before shutdown (stayed loaded)
2. PDC data was cleared

**Solution**:
- Force chunk unload before shutdown (happens automatically with graceful stop)
- Avoid killing server process (use `/stop` or `/restart`)

### Memory leak

**Symptoms**:
- Memory usage increases over time
- Thousands of tracked blocks in cache

**Causes**:
- Not calling `untrackBlock()` after breaking blocks
- Blocks broken by non-player causes (explosions, etc.) not handled

**Solution**:
- Always call `untrackBlock()` in effect handlers
- Listen to `BlockBreakEvent` and `BlockExplodeEvent` to clean up

---

## API Usage

### Tracking a Block

```java
Block block = ...; // Block that was placed
String itemId = "ruby_ore"; // Custom item ID

BlockTracker.get().trackBlock(block, itemId);
```

### Getting Tracked Item

```java
Block block = ...; // Block to check
Player player = ...; // Player breaking it

Optional<ItemStack> customDrop = BlockTracker.get().getCustomBlockDrop(block, player);

if (customDrop.isPresent()) {
    // This is a custom block
    ItemStack drop = customDrop.get();
    // Don't forget to untrack!
    BlockTracker.get().untrackBlock(block);
}
```

### Checking if Tracked

```java
Block block = ...;

Optional<String> itemId = BlockTracker.get().getTrackedItemId(block);

if (itemId.isPresent()) {
    System.out.println("This block is tracked: " + itemId.get());
}
```

### Manual Cleanup

```java
Block block = ...;

// Remove from tracking (e.g., block was destroyed)
BlockTracker.get().untrackBlock(block);
```

---

## Best Practices

1. **Always untrack after breaking** - Prevents memory leaks
2. **Use getCustomBlockDrop() in effect handlers** - Ensures correct drops
3. **Don't track vanilla items** - Only track custom items
4. **Check BlockTracker first** - Before checking CustomBlockProviderRegistry
5. **Enable debug logging during testing** - Helps catch tracking issues
6. **Use graceful server shutdown** - Ensures chunks save properly

---

## Implementation Files

- **`BlockTracker.java`** - Main tracking logic (src/main/java/fr/traqueur/items/blocks/)
- **`TrackedBlock.java`** - Record for persistence (api)
- **`ZTrackedBlock.java`** - Implementation (src)
- **`ZTrackedBlockDataType.java`** - PDC serialization (src/main/java/fr/traqueur/items/serialization/)
- **`Keys.java`** - PDC key constants (src/main/java/fr/traqueur/items/serialization/)

---

## Related Documentation

- **[Effect Handlers Reference](effect-handlers.md)** - How effects use BlockTracker
- **[Hooks System](hooks.md)** - Custom block provider integrations

---

Need help? Join our [Discord](https://groupez.dev) or check [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues)!