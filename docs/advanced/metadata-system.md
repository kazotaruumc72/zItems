# Metadata System

The metadata system in zItems allows you to configure advanced item properties like food effects, potion effects, leather armor colors, armor trims, and more through a flexible, type-safe configuration system.

## What is Metadata?

Metadata represents **specialized item properties** that go beyond basic attributes. Each metadata type applies to specific item types (e.g., food metadata only works on edible items, leather armor metadata only works on leather armor).

### Key Concepts

1. **Polymorphic Configuration**: Each metadata type has its own configuration structure
2. **Type-Safe**: Structura validates metadata against the correct Java class
3. **Auto-Discovery**: Metadata implementations are discovered via `@AutoMetadata` annotation
4. **Item-Specific**: Each metadata type only works with compatible items

---

## Metadata Structure

Metadata is configured in the `metadata` section of item configs:

```yaml
id: "my_item"
material: GOLDEN_APPLE

metadata:
  food:              # Metadata type (discriminator)
    nutrition: 8
    saturation: 9.6
  # Can have multiple metadata types
```

---

## Available Metadata Types

### Food Metadata

**Discriminator**: `food`
**Compatible Materials**: Any edible item (Paper 1.20.5+)
**Purpose**: Configure food properties and consumption effects

#### Configuration

```yaml
metadata:
  food:
    nutrition: 8                # Hunger restored (required)
    saturation: 9.6             # Saturation value (required)
    can-always-eat: true        # Eat even when full (optional, default: false)
    eat-seconds: 0.8            # Eating duration (optional, default: -1 = vanilla)
    animation: DRINK            # Eat animation (optional)
    sound: ENTITY_GENERIC_EAT   # Eating sound (optional)
    cooldown-seconds: 5.0       # Use cooldown (optional, default: -1 = none)
    group-cooldown: "food:golden_apples"  # Cooldown group (optional)

    effects:                    # Potion effects when eaten (optional)
      - type: REGENERATION
        duration: 200           # Duration in ticks (20 ticks = 1 second)
        amplifier: 1            # Effect level (0 = level I)
      - type: ABSORPTION
        duration: 2400
        amplifier: 3
```

#### Fields Reference

| Field | Type | Default | Description |
|-------|------|---------|-------------|
| `nutrition` | `int` | Required | Hunger points restored (half drumsticks) |
| `saturation` | `double` | Required | Saturation value |
| `can-always-eat` | `boolean` | `false` | Can eat even when hunger is full |
| `eat-seconds` | `double` | `-1` | Time to consume (seconds) |
| `animation` | `ItemUseAnimation` | `null` | Eating animation type |
| `sound` | `Sound` | `null` | Sound played when eating |
| `cooldown-seconds` | `double` | `-1` | Cooldown after use (seconds) |
| `group-cooldown` | `String` | `null` | Cooldown group ID |
| `effects` | `List<PotionEffectWrapper>` | `null` | Potion effects on consumption |

#### Animation Types

- `EAT` - Normal eating animation
- `DRINK` - Drinking animation
- `BLOCK` - Shield blocking animation
- `BOW` - Bow drawing animation
- `SPEAR` - Trident throwing animation
- `CROSSBOW` - Crossbow loading animation
- `SPYGLASS` - Spyglass looking animation
- `TOOT_HORN` - Horn tooting animation
- `BRUSH` - Brush using animation

#### Complete Example

```yaml
id: "enchanted_golden_apple"
material: GOLDEN_APPLE
display-name: "<gradient:#FFD700:#FFA500><bold>✨ Enchanted Golden Apple</bold></gradient>"
lore:
  - "<gray>A mystical apple imbued with powerful magic"

metadata:
  food:
    nutrition: 8
    saturation: 9.6
    can-always-eat: true
    eat-seconds: 0.8
    animation: EAT
    sound: ENTITY_GENERIC_EAT

    effects:
      - type: REGENERATION
        duration: 200      # 10 seconds
        amplifier: 1       # Level II
      - type: ABSORPTION
        duration: 2400     # 2 minutes
        amplifier: 3       # Level IV
      - type: RESISTANCE
        duration: 6000     # 5 minutes
        amplifier: 0       # Level I
```

---

### Potion Metadata

**Discriminator**: `potion`
**Compatible Materials**: `POTION`, `SPLASH_POTION`, `LINGERING_POTION`, `TIPPED_ARROW`
**Purpose**: Configure potion colors and effects

#### Configuration

