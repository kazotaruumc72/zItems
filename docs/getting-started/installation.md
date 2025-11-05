# Installation Guide

This guide will walk you through installing zItems on your Minecraft server.

## Prerequisites

Before installing zItems, ensure your server meets these requirements:

### Server Requirements
- **Minecraft Version**: 1.21.5 or higher
- **Server Software**: Paper or Spigot (Paper recommended for best performance)
- **Java Version**: Java 21 or higher

### Required Dependencies
- **zMenu**: Required for GUI functionality
  - Download: [zMenu Plugin](https://groupez.dev)
  - Must be installed before zItems

### Optional Dependencies
zItems integrates with these plugins if installed:

| Plugin | Purpose |
|--------|---------|
| **ItemsAdder** | Custom block support |
| **Nexo** | Custom block support |
| **Oraxen** | Custom block support |
| **Jobs Reborn / ZJobs** | Job system integration & XP boost effects |
| **WorldGuard** | Region protection checks |
| **SuperiorSkyBlock2** | Island protection checks |
| **EconomyShopGUI** | Shop integration for Auto-Sell |
| **ShopGUIPlus** | Shop integration for Auto-Sell |
| **ZShop** | Shop integration for Auto-Sell |
| **PlaceholderAPI** | Placeholder support in item names/lore |

## Step-by-Step Installation

### Step 1: Download zItems

1. Download the latest version of zItems from:
   - Official website: [groupez.dev](https://groupez.dev)
   - Or from your purchase platform

2. Download **zMenu** (required dependency)

### Step 2: Upload Plugin Files

1. Stop your Minecraft server completely
2. Navigate to your server's `plugins/` folder
3. Upload the following files:
   - `zMenu-X.X.X.jar`
   - `zItems-X.X.X.jar`

```
server/
├── plugins/
│   ├── zMenu-X.X.X.jar      ← Upload first
│   ├── zItems-X.X.X.jar     ← Upload second
│   └── ... (other plugins)
```

### Step 3: Start Your Server

1. Start your Minecraft server
2. Watch the console for any errors
3. You should see messages indicating zItems loaded successfully:

```
[zItems] Enabling zItems v1.0.0
[zItems] Loading items from items/...
[zItems] Loading effects from effects/...
[zItems] Registered X items and Y effects
[zItems] zItems has been enabled!
```

### Step 4: Verify Installation

Run the following command in-game or console:

```
/zitems
```

You should see:
```
ZItems Plugin - Version X.X.X
Use /zitems help for a list of commands.
```

If you see this message, congratulations! zItems is installed correctly.

## Directory Structure

After first startup, zItems will create the following structure:

```
plugins/
├── zItems/
│   ├── config.yml           # Main configuration
│   ├── messages.yml         # Customizable messages
│   ├── items/               # Custom item definitions
│   │   ├── example_simple_stick.yml
│   │   ├── example_advanced_crafter.yml
│   │   └── ... (your items)
│   ├── effects/             # Effect definitions
│   │   ├── example_hammer.yml
│   │   ├── example_vein_miner.yml
│   │   └── ... (your effects)            # Internal data storage
```

## Initial Configuration

### 1. Edit `config.yml`

Open `plugins/zItems/config.yml` and configure basic settings:

```yaml
# Debug mode - enables detailed logging
debug: false

# Default number of effects to display in item lore
# -1 = show all effects
# 0 = hide all effects
# >0 = show only this number of effects
default-nb-effects-view: -1
```

### 2. Customize Messages (Optional)

Edit `plugins/zItems/messages.yml` to customize all plugin messages:

```yaml
effect-applied: "<green>Effect <yellow><effect></yellow> applied!"
item-given: "<green>Given <yellow><amount>x <item></yellow> to <aqua><player></aqua>."
# ... and more
```

All messages support [MiniMessage format](https://docs.advntr.dev/minimessage/format.html) for colors and formatting.

### 3. Reload Configuration

After making changes, reload the plugin:

```
/zitems reload
```

## Troubleshooting

### Plugin Won't Load

**Error: "zMenu not found"**
- **Solution**: Install zMenu first, then restart your server

**Error: "Unsupported Java version"**
- **Solution**: Upgrade to Java 21 or higher

**Error: "Unsupported API version"**
- **Solution**: Update to Paper/Spigot 1.21+

### Plugin Loads But Commands Don't Work

**Check permissions**: Ensure you have `zitems.command.admin` permission

**Try as OP**: If permissions aren't working, give yourself OP:
```
/op YourUsername
```

### Items Not Loading

**Check console for errors**: Look for YAML syntax errors in item files

**Validate YAML syntax**: Use an online YAML validator to check your configuration files

**Check file names**: Ensure item files have `.yml` extension

## Next Steps

Now that zItems is installed, here's what to do next:

1. **[Quick Start Guide](quick-start.md)** - Create your first custom item
2. **[Commands & Permissions](../user-guide/commands.md)** - Learn available commands
3. **[Creating Items](../user-guide/creating-items.md)** - Deep dive into item creation
4. **[Effects System](../user-guide/effects.md)** - Understand how effects work

## Getting Help

If you encounter issues during installation:

1. Check the [Troubleshooting](#troubleshooting) section above
2. Review server console logs for error messages
3. Visit our [GitHub Issues](https://github.com/GroupeZ-dev/zItems/issues)
4. Join our Discord community for support

---

**Installation complete!** Continue to the [Quick Start Guide](quick-start.md) to create your first item.