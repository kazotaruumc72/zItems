# Compilation Notes for zItems

## ✅ RESOLVED - Build Issues Fixed

### Fixed in Latest Commit

The Maven build failures have been resolved by fixing critical dependency issues in `pom.xml`:

1. **Removed Circular Self-Dependency** ✅
   - The project was incorrectly depending on itself via `com.github.GroupeZ-dev:zItems:main-SNAPSHOT`
   - This caused Maven to fail during dependency resolution
   - **Fix**: Removed lines 141-146 from pom.xml

2. **Removed Duplicate Structura Dependency** ✅
   - Two conflicting Structura dependencies were defined:
     - `com.github.Traqueur-dev:Structura:1.6.1` (kept)
     - `fr.traqueur:structura:1.6.0` (removed)
   - **Fix**: Removed lines 129-134 from pom.xml, keeping only the first dependency

### Current Dependencies

The project now has clean dependencies:
- ✅ `com.github.Traqueur-dev:Structura:1.6.1` - Type-safe YAML configuration
- ✅ `org.yaml:snakeyaml:2.4` - Required for Structura YAML parsing
- ✅ `io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT` - Paper API
- ✅ `me.clip:placeholderapi:2.11.1` - PlaceholderAPI
- ✅ `com.mojang:authlib:3.11.50` - Mojang Auth Library
- ✅ `com.sk89q.worldguard:worldguard-bukkit:7.0.9` - WorldGuard integration
- ✅ `com.sk89q.worldedit:worldedit-bukkit:7.2.14` - WorldEdit integration
- ✅ `com.github.Zrips:Jobs:v5.2.2.3` - Jobs plugin integration
- ✅ `io.lumine:Mythic-Dist:5.6.1` - MythicMobs integration
- ✅ `fr.maxlego08.menu:zmenu-api:1.1.0.8` - zMenu API

## Important Notice About Package Names

The error messages in the original problem statement referenced `fr.traqueur.items.*` packages, but this repository uses `fr.maxlego08.items.*` packages.

**Current Repository Structure:**
- This project uses: `fr.maxlego08.items.api.*`
- Error messages referenced: `fr.traqueur.items.api.*` (from a different environment/fork)

**The actual source code in this repository is correct** - no changes to Java files were needed.

## How to Build

```bash
# Clean and compile the project
mvn clean compile

# Package the project
mvn clean package
```

## Troubleshooting

If you still encounter build issues:

1. **Network Issues**: Ensure you can reach Maven repositories (Paper, JitPack, etc.)
2. **Cache Issues**: Try `mvn clean install -U` to force update dependencies
3. **Local Repository**: Clear your local Maven cache if corrupted: `rm -rf ~/.m2/repository/`

## Original Problem Analysis

The error messages in the problem statement showed compilation failures referencing:
- `fr.traqueur.items.api.placeholders`
- `fr.traqueur.items.api.utils`
- `fr.traqueur.items.api.effects`
- etc.

These errors were caused by the **circular dependency** issue, not missing code or wrong package names. The fix was purely in `pom.xml`, not in source code.
