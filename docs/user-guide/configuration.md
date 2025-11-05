# Configuration Guide

This guide covers all configuration files and settings in zItems.

## Configuration Files Overview

zItems uses multiple configuration files:

```
plugins/zItems/
├── config.yml          # Main plugin configuration
├── messages.yml        # Customizable messages
├── items/*.yml         # Custom item definitions
└── effects/*.yml       # Effect definitions
```

---

## Main Configuration (config.yml)

The main configuration file contains plugin-wide settings.

### Default Configuration

```yaml
# Debug mode - enables detailed logging
debug: false

# Default number of effects to display in item lore
# -1 = show all effects
# 0 = hide all effects
# >0 = show only this number of effects (remaining shown as "...")
default-nb-effects-view: -1

# Block break event plugins (optional)
# List of plugins that should receive BlockBreakEvent when using Hammer/Vein Mining
# block-break-event-plugins:
#   - "ExamplePlugin"
```

### Settings Reference

#### debug

**Type**: `boolean`
**Default**: `false`
**Description**: Enables debug logging in console

When enabled, you'll see detailed information about:
- Effect loading and registration
- Item building process
- Effect application
- Block breaking events
- Drop calculations

**Example**:
```yaml
debug: true
```

**Console Output**:
```
[zItems] [DEBUG] Loading effect: super_hammer
[zItems] [DEBUG] Registered handler: HAMMER
[zItems] [DEBUG] Building item: super_pickaxe for player Steve
[zItems] [DEBUG] Applying effect: my_hammer to item
```

---

#### default-nb-effects-view

**Type**: `integer`
**Default**: `-1`
**Description**: Global default for effect lore display

**Values**:
- `-1` - Show all effects on all items
- `0` - Hide all effects on all items
- `>0` - Show only this number of effects

This can be overridden per-item with the `nb-effects-view` setting.

**Examples**:

```yaml
# Show all effects
default-nb-effects-view: -1
```

```yaml
# Hide all effects by default
default-nb-effects-view: 0
```

```yaml
# Show only first 3 effects
default-nb-effects-view: 3
```

**Item Override**:
```yaml
# In items/my_item.yml
id: "my_pickaxe"
material: DIAMOND_PICKAXE
effects:
  - effect1
  - effect2
  - effect3
  - effect4
  - effect5

# Override global setting
nb-effects-view: 2  # Only show 2 effects, others shown as "And More..."
```

---

#### block-break-event-plugins

**Type**: `List<String>`
**Default**: `null` (empty)
**Description**: Plugins that should receive BlockBreakEvent when using area mining effects

**Why This Exists**:
When using Hammer or Vein Mining, zItems fires custom BlockBreakEvents for each block. By default, these events are limited to specific plugins (WorldGuard, SuperiorSkyBlock2, etc.) to prevent issues with other plugins double-processing blocks.

If you want a custom plugin to receive these events, add it to this list.

**Example**:
```yaml
block-break-event-plugins:
  - "MyCustomPlugin"
  - "AnotherPlugin"
```

**Technical Details**:
- Events are fired using `EventUtil.fireEvent()`
- Only plugins in this list (plus hardcoded protection plugins) receive the events
- This prevents duplication bugs with poorly-coded plugins

---

## Messages Configuration (messages.yml)

All user-facing messages can be customized using MiniMessage format.

### Message Categories

