# Compilation Notes for zItems

## Important Notice

The error messages in the problem statement reference `fr.traqueur.items.*` packages, but this repository uses `fr.maxlego08.items.*` packages. 

**This mismatch indicates one of the following scenarios:**
1. You have custom code that needs to be refactored to use `fr.maxlego08.items.api` instead of `fr.traqueur.items.api`
2. You forked this project and renamed packages to `fr.traqueur` but didn't update dependencies
3. You're trying to compile against a different version of the project

## Resolved Issues

### ✅ Structura Dependency - FIXED
Added `com.github.Traqueur-dev:Structura:1.6.1` dependency to resolve `fr.traqueur.structura.*` package errors.

This provides:
- `fr.traqueur.structura.api` - Core API interfaces
- `fr.traqueur.structura.annotations` - Annotation support
- `org.yaml:snakeyaml` - Required YAML parsing library

## Outstanding Issues

### ⚠️ fr.traqueur.items.api Package Does Not Exist

The compilation errors reference `fr.traqueur.items.api.*` packages that **do not exist** in this repository or as a published Maven artifact.

**Current Repository Structure:**
- This project uses: `fr.maxlego08.items.api.*`
- Error messages expect: `fr.traqueur.items.api.*`

**Missing Packages from Error Messages:**
- `fr.traqueur.items.api.placeholders` → Available as `fr.maxlego08.items.api.configurations.*`
- `fr.traqueur.items.api.utils` → Not directly available
- `fr.traqueur.items.api.effects` → Check `fr.maxlego08.items.api.*`
- `fr.traqueur.items.api.events` → Available as `fr.maxlego08.items.api.events.*`
- `fr.traqueur.items.api.items` → Available as `fr.maxlego08.items.api.*`
- `fr.traqueur.items.api.managers` → Check `fr.maxlego08.items.api.*`
- `fr.traqueur.items.api.settings` → Check `fr.maxlego08.items.api.configurations.*`
- `fr.traqueur.items.api.annotations` → Not directly available

### Recommended Solutions

#### Option 1: Use Existing API Packages (Recommended)
Refactor your code to use the existing `fr.maxlego08.items.api.*` packages:
```java
// Change from:
import fr.traqueur.items.api.items.Item;

// To:
import fr.maxlego08.items.api.Item;
```

#### Option 2: Create Package Aliases
If you must use `fr.traqueur` packages, create wrapper classes that extend/implement the `fr.maxlego08` equivalents.

#### Option 3: Fork and Rename
If you forked this project and renamed packages:
1. Update ALL package declarations from `fr.maxlego08` to `fr.traqueur`
2. Update the `<groupId>` in pom.xml to `fr.traqueur.items`
3. Publish your fork to JitPack or Maven Central
4. Add it as a dependency to other projects

## Dependencies Added

| Dependency | Version | Purpose |
|------------|---------|---------|
| **Structura** | 1.6.1 | Type-safe YAML configuration library |
| **SnakeYAML** | 2.4 | YAML parsing (required by Structura) |

All dependencies are set with `provided` scope as they are expected to be available at runtime.

## Next Steps

1. **Identify which scenario applies to your situation**
2. **Choose appropriate solution** from the options above
3. **Refactor code** if needed to match package structure
4. **Rebuild project** after making changes

If you need help with any of these steps, please provide more context about your use case.

