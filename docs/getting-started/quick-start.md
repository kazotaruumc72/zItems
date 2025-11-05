# Quick Start Guide

Get started with zItems in 5 minutes! This guide will walk you through creating your first custom item with effects.

## Prerequisites

Before starting, ensure you have:
- ✅ zItems installed and running
- ✅ zMenu plugin installed
- ✅ Server running Paper/Spigot 1.21+

If you haven't installed zItems yet, see the [Installation Guide](installation.md).

---

## Step 1: Understanding the File Structure

After installation, zItems creates this structure:

```
plugins/zItems/
├── config.yml          # Plugin settings
├── messages.yml        # Customizable messages
├── items/              # Your custom items go here
└── effects/            # Your effect definitions go here
```

**Key Concept**:
- **Effects** are reusable abilities (like Hammer, Vein Mining)
- **Items** are actual items that use those effects

---

## Step 2: Create Your First Effect

Let's create a simple Hammer effect that mines in a 3x3 area.

**Create**: `plugins/zItems/effects/my_hammer.yml`

```yaml
# My First Hammer Effect
id: "my_hammer"
type: "HAMMER"
display-name: "<gold><bold>⚒ My Hammer</bold></gold>"

# What blocks can be broken
materials:
  - STONE
  - COBBLESTONE
  - DEEPSLATE
  - ANDESITE
  - DIORITE
  - GRANITE

# Mining area
width: 3
height: 3
depth: 1

# Durability cost
damage: 1
```

**Save the file** and reload:
```
/zitems reload
```

You should see: `§aItems plugin reloaded successfully.`

---

## Step 3: Create Your First Item

Now let's create a pickaxe that uses your hammer effect.

**Create**: `plugins/zItems/items/super_pickaxe.yml`

```yaml
# Super Pickaxe with Hammer Effect
id: "super_pickaxe"
material: DIAMOND_PICKAXE

# Display
display-name: "<gradient:#00FFFF:#0080FF><bold>💎 Super Pickaxe</bold></gradient>"
lore:
  - ""
  - "<gray>A powerful pickaxe that mines"
  - "<gray>in a <yellow>3x3</yellow> area!"
  - ""

# Add the effect
effects:
  - my_hammer

# Optional: Make it unbreakable
unbreakable: false

# Optional: Custom model data for resource packs
# custom-model-data: 1001
```

**Save and reload**:
```
/zitems reload
```

---

## Step 4: Get Your Item

Give yourself the item:

```
/zitems item give @s super_pickaxe
```

You should receive your Super Pickaxe with the Hammer effect!

---

## Step 5: Test It Out

1. Find some stone blocks
2. Mine a block with your Super Pickaxe
3. Watch it break a 3x3 area! ⚒️

**What's happening**:
- The Hammer effect detects when you break a block
- It finds all blocks in a 3x3x1 area
- It breaks them all and collects the drops
- Your pickaxe takes 1 durability damage per block

---

## Step 6: View Effect Details

Want to see what effects are on your item?

```
/zitems effect view
```

This shows:
- All effects on the item in your hand
- Effect IDs and display names
- Handler types and priorities

---

## What's Next?

### Add More Effects

Let's add Auto-Sell to automatically sell mined blocks!

**Create**: `plugins/zItems/effects/auto_sell.yml`

```yaml
id: "auto_sell_mining"
type: "AUTO_SELL"
display-name: "<green><bold>💰 Auto Sell</bold></green>"
multiplier: 1.0  # Normal sell price
```

**Update your pickaxe**: `plugins/zItems/items/super_pickaxe.yml`

```yaml
effects:
  - my_hammer
  - auto_sell_mining  # Add this line
```

Reload and give yourself a new pickaxe - it now mines AND sells automatically!

**Note**: Requires a shop plugin (EconomyShopGUI, ShopGUIPlus, or ZShop).

---

### Create an Effect Item (Smithing Table)

You can create items that represent effects and apply them in smithing tables!

**Create**: `plugins/zItems/effects/hammer_stone.yml`

```yaml
id: "hammer_stone"
type: "HAMMER"
display-name: "<gold><bold>⚒ Hammer Stone</bold></gold>"

# Effect item representation
representation:
  material: GOLDEN_PICKAXE
  display-name: "<yellow><bold>⚒ Hammer Enhancement</bold></yellow>"
  lore:
    - ""
    - "<gray>Apply to a pickaxe in a"
    - "<gray>smithing table to add the"
    - "<gold>Hammer</gold><gray> effect!"
    - ""
  custom-model-data: 100
  applicator-type: SMITHING_TABLE
  template:
    item: "item:NETHERITE_UPGRADE_SMITHING_TEMPLATE"

# Hammer settings
materials:
  - STONE
  - COBBLESTONE
width: 3
height: 3
depth: 1

# Can only apply to pickaxes
applicable-materials:
  - WOODEN_PICKAXE
  - STONE_PICKAXE
  - IRON_PICKAXE
  - GOLDEN_PICKAXE
  - DIAMOND_PICKAXE
  - NETHERITE_PICKAXE
```

Give yourself the stone:
```
/zitems effect give @s hammer_stone
```

Use it in a smithing table:
1. Place Netherite Upgrade Template
2. Place your pickaxe
3. Place the Hammer Stone
4. Take out your enhanced pickaxe!

---

## Common Customizations

### Change Effect Display

Control how effects appear on items:

