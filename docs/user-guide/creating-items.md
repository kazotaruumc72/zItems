# Creating Items

Learn how to create custom items with zItems, from simple sticks to complex tools with effects, metadata, and recipes.

---

## Quick Start

### Your First Item

Create a file `items/my_stick.yml`:

```yaml
id: "my_stick"
material: STICK
display-name: "<aqua>My Custom Stick</aqua>"
lore:
  - "<gray>My first custom item!"
```

**Reload and test**:
```
/zitems reload
/zitems item give <player-name> my_stick
```

That's it! You've created your first custom item.

---

## File Structure

### Items Directory

All item files go in `plugins/zItems/items/`:

```
plugins/zItems/
└── items/
    ├── my_stick.yml
    ├── hammer_pickaxe.yml
    ├── tools/
    │   ├── farming_hoe.yml
    │   └── auto_sell_pickaxe.yml
    └── weapons/
        └── legendary_sword.yml
```

**Subdirectories are supported** - organize items however you like!

### File Naming

- Use snake_case: `hammer_pickaxe.yml` ✅
- Avoid spaces: `hammer pickaxe.yml` ❌
- Be descriptive: `tool1.yml` vs `farming_hoe.yml` ✅

---

## Basic Configuration

### Required Fields

Every item needs these fields:

```yaml
id: "my_item"          # Unique identifier
material: DIAMOND      # Bukkit Material type
display-name: "<gold>My Item</gold>"  # Display name with formatting
```

#### `id`

**Unique identifier** for this item.

**Rules**:
- Must be unique across all items
- Use snake_case
- Only letters, numbers, underscore
- No spaces

**Examples**:
```yaml
id: "hammer_pickaxe"     ✅
id: "super_sword_v2"     ✅
id: "my item"            ❌ (space)
id: "sword-mega"         ❌ (hyphen)
```

#### `material`

Bukkit **Material** enum value.

**Examples**:
```yaml
material: DIAMOND_PICKAXE
material: STICK
material: GOLDEN_APPLE
material: NETHERITE_CHESTPLATE
```

**Find materials**: [Bukkit Materials](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html)

#### `display-name`

Item name shown to players, using **MiniMessage** format.

**Examples**:
```yaml
# Simple color
display-name: "<gold>Golden Pickaxe</gold>"

# Gradient
display-name: "<gradient:#FF0000:#0000FF>Rainbow Sword</gradient>"

# Bold + italic
display-name: "<red><bold><italic>LEGENDARY AXE</italic></bold></red>"

# Unicode emoji
display-name: "<aqua>⚒ Hammer Pickaxe</aqua>"
```