```yaml
metadata:
  potion:
    color: "#FF0000"              # Potion color (hex or Bukkit Color)
    base-potion-type: STRENGTH    # Base potion type (optional)
    custom-effects:               # Custom potion effects (optional)
      - type: STRENGTH
        duration: 6000
        amplifier: 2
      - type: SPEED
        duration: 6000
        amplifier: 1
```

#### Fields Reference

| Field | Type | Default | Description |
|-------|------|---------|-------------|
| `color` | `Color` | `null` | Potion color (hex or Bukkit) |
| `base-potion-type` | `PotionType` | `null` | Base potion type |
| `custom-effects` | `List<PotionEffectWrapper>` | `null` | Custom potion effects |

#### Base Potion Types

Common types: `STRENGTH`, `SPEED`, `HEALING`, `HARMING`, `REGENERATION`, `FIRE_RESISTANCE`, `WATER_BREATHING`, `INVISIBILITY`, `NIGHT_VISION`, `WEAKNESS`, `POISON`, `SLOWNESS`, etc.

See [Bukkit PotionType](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionType.html) for all types.

#### Complete Example

```yaml
id: "super_strength_potion"
material: POTION
display-name: "<gradient:#FF0000:#8B0000><bold>⚔ Potion of Ultimate Strength</bold></gradient>"
lore:
  - "<gray>Grants immense physical power"
  - ""
  - "<gold>Effects:"
  - "  <yellow>• Strength III (5min)"
  - "  <yellow>• Speed II (5min)"
  - "  <yellow>• Resistance I (5min)"

metadata:
  potion:
    color: "#FF0000"
    base-potion-type: STRENGTH
    custom-effects:
      - type: STRENGTH
        duration: 6000    # 5 minutes
        amplifier: 2      # Level III
      - type: SPEED
        duration: 6000
        amplifier: 1      # Level II
      - type: RESISTANCE
        duration: 6000
        amplifier: 0      # Level I
```

---

### Leather Armor Metadata

**Discriminator**: `leather-armor`
**Compatible Materials**: `LEATHER_HELMET`, `LEATHER_CHESTPLATE`, `LEATHER_LEGGINGS`, `LEATHER_BOOTS`, `LEATHER_HORSE_ARMOR`
**Purpose**: Dye leather armor

#### Configuration

```yaml
metadata:
  leather-armor:
    color: "#FF0000"    # Hex color or Bukkit Color
```

#### Color Formats

**Hex Color**:
```yaml
color: "#FF0000"      # Red
color: "#00FF00"      # Green
color: "#0000FF"      # Blue
```

**Bukkit Color** (RGB):
```yaml
color: "255, 0, 0"    # Red
```

#### Complete Example

```yaml
id: "ruby_chestplate"
material: LEATHER_CHESTPLATE
display-name: "<red><bold>Ruby Chestplate</bold>"
lore:
  - "<gray>Colored with the essence of rubies"

metadata:
  leather-armor:
    color: "#FF0000"

enchantments:
  - enchantment: PROTECTION
    level: 4

attributes:
  - attribute: ARMOR
    operation: ADD_NUMBER
    amount: 10.0
    slot: CHEST
```

---

### Armor Trim Metadata

**Discriminator**: `trim`
**Compatible Materials**: Any armor piece
**Purpose**: Apply armor trims (1.20+)

#### Configuration

```yaml
metadata:
  trim:
    material: DIAMOND    # Trim material
    pattern: VEX         # Trim pattern
```

#### Trim Materials

- `QUARTZ`, `IRON`, `NETHERITE`, `REDSTONE`, `COPPER`, `GOLD`, `EMERALD`, `DIAMOND`, `LAPIS`, `AMETHYST`

#### Trim Patterns

- `SENTRY`, `VEX`, `WILD`, `COAST`, `DUNE`, `WAYFINDER`, `RAISER`, `SHAPER`, `HOST`, `WARD`, `SILENCE`, `TIDE`, `SNOUT`, `RIB`, `EYE`, `SPIRE`

#### Complete Example

```yaml
id: "legendary_netherite_chestplate"
material: NETHERITE_CHESTPLATE
display-name: "<gradient:#4A4A4A:#8B0000><bold>⚔ Legendary Netherite Chestplate</bold></gradient>"
lore:
  - "<gray>Ancient armor with mystical patterns"

metadata:
  trim:
    material: DIAMOND
    pattern: VEX

enchantments:
  - enchantment: PROTECTION
    level: 5
  - enchantment: THORNS
    level: 3

attributes:
  - attribute: ARMOR
    operation: ADD_NUMBER
    amount: 12.0
    slot: CHEST

unbreakable: true
```

---

### Banner Metadata

