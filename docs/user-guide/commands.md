# Commands & Permissions

This page documents all zItems commands, their usage, and required permissions.

## Main Command

### `/zitems` (aliases: `/zit`, `/zitem`)

Main command for zItems plugin.

**Permission**: `zitems.command.admin`
**Usage**: `/zitems`
**Description**: Displays plugin version and help information

---

## Subcommands

### Give Item

Give custom items to players.

**Command**: `/zitems item give <player> <item> [amount]`
**Permission**: `items.command.item.give`
**Aliases**: None

**Arguments**:
- `<player>` - Target player (required)
- `<item>` - Item ID from your configuration (required)
- `[amount]` - Number of items to give (optional, default: 1)

**Examples**:
```bash
# Give 1 super hammer to Steve
/zitems item give Steve super_hammer

# Give 64 diamond packs to Alex
/zitems item give Alex diamond_pack 64
```

**Tab Completion**:
- Player names auto-complete
- Item IDs auto-complete from your `items/` folder
- Amount suggests: 1, 16, 64

---

### Apply Effect

Apply an effect to the item in your main hand.

**Command**: `/zitems effect apply <effect>`
**Permission**: `items.command.effect.apply`
**Aliases**: None
**Game Only**: Yes (must be a player)

**Arguments**:
- `<effect>` - Effect ID from your configuration (required)

**Examples**:
```bash
# Apply hammer effect to your pickaxe
/zitems effect apply super_hammer

# Apply vein miner effect
/zitems effect apply vein_miner_pickaxe

# Apply auto-sell effect
/zitems effect apply auto_sell_diamond
```

**Requirements**:
- Must hold an item in main hand
- Item must be compatible with the effect
- Effect must not conflict with existing effects
- Item must allow additional effects (if custom item)

**Possible Results**:
- ✅ **SUCCESS**: Effect applied successfully
- ❌ **ALREADY_PRESENT**: Effect already on item
- ❌ **INCOMPATIBLE**: Conflicts with existing effect
- ❌ **NOT_ALLOWED**: Item doesn't allow additional effects
- ❌ **DISABLED**: Effect is disabled for this item
- ❌ **HANDLER_NOT_FOUND**: Effect handler not registered

---

### Give Effect Item

Give effect representation items (used in smithing tables).

**Command**: `/zitems effect give <player> <effect> [amount]`
**Permission**: `items.command.effect.give`
**Aliases**: None

**Arguments**:
- `<player>` - Target player (required)
- `<effect>` - Effect ID that has a representation (required)
- `[amount]` - Number of effect items (optional, default: 1)

**Examples**:
```bash
# Give hammer effect stone to Steve
/zitems effect give Steve hammer_effect_stone

# Give 5 speed boost applicators to Alex
/zitems effect give Alex speed_boost 5
```

**Note**: Only effects with `representation` configured can be given as items. See [Effect Representation](../advanced/effect-handlers.md#effect-representation) for details.

**Tab Completion**:
- Only shows effects that have representations defined

---

### View Effect

View effects in the main hand of the executor

**Command**: `/zitems effect view`
**Permission**: `items.command.effect.view`
**Aliases**: None
**Game Only**: Yes

**Examples**:
```bash
/zitems effect view
```

---

### GUI

Open the effects browser GUI.

**Command**: `/zitems gui`
**Permission**: `items.command.gui`
**Aliases**: None
**Game Only**: Yes (opens GUI)

**Usage**: `/zitems gui`

**Description**: Opens an interactive GUI showing:
- All available effects with item representation and items
- Click to give effect or item

---

### Reload

Reload plugin configuration and files.

**Command**: `/zitems reload`
**Permission**: `items.command.reload`
**Aliases**: None

**Usage**: `/zitems reload`

**What Gets Reloaded**:
- `config.yml` - Main configuration
- `messages.yml` - All messages
- `items/*.yml` - All item definitions
- `effects/*.yml` - All effect definitions
- Effect recipes (smithing table recipes)

**Example**:
```bash
# After editing configuration files
/zitems reload
```

---

## Permission Nodes

### Admin Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `zitems.command.admin` | Access to main command | op |
| `items.command.item.give` | Give items to players | op |
| `items.command.effect.give` | Give effect items | op |
| `items.command.effect.apply` | Apply effects to items | op |
| `items.command.effect.view` | View effect details | op |
| `items.command.gui` | Open effects GUI | op |
| `items.command.reload` | Reload plugin | op |

### Wildcard Permissions

```yaml
# Give all zItems commands
zitems.command.*

# Give all effect commands
items.command.effect.*

# Give all item commands
items.command.item.*
```

---

## Troubleshooting Commands

### Command Not Found

**Problem**: "Unknown command. Type '/help' for help."

**Solutions**:
1. Ensure zItems is loaded: `/plugins` should show zItems in green
2. Check for typos - use tab completion
3. Try the full command: `/zitems` instead of `/zit`

---

## Next Steps

- **[Configuration Guide](configuration.md)** - Configure plugin settings
- **[Creating Items](creating-items.md)** - Learn to create custom items
- **[Effects System](effects.md)** - Understand how effects work
- **[Effect Handlers](../advanced/effect-handlers.md)** - Explore all available effects

---

Need help? Check our [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues) or join our Discord!