```yaml
# ========================================
# Command Messages
# ========================================
no-permission: "<red>You do not have permission to execute this command."
only-in-game: "<red>This command can only be executed in-game."
requirement-not-met: "<red>You do not meet the requirements to perform this command."
arg-not-recognized: "<red>Argument not recognized."

# Effect Application
effect-applied: "<green>Effect <yellow><effect></yellow> has been applied to your item."
effect-already-present: "<red>Effect <yellow><effect></yellow> is already present on this item."
effect-incompatible: "<red>Effect <yellow><effect></yellow> is incompatible with existing effects on this item."
effect-not-allowed: "<red>Additional effects are not allowed on this item."
effect-disabled: "<red>Effect <yellow><effect></yellow> is disabled for this item."
effect-handler-not-found: "<red>Handler not found for effect <yellow><effect></yellow>."

# ========================================
# Item Give Command Messages
# ========================================
item-given: "<green>Given <yellow><amount>x <item></yellow> to <aqua><player></aqua>."
item-received: "<green>You received <yellow><amount>x <item></yellow>."
item-give-invalid-amount: "<red>Invalid amount! Amount must be greater than 0."

# ========================================
# Effect Give Command Messages
# ========================================
effect-given: "<green>Given <yellow><amount>x <effect></yellow> to <aqua><player></aqua>."
effect-received: "<green>You received <yellow><amount>x <effect></yellow>."
effect-give-invalid-amount: "<red>Invalid amount! Amount must be greater than 0."
effect-no-representation: "<red>Effect <effect> has no representation item."

# ========================================
# Effect View Command Messages
# ========================================
view-no-item: "<red>You must hold an item in your main hand!"
view-no-effects: "<red>This item has no effects."
view-header: "<gold><bold>Effects on Item</bold> <gray>(<count> total)"
view-effect-line: "<dark_gray>▸ <yellow><id></yellow> <gray>(<type>, Priority: <priority>)\n  <display-name>"
view-footer: "<gray>Use /zitems gui to browse all effects"

# ========================================
# GUI Messages
# ========================================
failed-to-open-gui: "<red>Failed to open GUI. Please try again."

# ========================================
# Effects Display in Item Lore
# ========================================
effects-lore-header: ""  # Empty line before effects section
effects-lore-title: "<gray>Effects"  # Section title
effects-lore-line: "<dark_gray>- <effect>"  # Each effect line
effects-lore-more: "<dark_gray>- <white>And More..."  # When limit exceeded
```

### Placeholders

Messages support various placeholders:

| Placeholder | Description | Used In |
|-------------|-------------|---------|
| `<effect>` | Effect display name or ID | Effect commands |
| `<item>` | Item display name | Item commands |
| `<player>` | Player name | Give commands |
| `<amount>` | Item/effect amount | Give commands |
| `<count>` | Number of effects | View command |
| `<id>` | Effect ID | View command |
| `<type>` | Effect handler type | View command |
| `<priority>` | Effect priority | View command |
| `<display-name>` | Effect display name | View command |

### MiniMessage Formatting

All messages support [MiniMessage format](https://docs.advntr.dev/minimessage/format.html):

**Colors**:
```yaml
message: "<red>Red text</red>"
message: "<#FF5555>Hex color</color>"
message: "<gradient:#FF0000:#00FF00>Gradient</gradient>"
message: "<rainbow>Rainbow text</rainbow>"
```

**Formatting**:
```yaml
message: "<bold>Bold</bold>"
message: "<italic>Italic</italic>"
message: "<underlined>Underlined</underlined>"
message: "<strikethrough>Strikethrough</strikethrough>"
```

**Hover & Click**:
```yaml
message: "<hover:show_text:'Hover text'>Hover me</hover>"
message: "<click:run_command:'/help'>Click me</click>"
```

### Customization Example

```yaml
# Custom effect applied message with hover
effect-applied: |
  <green>✓ Effect applied successfully!</green>
  <hover:show_text:'<gray>Effect: <effect>\n<gray>Type: <type>'>
    <yellow>⚡ <effect></yellow>
  </hover>

# Custom item given message with gradient
item-given: |
  <gradient:#00FF00:#00FFFF>
    Successfully given <amount>x <item> to <player>
  </gradient>

# Custom effect lore with custom formatting
effects-lore-title: "<gradient:#FFD700:#FFA500><bold>⚡ EFFECTS</bold></gradient>"
effects-lore-line: "  <dark_gray>▸ <effect>"
effects-lore-more: "  <dark_gray>▸ <gradient:#888888:#AAAAAA>...and more</gradient>"
```

---

## Item Configuration

Items are defined in `plugins/zItems/items/*.yml` files.

### Complete Item Structure

```yaml
# ========================================
# Basic Information
# ========================================
id: "example_item"              # Unique identifier (required)
material: DIAMOND_PICKAXE       # Bukkit material (required)

# ========================================
# Display
# ========================================
display-name: "<gradient:#00FFFF:#0080FF><bold>Example Item</bold></gradient>"
item-name: "<gray>Internal Name"  # Hover name (optional)
lore:
  - ""
  - "<gray>First line of lore"
  - "<yellow>Second line"
  - ""

# ========================================
# Effects
# ========================================
effects:                        # List of effect IDs
  - my_hammer
  - auto_sell

# Effect Display Control
nb-effects-view: -1             # -1 = all, 0 = none, >0 = limit
base-effects-visible: true      # Show base effects in lore
additional-effects-visible: true # Show added effects in lore

# Effect Restrictions
allow-additional-effects: true  # Allow applying more effects
disabled-effects:               # Effects that cannot be added
  - "vein_miner"
  - "silk_spawner"

# ========================================
# Properties
# ========================================
unbreakable: false
hide-tooltip: false
max-damage: -1                  # Custom durability (-1 = default)
custom-model-data: -1           # Resource pack model (-1 = none)
max-stack-size: -1              # Stack size (-1 = default)
repair-cost: -1                 # Anvil repair cost (-1 = default)
rarity: COMMON                  # COMMON, UNCOMMON, RARE, EPIC

# ========================================
# Enchantments
# ========================================
enchantments:
  - enchantment: EFFICIENCY
    level: 5
  - enchantment: FORTUNE
    level: 3

disabled-enchantments:          # Cannot be enchanted with
  - enchantment: SILK_TOUCH
    level: 1

# ========================================
# Attributes
# ========================================
attributes:
  - attribute: ATTACK_DAMAGE
    operation: ADD_NUMBER
    amount: 10.0
    slot: HAND

attribute-merge-strategy: REPLACE  # REPLACE, ADD, MULTIPLY

# ========================================
# Item Flags
# ========================================
flags:
  - HIDE_ENCHANTS
  - HIDE_ATTRIBUTES
  - HIDE_UNBREAKABLE
  - HIDE_DESTROYS
  - HIDE_PLACED_ON
  - HIDE_ADDITIONAL_TOOLTIP

# ========================================
# Advanced
# ========================================
trackable: true                 # Track when placed as block
anvil-enabled: true             # Can be used in anvil
enchanting-table-enabled: true  # Can be enchanted
grindstone-enabled: false       # Can be used in grindstone

damage-type-resistance: FIRE    # Bukkit damage type tag

# ========================================
# Recipe (Optional)
# ========================================
recipe:
  type: CRAFTING_SHAPED
  shape:
    - "DDD"
    - " S "
    - " S "
  ingredients:
    D: DIAMOND
    S: STICK
  amount: 1
  group: "custom_tools"
  category: EQUIPMENT
```

### Required Fields

Only two fields are required:

```yaml
id: "my_item"          # Must be unique
material: DIAMOND      # Must be valid Bukkit material
```

Everything else is optional!

### Field Reference

For detailed information about each field, see:
- [Creating Items Guide](creating-items.md)
- [Item Settings API](../api/reference.md#itemsettings)

---

## Effect Configuration

Effects are defined in `plugins/zItems/effects/*.yml` files.

### Basic Effect Structure

```yaml
# ========================================
# Basic Information
# ========================================
id: "example_effect"           # Unique identifier (required)
type: "HAMMER"                 # Effect handler type (required)
display-name: "<gold>⚒ Example</gold>"  # Display name (optional but recommended)

# ========================================
# Effect-Specific Settings
# ========================================
# Settings vary by effect type - see effect handler documentation

# Example for HAMMER:
materials:
  - STONE
width: 3
height: 3
depth: 1
damage: 1

# ========================================
# Applicability (Optional)
# ========================================
applicable-materials:          # Can only be applied to these
  - DIAMOND_PICKAXE
  - NETHERITE_PICKAXE
applicability-blacklisted: false

applicable-tags:               # Or use Bukkit tags
  - PICKAXES

# ========================================
# Effect Representation (Optional)
# ========================================
representation:
  # Item appearance
  material: GOLDEN_PICKAXE
  display-name: "<yellow>Hammer Stone</yellow>"
  lore:
    - "<gray>Apply to pickaxe"
  custom-model-data: 100

  # Application method
  applicator-type: SMITHING_TABLE  # or GUI

  # Smithing table requirements
  template:
    item: "item:NETHERITE_UPGRADE_SMITHING_TEMPLATE"
```

### Effect Types

See [Effect Handlers Reference](../advanced/effect-handlers.md) for all available types and their specific settings.

### Effect Representation

Effect representations allow effects to be applied via smithing tables or GUIs:

```yaml
representation:
  # What the effect item looks like
  material: NETHER_STAR
  display-name: "<gradient:#FF0000:#FFD700>Power Crystal</gradient>"
  lore:
    - ""
    - "<gray>Apply this to any tool to"
    - "<gray>grant it immense power!"
    - ""
    - "<gold>▸ Application: <yellow>Smithing Table"
    - ""
  custom-model-data: 5000

  # How to apply it
  applicator-type: SMITHING_TABLE

  # Smithing requirements
  template:
    item: "item:NETHERITE_UPGRADE_SMITHING_TEMPLATE"

  # OR custom item
  template:
    item: "zItems:my_custom_template"
```

**Applicator Types**:
- `SMITHING_TABLE` - Apply via smithing table
- `GUI` - Apply via custom GUI (future feature)

---

## Configuration Best Practices

### 1. Use Descriptive IDs

```yaml
# Good
id: "diamond_hammer_pickaxe"
id: "auto_sell_3x_multiplier"
id: "farming_hoe_7x7"

# Bad
id: "item1"
id: "effect"
id: "test"
```

### 2. Organize Files

Create subdirectories for organization:

```
items/
├── tools/
│   ├── pickaxes.yml
│   ├── shovels.yml
│   └── hoes.yml
├── weapons/
│   ├── swords.yml
│   └── bows.yml
└── armor/
    └── helmets.yml

effects/
├── mining/
│   ├── hammer.yml
│   └── vein_miner.yml
└── farming/
    └── farming_hoe.yml
```

### 3. Comment Your Configs

```yaml
# Ultimate Mining Pickaxe
# Designed for endgame mining with all enhancements
# Version: 2.0
# Last modified: 2024-01-15
id: "ultimate_pickaxe"
material: NETHERITE_PICKAXE

# Effects (order matters for priority)
effects:
  - ultimate_hammer  # 5x5x3 area
  - auto_smelt       # Instant smelting
  - auto_sell        # 1.5x sell price
  - xp_boost         # 3x experience
```

### 4. Use YAML Anchors for Reusability

```yaml
# Define common settings
common_pickaxe_materials: &pickaxe_materials
  - WOODEN_PICKAXE
  - STONE_PICKAXE
  - IRON_PICKAXE
  - GOLDEN_PICKAXE
  - DIAMOND_PICKAXE
  - NETHERITE_PICKAXE

# Reuse in multiple effects
---
id: "hammer_stone"
type: "HAMMER"
applicable-materials: *pickaxe_materials

---
id: "vein_miner_stone"
type: "VEIN_MINING"
applicable-materials: *pickaxe_materials
```

### 5. Test Incrementally

1. Create configuration file
2. Reload: `/zitems reload`
3. Check console for errors
4. Test in-game
5. Iterate

Don't create complex configs all at once!

---

## Validation & Error Checking

### Common Errors

**YAML Syntax Error**:
```
[zItems] Error loading items/my_item.yml:
  mapping values are not allowed here
```
**Solution**: Check YAML syntax - likely missing space after colon

**Unknown Material**:
```
[zItems] Unknown material: DIAMOD_PICKAXE
```
**Solution**: Check spelling against [Bukkit Material list](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)

**Missing Required Field**:
```
[zItems] Item file items/my_item.yml is missing required field: material
```
**Solution**: Add the required field

### Validation Tools

Use online YAML validators:
- https://www.yamllint.com/
- https://jsonformatter.org/yaml-validator

---

## Performance Considerations

### Effect Caching

Effects are cached after first load. Changes require `/zitems reload`.

### Custom Model Data

Use custom-model-data sparingly:
```yaml
custom-model-data: 1001  # OK
custom-model-data: 999999  # May cause lag with large numbers
```

### Lore Length

Keep lore concise:
```yaml
# Good - 5 lines
lore:
  - ""
  - "<gray>Description"
  - ""

# Bad - 50 lines
lore:
  - "Line 1"
  - "Line 2"
  # ... (48 more lines)
```

---

## Migration from zItemsOld

If you're migrating from the old zItems plugin, see the [Migration Guide](../examples/migration.md).

---

## Next Steps

- **[Creating Items](creating-items.md)** - Detailed item creation guide
- **[Effects System](effects.md)** - Understanding effects
- **[Effect Handlers](../advanced/effect-handlers.md)** - All available effects

---

Need help? Check our [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues) or join our Discord!