**MiniMessage docs**: [Adventure MiniMessage](https://docs.advntr.dev/minimessage/format.html)

---

### Optional Fields

#### `lore`

List of lore lines displayed under the item name.

```yaml
lore:
  - "<gray>First line of lore"
  - "<yellow>Second line"
  - ""  # Empty line
  - "<gold>More text</gold>"
```

**Tips**:
- Use `""` for empty lines
- Keep lines short (< 40 characters)
- Use colors for organization

#### `custom-model-data`

Custom model data value for resource packs.

```yaml
custom-model-data: 1001
```

**Use case**: Custom textures/models via resource pack.

#### `max-stack-size`

Maximum stack size (1-99).

```yaml
max-stack-size: 64  # Default
max-stack-size: 1   # Non-stackable (tools, weapons)
```

#### `max-damage`

Maximum durability (for tools/weapons).

```yaml
max-damage: 2000  # Custom durability
```

**Note**: Requires `unbreakable: false`.

#### `unbreakable`

Make item unbreakable (infinite durability).

```yaml
unbreakable: true   # Never breaks
unbreakable: false  # Normal durability
```

#### `hide-tooltip`

Hide all tooltip information (enchants, attributes, etc.).

```yaml
hide-tooltip: false  # Show everything (default)
hide-tooltip: true   # Hide all tooltips
```

#### `trackable`

Enable block tracking (for custom blocks).

```yaml
trackable: true   # Track when placed
trackable: false  # Don't track (default)
```

**Use case**: Custom blocks that should drop the custom item when broken.

See: [Block Tracking](../advanced/block-tracking.md)

---

## Enchantments

Add vanilla enchantments to items:

```yaml
enchantments:
  - enchantment: efficiency
    level: 5
  - enchantment: fortune
    level: 3
  - enchantment: unbreaking
    level: 10  # Above vanilla limits!
```

### Format

```yaml
enchantments:
  - enchantment: <ENCHANTMENT_TYPE>
    level: <number>
```

**Enchantment types**: [Bukkit Enchantment](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/enchantments/Enchantment.html)

### Examples

```yaml
# Pickaxe enchants
enchantments:
  - enchantment: efficiency
    level: 5
  - enchantment: fortune
    level: 3
  - enchantment: mending
    level: 1

# Sword enchants
enchantments:
  - enchantment: sharpness
    level: 10
  - enchantment: looting
    level: 5
  - enchantment: fire_aspect
    level: 2

# Armor enchants
enchantments:
  - enchantment: protection
    level: 5
  - enchantment: thorns
    level: 3
  - enchantment: unbreaking
    level: 5
```

---

## Attributes

Add vanilla attributes (armor, attack damage, etc.):

```yaml
attributes:
  - attribute: armor
    operation: ADD_NUMBER
    amount: 12.0
    slot: CHEST
  - attribute: knockback_resistance
    operation: ADD_NUMBER
    amount: 0.3
    slot: CHEST
```

### Format

```yaml
attributes:
  - attribute: <ATTRIBUTE_TYPE>
    operation: <OPERATION>
    amount: <number>
    slot: <EQUIPMENT_SLOT>  # Optional
```

**Attribute types**:
- `armor` - Armor points
- `armor_toughness` - Armor toughness
- `attack_damage` - Attack damage
- `attack_speed` - Attack speed
- `knockback_resistance` - Knockback resistance
- `max_health` - Max health
- `movement_speed` - Movement speed
- `luck` - Luck

**Operations**:
- `ADD_NUMBER` - Add amount (e.g., +10 armor)
- `ADD_SCALAR` - Add percentage (e.g., +50% = 0.5)
- `MULTIPLY_SCALAR_1` - Multiply by (1 + amount)

**Slots** (optional):
- `HAND`, `OFF_HAND`
- `HEAD`, `CHEST`, `LEGS`, `FEET`

### Attribute Merge Strategy

Control how custom attributes merge with default item attributes:

```yaml
attribute-merge-strategy: SUM
```

**Strategies**:
- `REPLACE` - Replace all existing attributes (default)
- `ADD` - Keep existing + add new (may create duplicates)
- `KEEP_HIGHEST` - Keep highest value for each attribute
- `KEEP_LOWEST` - Keep lowest value for each attribute
- `SUM` - Sum values for each attribute

**Example**:

```yaml
# Netherite Chestplate has +8 armor by default
material: NETHERITE_CHESTPLATE
attributes:
  - attribute: armor
    operation: ADD_NUMBER
    amount: 12.0
    slot: CHEST
attribute-merge-strategy: SUM
# Result: 8 (default) + 12 (custom) = 20 total armor
```

### Examples

```yaml
# Super Chestplate
attributes:
  - attribute: armor
    operation: ADD_NUMBER
    amount: 20.0
    slot: CHEST
  - attribute: armor_toughness
    operation: ADD_NUMBER
    amount: 10.0
    slot: CHEST
  - attribute: knockback_resistance
    operation: ADD_NUMBER
    amount: 0.5
    slot: CHEST

# Speed Boots
attributes:
  - attribute: movement_speed
    operation: ADD_SCALAR
    amount: 0.5  # +50% speed
    slot: FEET

# Heavy Sword
attributes:
  - attribute: attack_damage
    operation: ADD_NUMBER
    amount: 15.0
    slot: HAND
  - attribute: attack_speed
    operation: ADD_NUMBER
    amount: -1.0  # Slower attacks
    slot: HAND
```

---

## Effects

Apply custom effects to items:

```yaml
effects:
  - hammer_effect
  - vein_miner
  - auto_sell
```

Effects are **effect IDs** defined in `effects/` directory.

**See**: [Effects System](effects.md)

### Effect Display Control

Control how effects appear in lore:

```yaml
effects:
  - hammer_effect
  - vein_miner

# Effect visibility
nb-effects-view: -1  # -1 = all, 0 = none, N = limit to N
base-effects-visible: true  # Show base effects
additional-effects-visible: true  # Show added effects

# Effect restrictions
allow-additional-effects: true  # Can add more effects
disabled-effects:  # Specific effects that cannot be added
  - "some_effect_id"
```

**See**: [Effect Lore Display](../advanced/effect-lore.md)

---

## Metadata System

Add special functionality using metadata:

```yaml
metadata:
  food:
    nutrition: 8
    saturation: 9.6
  # OR
  potion:
    color: "#FF0000"
    effects:
      - type: SPEED
        duration: 600
        amplifier: 1
  # etc...
```

**Available metadata types**:
- `food` - Food properties
- `potion` - Potion effects and color
- `leather-armor` - Leather armor color
- `trim` - Armor trims
- `banner` - Banner patterns
- `enchant-storage` - Stored enchantments (for books)

**See**: [Metadata System](../advanced/metadata-system.md)

---

## Recipes

Add crafting recipes for your items:

```yaml
recipe:
  type: CRAFTING_SHAPED
  category: MISC
  pattern:
    - "###"
    - " S "
    - " S "
  result-amount: 1
  ingredients:
    - item: "material:DIAMOND"
      sign: '#'
    - item: "material:STICK"
      sign: 'S'
```

### Recipe Types

#### Shaped Crafting

```yaml
recipe:
  type: CRAFTING_SHAPED
  category: EQUIPMENT
  pattern:
    - "###"
    - "## "
  result-amount: 1
  ingredients:
    - item: "material:DIAMOND"
      sign: '#'
```

**Pattern**:
- 3x3 grid
- Use any character for ingredients
- Space = empty slot

#### Shapeless Crafting

```yaml
recipe:
  type: CRAFTING_SHAPELESS
  category: MISC
  result-amount: 1
  ingredients:
    - "material:DIAMOND"
    - "material:STICK"
    - "material:GOLD_INGOT"
```

Order doesn't matter - just put ingredients anywhere.

#### Smelting

```yaml
recipe:
  type: SMELTING
  category: FOOD
  result-amount: 1
  experience: 0.5
  cooking-time: 200  # Ticks (200 = 10 seconds)
  ingredient: "material:RAW_IRON"
```

#### Smithing

```yaml
recipe:
  type: SMITHING_TRANSFORM
  result-amount: 1
  template: "material:NETHERITE_UPGRADE_SMITHING_TEMPLATE"
  base: "material:DIAMOND_PICKAXE"
  addition: "material:NETHERITE_INGOT"
```

### Ingredient Types

```yaml
# Material
item: "material:DIAMOND"

# Custom zItems item
item: "zitems:hammer_pickaxe"

# Tag (group of materials)
item: "tag:logs"  # Any log type
```

**Common tags**:
- `logs` - All log types
- `planks` - All plank types
- `stone_tool_materials` - Cobblestone, blackstone, etc.
- `wooden_slabs` - All wooden slabs

### Recipe Examples

```yaml
# Hammer Pickaxe Recipe
recipe:
  type: CRAFTING_SHAPED
  category: EQUIPMENT
  pattern:
    - "DDD"
    - " S "
    - " S "
  result-amount: 1
  ingredients:
    - item: "material:DIAMOND"
      sign: 'D'
    - item: "material:STICK"
      sign: 'S'

# Golden Apple (Shapeless)
recipe:
  type: CRAFTING_SHAPELESS
  category: FOOD
  result-amount: 1
  ingredients:
    - "material:APPLE"
    - "material:GOLD_INGOT"
    - "material:GOLD_INGOT"
    - "material:GOLD_INGOT"
    - "material:GOLD_INGOT"

# Smelted Item
recipe:
  type: SMELTING
  category: MISC
  result-amount: 1
  experience: 1.0
  cooking-time: 200
  ingredient: "material:IRON_ORE"
```

---

## Complete Examples

### Simple Stick

```yaml
id: "simple_stick"
material: STICK
display-name: "<aqua>Simple Stick</aqua>"
lore:
  - "<gray>Just a simple stick, nothing special"
hide-tooltip: false
max-stack-size: 64
```

### Hammer Pickaxe

```yaml
id: "hammer_pickaxe"
material: DIAMOND_PICKAXE
display-name: "<gradient:#00FFFF:#0088FF><bold>⚒ Hammer Pickaxe</bold></gradient>"
lore:
  - "<gray>Breaks blocks in a 3x3 area!"
  - ""
  - "<gold>Effects:"
  - "  <yellow>• Hammer (3x3x3)"
  - "  <yellow>• Efficiency V"

enchantments:
  - enchantment: efficiency
    level: 5

effects:
  - super_hammer

unbreakable: false
max-damage: 2000
hide-tooltip: false
max-stack-size: 1
```

### Auto-Sell Pickaxe with Recipe

```yaml
id: "auto_sell_pickaxe"
material: GOLDEN_PICKAXE
display-name: "<gradient:#FFD700:#FFA500><bold>💰 Auto-Sell Pickaxe</bold></gradient>"
lore:
  - "<gray>Automatically sells everything you mine!"
  - ""
  - "<gold>Effects:"
  - "  <yellow>• Auto Sell (1.5x multiplier)"
  - "  <yellow>• Unbreakable"

enchantments:
  - enchantment: efficiency
    level: 10
  - enchantment: fortune
    level: 5

effects:
  - auto_sell_pickaxe
  - unbreakable_tool

custom-model-data: 1001
unbreakable: false
hide-tooltip: false
max-stack-size: 1

recipe:
  type: CRAFTING_SHAPED
  category: EQUIPMENT
  pattern:
    - "GGG"
    - " S "
    - " S "
  result-amount: 1
  ingredients:
    - item: "material:GOLD_INGOT"
      sign: 'G'
    - item: "material:STICK"
      sign: 'S'
```

### Golden Apple with Food Metadata

```yaml
id: "golden_apple"
material: GOLDEN_APPLE
display-name: "<gradient:#FFD700:#FFA500><bold>✨ Enchanted Golden Apple</bold></gradient>"
lore:
  - "<gray>A mystical apple imbued with powerful magic"
  - ""
  - "<gold>Effects:"
  - "  <yellow>• Regeneration II (10s)"
  - "  <yellow>• Absorption IV (2min)"
  - "  <yellow>• Resistance I (5min)"

enchantments:
  - enchantment: protection
    level: 2

metadata:
  food:
    nutrition: 8
    saturation: 9.6
    can-always-eat: true
    eat-seconds: 0.8  # Paper only
    effects:  # Paper only
      - type: REGENERATION
        duration: 200
        amplifier: 1
      - type: ABSORPTION
        duration: 2400
        amplifier: 3
      - type: RESISTANCE
        duration: 6000
        amplifier: 0
```

### Trimmed Netherite Armor

```yaml
id: "trimmed_netherite_chestplate"
material: NETHERITE_CHESTPLATE
display-name: "<gradient:#4A4A4A:#8B0000><bold>⚔ Legendary Netherite Chestplate</bold></gradient>"
lore:
  - "<gray>Ancient armor with mystical patterns"
  - ""
  - "<gold>Features:"
  - "  <yellow>• Netherite Vex Trim"
  - "  <yellow>• +20 Total Armor"
  - "  <yellow>• +3 Knockback Resistance"

enchantments:
  - enchantment: protection
    level: 5
  - enchantment: unbreaking
    level: 5
  - enchantment: thorns
    level: 3

attributes:
  - attribute: armor
    operation: ADD_NUMBER
    amount: 12.0
    slot: CHEST
  - attribute: knockback_resistance
    operation: ADD_NUMBER
    amount: 0.3
    slot: CHEST

attribute-merge-strategy: SUM  # 8 (default) + 12 = 20 total

metadata:
  trim:
    material: DIAMOND
    pattern: VEX

unbreakable: true
```

### Hidden Additional Effects

```yaml
id: "stealth_tool"
material: NETHERITE_PICKAXE
display-name: "<gradient:#4B0082:#8B008B><bold>🔮 Stealth Miner</bold></gradient>"
lore:
  - "<gray>A tool with visible base powers"
  - "<dark_purple>Additional powers remain hidden..."
  - ""
  - "<aqua>Base Effect: Shown in lore"
  - "<gold>Additional Effects: Hidden but active!"

custom-model-data: 5002

effects:
  - super_hammer

# Display settings
nb-effects-view: -1
base-effects-visible: true
additional-effects-visible: false  # Hide added effects!

# Restrictions
allow-additional-effects: true

unbreakable: true
trackable: true
```

**Usage**:
```
/zitems item give @s stealth_tool
-> Shows "⚒ HAMMER" in lore

/zitems effect apply vein_miner_pickaxe
-> Lore stays the same (only shows HAMMER)
-> BUT vein miner effect is active!
```

---

## Advanced Topics

### Custom Model Data

Use resource packs for custom textures:

```yaml
custom-model-data: 1001
```

**How it works**:
1. Create resource pack with custom model
2. Assign model to custom model data value
3. Set value in item YAML
4. Item displays with custom texture

**Resource pack tutorial**: [Minecraft Model Guide](https://minecraft.fandom.com/wiki/Tutorials/Models)

### Trackable Blocks

For custom blocks that should drop the custom item:

```yaml
id: "ruby_ore"
material: STONE
trackable: true
```

**How it works**:
1. Player places `ruby_ore` item as a block
2. BlockTracker remembers: "This block is `ruby_ore`"
3. Player breaks it (or uses Hammer/Vein Mining)
4. Drops `ruby_ore` item instead of `STONE`

**See**: [Block Tracking](../advanced/block-tracking.md)

### Effect Restrictions

Control which effects can be added:

```yaml
# Allow adding any effect
allow-additional-effects: true

# Don't allow adding effects
allow-additional-effects: false

# Allow adding effects, but not these specific ones
allow-additional-effects: true
disabled-effects:
  - "vein_miner"
  - "hammer"
```

**Use case**: Prevent overpowered combinations.

---

## Testing Your Items

### Step 1: Create Item File

`items/my_pickaxe.yml`:
```yaml
id: "my_pickaxe"
material: DIAMOND_PICKAXE
display-name: "<aqua>My Pickaxe</aqua>"
```

### Step 2: Reload

```
/zitems reload
```

**Check console**:
```
[zItems] Loading items from items/...
[zItems] Registered item: my_pickaxe
[zItems] Registered 1 items
```

### Step 3: Give Item

```
/zitems item give @s my_pickaxe
```

**Or**:
```
/zitems item give PlayerName my_pickaxe 5
```

### Step 4: Test

- Check display name
- Check lore
- Test enchantments
- Test effects
- Test recipe (if applicable)

### Step 5: Iterate

Edit YAML → `/zitems reload` → Test again

---

## Common Issues

### Item not loading

**Symptoms**:
- `/zitems item give` says "Item not found"
- Console shows no "Registered item: X"

**Checklist**:
1. Is file in `plugins/zItems/items/` (or subdirectory)?
2. Is file named `.yml`?
3. Are required fields present (`id`, `material`, `display-name`)?
4. Is YAML syntax valid? (Check for indentation errors)

**Debug**:
```yaml
# config.yml
debug: true
```

### YAML syntax error

**Error**:
```
[zItems] [ERROR] Failed to load item: items/my_item.yml
org.yaml.snakeyaml.scanner.ScannerException: ...
```

**Common causes**:
- Inconsistent indentation (use spaces, not tabs)
- Missing quotes around special characters
- Unclosed strings

**Example fix**:
```yaml
# Wrong
display-name: <gold>Item</gold>

# Right
display-name: "<gold>Item</gold>"
```

### Recipe not working

**Symptoms**:
- Crafting doesn't work
- Recipe not shown in recipe book

**Checklist**:
1. Is `recipe:` block present?
2. Is `type` valid? (`CRAFTING_SHAPED`, `CRAFTING_SHAPELESS`, etc.)
3. Do ingredient materials exist?
4. For shaped: Is pattern valid? (Max 3x3)

**Debug**:
Enable debug mode and check:
```
[zItems] [DEBUG] Registered recipe for item: my_item
```

### Effects not applying

**Symptoms**:
- Item has effects in config
- Effects don't work in-game

**Checklist**:
1. Do effect files exist in `effects/`?
2. Are effect IDs correct?
3. Does effect's `can-apply-to` allow this material?

**Test**:
```
/zitems effect apply <effect_id>
```

If this works, the effect is fine. Check item config.

---

## Best Practices

1. **Use descriptive IDs** - `hammer_pickaxe` not `item1`
2. **Organize with subdirectories** - `tools/`, `weapons/`, `armor/`
3. **Keep lore concise** - Max 3-5 lines
4. **Test after changes** - `/zitems reload` is fast!
5. **Use MiniMessage formatting** - More powerful than legacy codes
6. **Document complex items** - Add comments in YAML
7. **Balance effects** - Don't make items too overpowered
8. **Use recipes** - Make items craftable for survival
9. **Consistent naming** - Use a naming convention and stick to it
10. **Version control** - Use git for your items directory!

---

## Next Steps

Now that you can create items:

1. **[Effects System](effects.md)** - Add powerful effects to items
2. **[Effect Handlers Reference](../advanced/effect-handlers.md)** - All available effects
3. **[Metadata System](../advanced/metadata-system.md)** - Food, potions, trims, etc.
4. **[Effect Lore Display](../advanced/effect-lore.md)** - Customize effect display

---

Need help? Join our [Discord](https://groupez.dev) or check [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues)!