```yaml
# In your item configuration
id: "super_pickaxe"
material: DIAMOND_PICKAXE

effects:
  - my_hammer
  - auto_sell_mining

# Effect display settings
nb-effects-view: -1              # -1 = show all, 0 = hide all, >0 = limit
base-effects-visible: true       # Show base effects
additional-effects-visible: true # Show effects added later
```

### Prevent Adding More Effects

```yaml
# Don't allow players to add more effects
allow-additional-effects: false

# Or disable specific effects
disabled-effects:
  - "vein_miner"
  - "silk_spawner"
```

### Create Recipes

Add a crafting recipe:

```yaml
id: "super_pickaxe"
material: DIAMOND_PICKAXE
# ... other settings ...

recipe:
  type: CRAFTING_SHAPED
  shape:
    - "DDD"
    - " S "
    - " S "
  ingredients:
    D: "item:DIAMOND"
    S: "item:STICK"
```

---

## Troubleshooting

### Effect Not Working

**Problem**: "I added the effect but it's not working!"

**Checklist**:
1. Did you reload after creating the effect? `/zitems reload`
2. Is the effect ID spelled correctly in your item config?
3. Check console for errors when loading effects
4. Use `/zitems effect view` to confirm the effect is on the item

### Item Not Showing Effect Lore

**Problem**: "The effect doesn't appear in the lore!"

**Causes**:
1. `nb-effects-view: 0` hides all effects
2. `base-effects-visible: false` hides base effects
3. Effect doesn't have a `display-name` set

**Solution**:
```yaml
# In your effect configuration
display-name: "<gold>⚒ HAMMER</gold>"  # Add this!

# In your item configuration
nb-effects-view: -1  # Show all effects
```

### Can't Give Item

**Problem**: "/zitems item give doesn't work!"

**Solutions**:
1. Check permission: `items.command.item.give`
2. Check item ID spelling
3. Check console for errors
4. Try: `/zitems item give <player_name> super_pickaxe 1`

## Example: Complete Mining Tool

Here's a complete example of a powerful mining tool:

**Effect**: `plugins/zItems/effects/ultimate_mining.yml`

```yaml
id: "ultimate_mining"
type: "HAMMER"
display-name: "<gradient:#FFD700:#FFA500><bold>⚒ ULTIMATE MINING</bold></gradient>"

materials:
  - STONE
  - COBBLESTONE
  - DEEPSLATE
  - COBBLED_DEEPSLATE
  - ANDESITE
  - DIORITE
  - GRANITE
  - NETHERRACK
  - END_STONE
  - COAL_ORE
  - IRON_ORE
  - GOLD_ORE
  - DIAMOND_ORE
  - EMERALD_ORE

width: 5
height: 5
depth: 3
damage: 1
```

**Item**: `plugins/zItems/items/ultimate_pickaxe.yml`

```yaml
id: "ultimate_pickaxe"
material: NETHERITE_PICKAXE

display-name: "<gradient:#FF0000:#FFD700><bold>⛏ Ultimate Pickaxe</bold></gradient>"
lore:
  - ""
  - "<gray>The most powerful pickaxe"
  - "<gray>in existence!"
  - ""
  - "<gold>⚡ Unbreakable"
  - "<gold>⚡ 5x5x3 Mining Area"
  - "<gold>⚡ Auto-Smelting"
  - "<gold>⚡ 3x Experience"
  - ""

effects:
  - ultimate_mining

# Additional effects (create these too!)
# - auto_smelt
# - xp_boost_3x

unbreakable: true

enchantments:
  - enchantment: EFFICIENCY
    level: 5
  - enchantment: FORTUNE
    level: 3

custom-model-data: 9001
```

---

## Next Steps

Now that you've created your first items, explore more:

1. **[All Effect Types](../advanced/effect-handlers.md)** - Complete effect reference
2. **[Creating Items](../user-guide/creating-items.md)** - Advanced item configuration
3. **[Effect Lore Control](../advanced/effect-lore.md)** - Customize effect display
4. **[Commands Reference](../user-guide/commands.md)** - All available commands

---

## Tips & Best Practices

### Organize Your Files

```
plugins/zItems/
├── effects/
│   ├── mining/
│   │   ├── hammer.yml
│   │   ├── vein_miner.yml
│   │   └── auto_smelt.yml
│   ├── farming/
│   │   └── farming_hoe.yml
│   └── economic/
│       ├── auto_sell.yml
│       └── sell_stick.yml
├── items/
│   ├── tools/
│   │   ├── pickaxes.yml
│   │   └── hoes.yml
│   └── weapons/
│       └── swords.yml
```

### Use Consistent Naming

```yaml
# Good naming convention
id: "diamond_hammer_pickaxe"
id: "auto_sell_mining"
id: "farming_hoe_5x5"

# Avoid
id: "item1"
id: "effect"
id: "test"
```

### Test Incrementally

1. Create one effect
2. Test it
3. Create the item
4. Test it
5. Add more effects

Don't create everything at once - test as you go!

### Use MiniMessage Format

Take advantage of MiniMessage formatting:

```yaml
display-name: "<gradient:#FF0000:#FFD700>Gradient Text</gradient>"
display-name: "<rainbow>Rainbow Text</rainbow>"
display-name: "<bold><italic><red>Bold Italic Red</red></italic></bold>"
```

Learn more: [MiniMessage Documentation](https://docs.advntr.dev/minimessage/format.html)

---

**Congratulations!** 🎉 You've created your first custom item with zItems!

Need help? Join our [Discord](https://groupez.dev) or check [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues).