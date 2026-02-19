# Effect Lore Display

Learn how to customize the way effects are displayed in item lore, including visibility controls, formatting, and display limits.

---

## Overview

zItems provides fine-grained control over how effects appear in item lore. You can:
- **Control visibility** of base effects vs additional effects
- **Limit the number** of effects shown
- **Customize formatting** using messages
- **Show "And More..."** when effects are truncated
- **Separate base lore** from effect lore

---

## Display Settings

### Per-Item Settings

Configure in your item YAML files (`items/*.yml`):

```yaml
id: "my_pickaxe"
# ... other settings ...

# Effect Display Control
nb-effects-view: -1  # How many effects to show
base-effects-visible: true  # Show base effects
additional-effects-visible: true  # Show added effects
```

#### `nb-effects-view`

Controls how many effects are displayed in lore:

| Value | Behavior |
|-------|----------|
| `-1` | Show **all** effects (no limit) |
| `0` | Show **no** effects (hide all) |
| `> 0` | Show **up to N** effects, then "And More..." |

**Examples**:

```yaml
# Show all effects
nb-effects-view: -1

# Hide all effects
nb-effects-view: 0

# Show first 3 effects only
nb-effects-view: 3
```

#### `base-effects-visible`

Controls whether effects defined in the item config are shown:

```yaml
# Show base effects
base-effects-visible: true

# Hide base effects (only show added effects)
base-effects-visible: false
```

**Base effects** are those defined in the `effects:` list in the item YAML.

#### `additional-effects-visible`

Controls whether effects added via commands are shown:

```yaml
# Show additional effects
additional-effects-visible: true

# Hide additional effects (only show base)
additional-effects-visible: false
```

**Additional effects** are those applied using `/zitems effect apply`.

---

### Global Default Settings

Configure in `config.yml`:

```yaml
# Default value for nb-effects-view if not specified per-item
default-nb-effects-view: -1
```

This applies to all items that don't specify `nb-effects-view` in their YAML.

---

## Lore Formatting

Effect lore is formatted using the messages system. Customize in `messages.yml`:

```yaml
# Empty line before effects section
effects-lore-header: ""

# Title of effects section
effects-lore-title: "<gray>Effects"

# Each effect line (placeholder: <effect>)
effects-lore-line: "<dark_gray>- <effect>"

# When effects are truncated (nb-effects-view > 0)
effects-lore-more: "<dark_gray>- <white>And More..."
```

### Message Placeholders

#### `effects-lore-line`

The `<effect>` placeholder is replaced with the effect's `display-name` from the effect YAML:

**Effect YAML**:
```yaml
id: "hammer"
type: "HAMMER"
display-name: "<gradient:#FFD700:#FFA500><bold>⚒ HAMMER</bold></gradient>"
```

**Result in lore**:
```
Effects
- ⚒ HAMMER
```

---

## How Lore Generation Works

### During Item Build

When an item is first created (`/zitems item give`):

1. **Base lore** is added from `lore:` in item YAML
2. **Effect lore** is generated based on `base-effects-visible` and `nb-effects-view`
3. Both are combined and applied to the item
4. Effects are stored in PDC (PersistentDataContainer)

**Implementation**: `ZItem.java:build()`

### When Effects are Applied

When effects are added via `/zitems effect apply`:

1. Effect is added to PDC
2. Lore is recalculated:
   - **Base effects** = original effects from config
   - **Additional effects** = effects in PDC not in base
3. Effect lore is regenerated based on visibility settings
4. Item lore is updated: base lore + new effect lore

**Implementation**: `ZEffectsManager.java:updateItemLoreWithEffects()`

---

## Lore Update Flow

```
┌─────────────────────────────────────────┐
│ Effect Applied via Command              │
│ /zitems effect apply <effect>           │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│ ZEffectsManager.applyEffect()           │
│ - Add effect to PDC                     │
│ - Call applyNoEventEffect()             │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│ updateItemLoreWithEffects()             │
│ - Load all effects from PDC             │
│ - Separate base vs additional           │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│ generateEffectLore()                    │
│ - Filter based on visibility settings   │
│ - Apply nb-effects-view limit           │
│ - Format using Messages enum            │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│ Combine base lore + effect lore         │
│ - Base lore from item config            │
│ - Effect lore from generation           │
└─────────────────┬───────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────┐
│ ItemUtil.setLore()                      │
│ - Apply to ItemMeta                     │
│ - Player sees updated lore              │
└─────────────────────────────────────────┘
```

---

## Examples

### Show All Effects

```yaml
id: "demo_pickaxe"
material: DIAMOND_PICKAXE
display-name: "<gold>Demo Pickaxe</gold>"
lore:
  - "<gray>A demonstration item"

effects:
  - hammer
  - vein_miner
  - auto_sell

# Show everything
nb-effects-view: -1
base-effects-visible: true
additional-effects-visible: true
```

**Result**:
```
Demo Pickaxe
A demonstration item

Effects
- ⚒ HAMMER
- ⛏ VEIN MINER
- 💰 AUTO SELL
```

### Hide All Effects

```yaml
id: "secret_pickaxe"
material: DIAMOND_PICKAXE
display-name: "<gold>Secret Pickaxe</gold>"
lore:
  - "<gray>Effects are hidden"

effects:
  - hammer

# Hide effects completely
nb-effects-view: 0
```

**Result**:
```
Secret Pickaxe
Effects are hidden
```

No effect lore is shown at all.

### Limit to 2 Effects

```yaml
id: "limited_pickaxe"
material: DIAMOND_PICKAXE
display-name: "<gold>Limited Pickaxe</gold>"

effects:
  - hammer
  - vein_miner
  - auto_sell
  - xp_boost

# Only show first 2 effects
nb-effects-view: 2
base-effects-visible: true
additional-effects-visible: true
```

**Result**:
```
Limited Pickaxe

Effects
- ⚒ HAMMER
- ⛏ VEIN MINER
- And More...
```

The "And More..." line indicates there are additional effects not shown.

### Show Base Only

```yaml
id: "base_only_pickaxe"
material: DIAMOND_PICKAXE
display-name: "<gold>Base Only Pickaxe</gold>"

effects:
  - hammer

# Only show base effects (hide added effects)
nb-effects-view: -1
base-effects-visible: true
additional-effects-visible: false
```

**Result after adding Vein Miner**:
```
/zitems effect apply vein_miner
```

```
Base Only Pickaxe

Effects
- ⚒ HAMMER
```

Vein Miner is NOT shown because `additional-effects-visible: false`.

### Show Additional Only

```yaml
id: "additional_only_pickaxe"
material: DIAMOND_PICKAXE
display-name: "<gold>Additional Only Pickaxe</gold>"

effects:
  - hammer

# Only show added effects (hide base)
nb-effects-view: -1
base-effects-visible: false
additional-effects-visible: true
```

**Result initially**:
```
Additional Only Pickaxe
```

No effects shown because only base effect exists.

**After adding Vein Miner**:
```
/zitems effect apply vein_miner
```

```
Additional Only Pickaxe

Effects
- ⛏ VEIN MINER
```

Only the added effect is shown.

---

## Custom Formatting

### Rainbow Effect List

```yaml
# messages.yml
effects-lore-title: "<gradient:#FF0000:#FF7F00:#FFFF00:#00FF00:#0000FF:#4B0082:#9400D3>✨ Effects ✨</gradient>"
effects-lore-line: "<gradient:#FFD700:#FFA500>• <effect></gradient>"
effects-lore-more: "<gray>• <italic>Plus more...</italic>"
```

**Result**:
```
✨ Effects ✨
• ⚒ HAMMER
• ⛏ VEIN MINER
• Plus more...
```

### Minimalist Style

```yaml
# messages.yml
effects-lore-header: ""
effects-lore-title: "<white>─── Effects ───"
effects-lore-line: "<gray><effect>"
effects-lore-more: "<dark_gray>..."
```

**Result**:
```
─── Effects ───
⚒ HAMMER
⛏ VEIN MINER
...
```

### Detailed Style

```yaml
# messages.yml
effects-lore-title: "<aqua><bold>╔════ Effects ════╗"
effects-lore-line: "<aqua>║ <white><effect>"
effects-lore-more: "<aqua>║ <yellow>[+More Effects]"
```

**Result**:
```
╔════ Effects ════╗
║ ⚒ HAMMER
║ ⛏ VEIN MINER
║ [+More Effects]
```

---

## Vanilla Items

Effects can be applied to vanilla items using `/zitems effect apply` (while holding the item).

### Vanilla Item Lore Handling

For vanilla items:
- Existing lore is preserved
- Effect section is added below existing lore
- If effects are updated, old effect section is replaced

**Example**:

```
/give @s diamond_pickaxe{display:{Lore:['{"text":"My pickaxe"}']}}
/zitems effect apply hammer
```

**Result**:
```
My pickaxe

Effects
- ⚒ HAMMER
```

---

## Technical Details

### Lore Separation

**Custom Items**:
- Base lore is stored in `ItemSettings.lore()`
- Effect lore is generated on-demand
- Combined during `build()` and `updateItemLoreWithEffects()`

**Vanilla Items**:
- Existing lore is scanned for effect section
- Effect section identified by `Messages.EFFECTS_LORE_TITLE`
- Old effect section removed before adding new one

### Effect Order

Effects are displayed in the order they appear:
1. **Base effects** (if `base-effects-visible: true`)
2. **Additional effects** (if `additional-effects-visible: true`)

Within each group, effects are in the order they were added.

### PDC Storage

All effects (base + additional) are stored in `PersistentDataContainer`:
- Key: `Keys.EFFECTS`
- Type: `List<Effect>`
- Serialized using `ZEffectDataType`

The distinction between base and additional is recalculated each time by comparing PDC effects with the item config.

---

## Troubleshooting

### Effect lore not showing

**Check**:
1. Is `nb-effects-view` set to `0`? (Hides all effects)
2. Is `base-effects-visible` or `additional-effects-visible` false?
3. Does the effect have a `display-name` in its YAML?

**Debug**:
```yaml
# config.yml
debug: true
```

Look for: `[zItems] [DEBUG] Updated item lore for <item> with <N> total effects`

### Lore duplicated

**Cause**: Calling `updateItemLoreWithEffects()` multiple times without clearing previous lore.

**Solution**: This is handled automatically by zItems. If you're using the API:
```java
// Don't call this repeatedly for the same effect application
effectsManager.updateItemLoreWithEffects(item, effects);
```

### "And More..." not showing

**Check**:
1. Is `nb-effects-view` set correctly? (Must be > 0 and < total effects)
2. Do you have more effects than the limit?

**Example**:
```yaml
nb-effects-view: 5
effects:
  - hammer  # Only 1 effect
```

Won't show "And More..." because there's only 1 effect (less than limit).

### Custom formatting not applying

**Check**:
1. Did you edit `messages.yml`?
2. Did you `/zitems reload` after editing?
3. Are you using valid MiniMessage syntax?

**Test**:
```
/zitems reload
/zitems item give @s <item>
```

If it still doesn't work, check for YAML syntax errors:
```
[zItems] [ERROR] Failed to load messages.yml: ...
```

---

## API Usage

### Updating Lore Programmatically

```java
EffectsManager effectsManager = plugin.getManager(EffectsManager.class);
ItemStack item = player.getInventory().getItemInMainHand();

// Get all effects from PDC
List<Effect> effects = Keys.EFFECTS.get(
    item.getItemMeta().getPersistentDataContainer(),
    new ArrayList<>()
);

// Update lore
effectsManager.updateItemLoreWithEffects(item, effects);
```

### Generating Effect Lore

The generation logic is in `ZEffectsManager.java`:

```java
private List<Component> generateEffectLore(
    List<Effect> baseEffects,
    List<Effect> additionalEffects,
    ItemSettings itemSettings
) {
    // Implementation handles:
    // - nb-effects-view limit
    // - base/additional visibility
    // - Message formatting
    // - "And More..." line
}
```

---

## Best Practices

1. **Use `-1` for normal items** - Show all effects unless you have a reason to hide them
2. **Use `0` for mystery items** - Hide effects for surprise mechanics
3. **Use limits for items with many effects** - Prevents lore from becoming too long
4. **Keep effect names concise** - Long display names can wrap awkwardly
5. **Test with many effects** - Ensure lore doesn't overflow (Minecraft has lore limits)
6. **Use consistent formatting** - Edit `messages.yml` for server-wide style

---

## Related Documentation

- **[Effects System](../user-guide/effects.md)** - How effects work
- **[Creating Items](../user-guide/creating-items.md)** - Item configuration
- **[Configuration](../user-guide/configuration.md)** - Global settings
- **[Effect Handlers Reference](effect-handlers.md)** - All available effects

---

Need help? Join our [Discord](https://groupez.dev) or check [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues)!