package fr.traqueur.items.api.settings;

import fr.traqueur.recipes.api.RecipeType;
import fr.traqueur.recipes.api.Util;
import fr.traqueur.recipes.api.domains.Ingredient;
import fr.traqueur.recipes.api.domains.Recipe;
import fr.traqueur.recipes.api.hook.Hook;
import fr.traqueur.recipes.impl.domains.ItemRecipe;
import fr.traqueur.recipes.impl.domains.ingredients.ItemStackIngredient;
import fr.traqueur.recipes.impl.domains.ingredients.MaterialIngredient;
import fr.traqueur.recipes.impl.domains.ingredients.StrictItemStackIngredient;
import fr.traqueur.recipes.impl.domains.ingredients.TagIngredient;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultBool;
import fr.traqueur.structura.annotations.defaults.DefaultDouble;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import fr.traqueur.structura.annotations.defaults.DefaultString;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record RecipeSettings(RecipeType type,
                             List<IngredientSettings> ingredients,
                             List<String> pattern,
                             @Options(optional = true) @DefaultInt(0) int cookingTime,
                             @Options(optional = true) @DefaultString("") String group,
                             @Options(optional = true) @DefaultString("") String category,
                             @Options(optional = true) @DefaultInt(1) int amount,
                             @Options(optional = true) @DefaultDouble(0) double experience
                                   ) implements Recipe, Loadable {

    public record IngredientSettings(String item,
                                     @Options(optional = true) Character sign, @Options(optional = true) @DefaultBool(false) boolean strict) implements Loadable {

        private Tag<Material> getTag(String tagName) {
            Tag<Material> blockTag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.minecraft(tagName), Material.class);
            if (blockTag != null) {
                return blockTag;
            }
            Tag<Material> itemTag = Bukkit.getTag(Tag.REGISTRY_ITEMS, NamespacedKey.minecraft(tagName), Material.class);
            if (itemTag != null) {
                return itemTag;
            }
            throw new IllegalArgumentException("The tag " + tagName + " doesn't exist.");
        }

        public Ingredient toIngredient() {
            String[] data = item.split(":");
            if(data.length == 1) {
                return new MaterialIngredient(Util.getMaterial(data[0]), sign);
            }
            return switch (data[0]) {
                case "material" -> new MaterialIngredient(Util.getMaterial(data[1]), sign);
                case "tag" -> new TagIngredient(getTag(data[1]), sign);
                case "item" -> {
                    if(strict) {
                        yield new StrictItemStackIngredient(Util.getItemStack(data[1]), sign);
                    }
                    yield new ItemStackIngredient(Util.getItemStack(data[1]), sign);
                }
                default -> Hook.HOOKS.stream()
                        .filter(Hook::isEnable)
                        .filter(hook -> hook.getPluginName().equalsIgnoreCase(data[0]))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("The data " + data[0] + " isn't valid."))
                        .getIngredient(data[1], sign);
            };
        }

    }

    @Override
    public Recipe setName(String s) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe setResult(ItemStack itemStack) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe setAmount(int i) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe setType(RecipeType recipeType) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe addIngredient(Ingredient ingredient) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe setGroup(String s) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe setCategory(String s) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe setPattern(String... strings) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe setCookingTime(int i) {
        throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public Recipe setExperience(float v) {
       throw new UnsupportedOperationException("Can't use this method.");
    }

    @Override
    public RecipeType getType() {
        return this.type;
    }

    @Override
    public ItemRecipe build() {
        throw new UnsupportedOperationException("Use build(ItemStack result) instead.");
    }
    
    public ItemRecipe build(String name, ItemStack result) {
        return this.getItemRecipe(
                ingredients.stream().map(IngredientSettings::toIngredient).toList(),
                type,
                pattern.toArray(String[]::new),
                cookingTime,
                name,
                group,
                category,
                Util.fromItemStack(result),
                amount,
                (float) experience
        );
    }
}