**Discriminator**: `banner`
**Compatible Materials**: Any banner
**Purpose**: Add patterns to banners

#### Configuration

```yaml
metadata:
  banner:
    patterns:
      - color: WHITE
        pattern: BASE
      - color: RED
        pattern: STRIPE_TOP
      - color: BLUE
        pattern: STRIPE_BOTTOM
```

#### Pattern Types

- `BASE`, `STRIPE_BOTTOM`, `STRIPE_TOP`, `STRIPE_LEFT`, `STRIPE_RIGHT`, `STRIPE_CENTER`, `STRIPE_MIDDLE`, `STRIPE_DOWNRIGHT`, `STRIPE_DOWNLEFT`, `SMALL_STRIPES`
- `CROSS`, `STRAIGHT_CROSS`, `TRIANGLE_BOTTOM`, `TRIANGLE_TOP`, `TRIANGLES_BOTTOM`, `TRIANGLES_TOP`
- `DIAGONAL_LEFT`, `DIAGONAL_RIGHT`, `DIAGONAL_UP_LEFT`, `DIAGONAL_UP_RIGHT`
- `CIRCLE`, `RHOMBUS`, `HALF_VERTICAL`, `HALF_HORIZONTAL`, `HALF_VERTICAL_RIGHT`, `HALF_HORIZONTAL_BOTTOM`
- `BORDER`, `CURLY_BORDER`, `GRADIENT`, `GRADIENT_UP`, `BRICKS`, `GLOBE`, `CREEPER`, `SKULL`, `FLOWER`, `MOJANG`, `PIGLIN`

#### Complete Example

```yaml
id: "royal_banner"
material: WHITE_BANNER
display-name: "<gold><bold>Royal Banner</bold>"

metadata:
  banner:
    patterns:
      - color: WHITE
        pattern: BASE
      - color: GOLD
        pattern: STRIPE_TOP
      - color: RED
        pattern: STRIPE_BOTTOM
      - color: GOLD
        pattern: BORDER
      - color: RED
        pattern: MOJANG
```

---

### Enchanted Book Metadata

**Discriminator**: `enchant-storage`
**Compatible Materials**: `ENCHANTED_BOOK`
**Purpose**: Store enchantments in books

#### Configuration

```yaml
metadata:
  enchant-storage:
    enchantments:
      - enchantment: SHARPNESS
        level: 10
      - enchantment: LOOTING
        level: 5
```

#### Complete Example

```yaml
id: "legendary_enchant_book"
material: ENCHANTED_BOOK
display-name: "<gradient:#9B59B6:#E91E63><bold>📖 Legendary Enchantment Book</bold></gradient>"
lore:
  - "<gray>Contains powerful enchantments"
  - ""
  - "<gold>Enchantments:"
  - "  <yellow>• Sharpness X"
  - "  <yellow>• Looting V"
  - "  <yellow>• Fire Aspect III"

metadata:
  enchant-storage:
    enchantments:
      - enchantment: SHARPNESS
        level: 10
      - enchantment: LOOTING
        level: 5
      - enchantment: FIRE_ASPECT
        level: 3
```

---

## Advanced Metadata Types

### Tool Metadata

**Discriminator**: `tool`
**Compatible Materials**: Any tool
**Purpose**: Configure tool properties (damage on block break, default mining speed)

### Block State Metadata

**Discriminator**: `block-state`
**Compatible Materials**: Block items
**Purpose**: Store block state data

### Can Break / Can Place On Metadata

**Discriminator**: `can-break` / `can-place-on`
**Compatible Materials**: Any item
**Purpose**: Adventure mode block restrictions

---

## Using Multiple Metadata Types

You can apply multiple metadata types to a single item:

```yaml
id: "ultimate_food"
material: GOLDEN_APPLE

metadata:
  # Food properties
  food:
    nutrition: 20
    saturation: 20.0
    can-always-eat: true
    effects:
      - type: REGENERATION
        duration: 600
        amplifier: 4

  # Custom commands on consume (if implemented)
  commands:
    - "give %player% diamond 64"
    - "title %player% title <gold>Ultimate Power!</gold>"
```

---

## Creating Custom Metadata Types

Developers can create custom metadata types using the API:

### Step 1: Implement ItemMetadata

```java
package com.example.metadata;

import fr.traqueur.items.api.annotations.AutoMetadata;
import fr.traqueur.items.api.items.ItemMetadata;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@AutoMetadata("custom")  // Discriminator key
public record CustomMetadata(
    String customValue,
    int customNumber
) implements ItemMetadata {

    @Override
    public void apply(ItemStack itemStack, @Nullable Player player) {
        // Apply custom logic to the item
        itemStack.editMeta(meta -> {
            // Modify meta...
        });
    }
}
```

