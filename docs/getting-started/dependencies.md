# Dependencies

This page lists all dependencies required and optional for zItems.

## Required Dependencies

### zMenu

**Status**: ✅ Required
**Download**: [GroupeZ Website](https://groupez.dev)
**Purpose**: GUI framework for zItems

zMenu is **mandatory** for zItems to function. The plugin will not load without it.

**What it's used for**:
- `/zitems gui` - Effects browser GUI
- Future GUI-based effect applicators
- Internal inventory management

**Installation**:
1. Download zMenu from GroupeZ
2. Place `zMenu-X.X.X.jar` in your `plugins/` folder
3. Restart server
4. Install zItems after zMenu is loaded

---

## Optional Plugin Integrations

### Custom Block Plugins

zItems integrates with custom block plugins to properly detect and drop custom items when using Hammer or Vein Mining effects.

#### ItemsAdder

**Status**: 🔷 Optional
**Download**: [SpigotMC](https://www.spigotmc.org/resources/itemsadder.73355/)
**Purpose**: Custom blocks, items, textures

**Integration Features**:
- Detects ItemsAdder custom blocks
- Returns correct custom item drops
- Works with Hammer and Vein Mining

**How It Works**:
```java
// When breaking ItemsAdder blocks
CustomBlockProviderRegistry registry = Registry.get(CustomBlockProviderRegistry.class);
Optional<List<ItemStack>> customDrop = registry.getCustomBlockDrop(block, player);
// Returns ItemsAdder custom item instead of vanilla drops
```

#### Nexo

**Status**: 🔷 Optional
**Download**: [th0rgal/Nexo](https://github.com/Nexo-Craft/Nexo)
**Purpose**: Custom blocks and items

**Integration Features**:
- Same as ItemsAdder
- Nexo custom blocks detected
- Correct drops when mining

#### Oraxen

**Status**: 🔷 Optional
**Download**: [SpigotMC](https://www.spigotmc.org/resources/oraxen.72448/)
**Purpose**: Custom items and blocks

**Integration Features**:
- Oraxen custom block detection
- Custom item drops preserved

**Note**: You can use **any combination** of these plugins. zItems will detect whichever ones are installed.

---

### Job Plugins

Integrate zItems with job systems for XP boost effects.

#### Jobs Reborn

**Status**: 🔷 Optional
**Download**: [SpigotMC](https://www.spigotmc.org/resources/jobs-reborn.4216/)
**Purpose**: Job system with leveling

**Integration Features**:
- Extract ItemStack from Jobs events
- Jobs XP boost effect
- Custom event handling for Jobs actions

**Example Effect**:
```yaml
id: "jobs_xp_boost"
type: "JOBS_XP_BOOST"
display-name: "<green>Jobs XP x2</green>"
multiplier: 2.0
```

#### ZJobs

**Status**: 🔷 Optional
**Download**: [GroupeZ Website](https://groupez.dev)
**Purpose**: GroupeZ custom job system

**Integration Features**:
- Same as Jobs Reborn
- ZJobs-specific event extractors

---

### Protection Plugins

zItems respects region protection from these plugins.

#### WorldGuard

**Status**: 🔷 Optional
**Download**: [EngineHub](https://enginehub.org/worldguard)
**Purpose**: Region protection

**Integration Features**:
- Checks block break permissions before Hammer/Vein Mining
- Respects region flags
- Uses `EventUtil.canBreakBlock()` internally

**Behavior**:
```java
// Before breaking blocks with Hammer
if (!EventUtil.canBreakBlock(player, block.getLocation())) {
    // Skip this block - protected by WorldGuard
    continue;
}
```

#### SuperiorSkyBlock2

**Status**: 🔷 Optional
**Download**: [SpigotMC](https://www.spigotmc.org/resources/superiorskyblock2.63905/)
**Purpose**: Skyblock plugin with island protection

**Integration Features**:
- Checks island permissions
- Respects island member roles
- Works with Hammer/Vein Mining

---

### Shop Plugins

Required for Auto-Sell and Sell Stick effects.

#### EconomyShopGUI

**Status**: 🔷 Optional (required for Auto-Sell)
**Download**: [SpigotMC](https://www.spigotmc.org/resources/economyshopgui.69927/)
**Purpose**: Shop plugin

**Integration Features**:
- Provides shop prices for items
- Auto-Sell effect integration
- Sell Stick support

**ShopProvider Integration**:
```java
ShopProvider provider = ShopProvider.get();
boolean sold = provider.sell(plugin, itemStack, amount, multiplier, player);
```

#### ShopGUIPlus

**Status**: 🔷 Optional (required for Auto-Sell)
**Download**: [SpigotMC](https://www.spigotmc.org/resources/shopgui-plus.6515/)
**Purpose**: Shop plugin

**Integration Features**:
- Same as EconomyShopGUI
- Auto-sell and Sell Stick support

#### ZShop

**Status**: 🔷 Optional (required for Auto-Sell)
**Download**: [GroupeZ Website](https://groupez.dev)
**Purpose**: GroupeZ custom shop system

**Integration Features**:
- Same as other shop plugins
- GroupeZ-specific pricing

**Note**: You only need **one** shop plugin for Auto-Sell to work. If no shop plugin is installed, Auto-Sell and Sell Stick effects will do nothing.

---

### Other Integrations

#### PlaceholderAPI

**Status**: 🔷 Optional
**Download**: [SpigotMC](https://www.spigotmc.org/resources/placeholderapi.6245/)
**Purpose**: Placeholder support in item names/lore

**Integration Features**:
- Use placeholders in item display names
- Use placeholders in lore
- Dynamic text based on player data

**Example**:
```yaml
display-name: "<gold>%player_name%'s Pickaxe</gold>"
lore:
  - "<gray>Level: %player_level%"
  - "<gray>Balance: %vault_eco_balance%"
```

---

## Soft Dependencies in plugin.yml

```yaml
softdepend:
  - EconomyShopGUI
  - ShopGUIPlus
  - ZShop
  - WorldGuard
  - SuperiorSkyBlock2
  - PlaceholderAPI
  - ItemsAdder
  - Nexo
  - Oraxen
  - Jobs
  - ZJobs
```

These plugins are **not required** but will be integrated if present.

---

## Shaded Dependencies

These are bundled inside zItems and don't need to be installed separately:

### Structura (1.4.0)

**Purpose**: YAML configuration framework
**Relocated**: `fr.traqueur.items.libs.structura`

Handles all YAML → Java object deserialization for items and effects.

### CommandsAPI (4.2.3)

**Purpose**: Command framework
**Relocated**: `fr.traqueur.items.libs.commands`

Provides the `/zitems` command structure with arguments and tab completion.

### RecipesAPI (3.1.0)

**Purpose**: Custom recipe registration
**Relocated**: `fr.traqueur.items.libs.recipes`

Registers smithing table recipes for effect applicators.

### Reflections (0.10.2)

**Purpose**: Annotation scanning
**Not Relocated**: Uses standard package

Scans for `@AutoEffect`, `@AutoHook`, and `@AutoExtractor` annotations.

---

## Server Requirements

### Minecraft Version

**Required**: 1.21 or higher
**Recommended**: 1.21.5

zItems uses modern Paper/Spigot APIs and requires 1.21+.

### Server Software

**Supported**:
- ✅ Paper (recommended)
- ✅ Spigot
- ✅ Folia (supported via `folia-supported: true`)

**Not Supported**:
- ❌ Bukkit (missing required APIs)
- ❌ Older versions (< 1.21)

### Java Version

**Required**: Java 21
**Why**: zItems uses modern Java features:
- Sealed interfaces
- Records
- Pattern matching
- Switch expressions

### Server Resources

**Minimum**:
- 2GB RAM
- 2 CPU cores

**Recommended**:
- 4GB+ RAM
- 4+ CPU cores
- SSD storage (for faster chunk loading with BlockTracker)

---

## Dependency Matrix

| Feature | Required Plugins | Optional Enhancements |
|---------|-----------------|----------------------|
| Basic Items | zMenu | - |
| Custom Effects | zMenu | - |
| Hammer/Vein Mining | zMenu | ItemsAdder, Nexo, Oraxen |
| Auto-Sell | zMenu + Shop Plugin | - |
| Sell Stick | zMenu + Shop Plugin | - |
| Jobs Integration | zMenu + Jobs/ZJobs | - |
| Protection | zMenu | WorldGuard, SuperiorSkyBlock2 |
| Placeholders | zMenu | PlaceholderAPI |

---

## Installation Order

For best results, install plugins in this order:

1. **Paper/Spigot** - Server software
2. **Vault** (if using economy)
3. **PlaceholderAPI** (if using placeholders)
4. **Protection Plugins** (WorldGuard, etc.)
5. **Custom Block Plugins** (ItemsAdder, Nexo, Oraxen)
6. **Job Plugins** (Jobs Reborn, ZJobs)
7. **Shop Plugins** (EconomyShopGUI, ShopGUIPlus, ZShop)
8. **zMenu** ⚠️ REQUIRED
9. **zItems** ✅

This ensures all hooks are detected properly at startup.

---

## Checking Dependencies

### In-Game

```
/plugins
```

Look for:
- `zMenu` - Should be **green**
- `zItems` - Should be **green**
- Optional plugins - Can be green or not present

### Console Log

When zItems loads, you'll see:

```
[zItems] Enabling zItems v1.0.0
[zItems] Loading items from items/...
[zItems] Loading effects from effects/...
[zItems] Detected ItemsAdder - enabling custom block support
[zItems] Detected WorldGuard - enabling region protection
[zItems] Detected EconomyShopGUI - enabling shop integration
[zItems] Registered 12 items and 24 effects
[zItems] zItems has been enabled!
```

Missing dependencies won't cause errors - they just won't be used.

---

## Troubleshooting Dependencies

### zMenu Not Found

**Error**: "zMenu not found! Disabling plugin..."

**Solution**:
1. Download zMenu from GroupeZ
2. Place in `plugins/` folder
3. Restart server
4. Ensure zMenu loads **before** zItems

### Shop Plugin Not Working

**Symptom**: Auto-Sell doesn't sell items

**Checklist**:
1. Is a shop plugin installed? (EconomyShopGUI, ShopGUIPlus, ZShop)
2. Is the shop plugin loaded? Check `/plugins`
3. Does the shop plugin have prices configured for the items?
4. Check console for "Detected [ShopPlugin]" message

**Test**:
```
/zitems reload
```

Look for: `[zItems] Detected EconomyShopGUI - enabling shop integration`

### Custom Blocks Not Dropping

**Symptom**: Hammer/Vein Mining drops vanilla items instead of custom items

**Checklist**:
1. Is the custom block plugin installed?
2. Is it loaded before zItems?
3. Check console for "Detected [CustomBlockPlugin]"

**Test**:
Break a custom block manually - does it drop the correct item? If yes, the plugin works. If no, the custom block plugin itself has issues.

---

## Version Compatibility

| zItems Version | Min Minecraft | Max Minecraft | Java |
|----------------|---------------|---------------|------|
| 1.0.0+ | 1.21 | 1.21.5+ | 21 |

**Future Updates**: zItems will support newer Minecraft versions as they release. Check the [Changelog](../support/changelog.md) for version-specific notes.

---

## Next Steps

Now that you understand dependencies:

1. **[Installation Guide](installation.md)** - Install zItems and dependencies
2. **[Quick Start](quick-start.md)** - Create your first item
3. **[Configuration](../user-guide/configuration.md)** - Configure the plugin

---

Need help? Join our [Discord](https://groupez.dev) or check [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues)!