### Step 2: Register Package

```java
@Override
public void onEnable() {
    // Scan for @AutoMetadata annotations
    Registry.get(MetadataRegistry.class)
        .scanPackages("com.example.metadata");
}
```

### Step 3: Use in Configuration

```yaml
metadata:
  custom:
    custom-value: "Hello!"
    custom-number: 42
```

---

## Metadata vs Properties

### When to Use Metadata

Use metadata for:
- Item-specific properties (food, potions, armor colors)
- Complex nested data structures
- Type-specific configurations

### When to Use Item Properties

Use regular properties for:
- Basic item settings (display name, lore, material)
- Universal item attributes (unbreakable, enchantments, flags)
- Simple key-value pairs

**Example Comparison**:

```yaml
# Properties (simple, universal)
unbreakable: true
custom-model-data: 1001
max-stack-size: 16

# Metadata (complex, type-specific)
metadata:
  food:
    nutrition: 8
    saturation: 9.6
    effects:
      - type: REGENERATION
        duration: 200
        amplifier: 1
```

---

## Best Practices

### 1. Only Use Compatible Metadata

```yaml
# GOOD - Leather armor with leather-armor metadata
id: "colored_armor"
material: LEATHER_CHESTPLATE
metadata:
  leather-armor:
    color: "#FF0000"

# BAD - Diamond armor cannot use leather-armor metadata
id: "bad_armor"
material: DIAMOND_CHESTPLATE
metadata:
  leather-armor:    # Won't work!
    color: "#FF0000"
```

### 2. Validate Configuration

Test your metadata configurations:

```bash
/zitems reload
# Check console for errors
/zitems item give @s my_item
# Test in-game
```

### 3. Check Paper vs Spigot

Some metadata is Paper-only:

```yaml
metadata:
  food:
    eat-seconds: 0.8        # Paper only
    animation: DRINK        # Paper only
    effects:                # Paper only
      - type: REGENERATION
        duration: 200
        amplifier: 1
```

On Spigot, these fields will be ignored.

### 4. Document Your Metadata

```yaml
id: "custom_potion"
material: POTION

# Custom strength potion with extended duration
# Compatible with: POTION, SPLASH_POTION, LINGERING_POTION
metadata:
  potion:
    color: "#FF0000"
    custom-effects:
      - type: STRENGTH
        duration: 9600    # 8 minutes
        amplifier: 1      # Level II
```

---

## Troubleshooting

### Metadata Not Applied

**Symptoms**: Item doesn't have expected properties

**Checklist**:
1. Is the material compatible? (e.g., leather-armor only works on leather armor)
2. Is the metadata type spelled correctly?
3. Check console for errors when loading
4. Validate YAML syntax

**Debug**:
```bash
/zitems reload
# Look for errors like:
# [zItems] Failed to apply LeatherArmorMeta to ItemStack of type DIAMOND_CHESTPLATE
```

### Wrong Colors

**Problem**: Leather armor or potion has wrong color

**Solutions**:
- Check hex format: `#RRGGBB`
- Use uppercase for hex
- Verify color value is valid

```yaml
# Good
color: "#FF0000"
color: "#00FF00"

# Bad
color: "FF0000"   # Missing #
color: "#GG0000"  # Invalid hex
```

### Effects Not Working

**Problem**: Food or potion effects not applying

**Solutions**:
- Duration is in ticks (20 ticks = 1 second)
- Amplifier starts at 0 (0 = Level I, 1 = Level II, etc.)
- Check effect type is valid

```yaml
effects:
  - type: REGENERATION
    duration: 200     # 10 seconds (not 200 seconds!)
    amplifier: 0      # Level I (not Level 0!)
```

---

## Performance Considerations

### Metadata is Applied on Build

Metadata is applied when an item is built (`ZItem.build()`), not on every use. This means:

- ✅ Efficient - no runtime overhead
- ✅ Cached - item meta is stored
- ❌ Not dynamic - cannot change based on runtime conditions

### Use Metadata for Static Properties

```yaml
# Good - static properties
metadata:
  leather-armor:
    color: "#FF0000"

# Bad - if you need dynamic colors, use effects instead
```

---

## Next Steps

- **[Creating Items Guide](../user-guide/creating-items.md)** - Complete item configuration
- **[Effect System](../user-guide/effects.md)** - Dynamic item behaviors

---

Need help? Join our [Discord](https://groupez.dev) or check [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues)!