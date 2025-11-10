package fr.traqueur.items.api.settings.models;

import fr.traqueur.items.api.items.Item;
import fr.traqueur.recipes.api.RecipeType;
import fr.traqueur.recipes.api.domains.Ingredient;
import fr.traqueur.recipes.api.domains.Recipe;
import fr.traqueur.recipes.impl.domains.ItemRecipe;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultDouble;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import fr.traqueur.structura.annotations.defaults.DefaultString;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * RecipeSettings is a record that holds the configuration for a recipe.
 * Check <a href="https://github.com/Traqueur-dev/RecipesAPI">Recipes API Project</a> for more information.
 * @param type         The type of the recipe.
 * @param ingredients  The list of ingredient wrappers for the recipe.
 * @param pattern      The optional pattern for shaped recipes.
 * @param cookingTime  The optional cooking time for cooking recipes.
 * @param group        The optional group name for the recipe.
 * @param category     The optional category name for the recipe.
 * @param amount       The optional amount of the resulting item.
 * @param experience   The optional experience gained from the recipe.
 * @param priority     The optional priority of the recipe.
 */
public record RecipeWrapper(RecipeType type,
                            List<IngredientWrapper> ingredients,
                            @Options(optional = true) List<String> pattern,
                            @Options(optional = true) @DefaultInt(0) int cookingTime,
                            @Options(optional = true) @DefaultString("") String group,
                            @Options(optional = true) @DefaultString("") String category,
                            @Options(optional = true) @DefaultInt(1) int amount,
                            @Options(optional = true) @DefaultDouble(0) double experience,
                            @Options(optional = true) @DefaultInt(0) int priority
) implements Recipe, Loadable {

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
        throw new UnsupportedOperationException("Use build(String pluginName, ItemStack result) instead.");
    }

    /**
     * Builds an ItemRecipe using the provided plugin name and result item.
     *
     * @param pluginName The name of the plugin.
     * @param result     The resulting item of the recipe.
     * @return An ItemRecipe instance.
     */
    public ItemRecipe build(String pluginName, Item result) {
        return this.getItemRecipe(
                ingredients.stream().map(IngredientWrapper::toIngredient).toList(),
                type,
                pattern == null ? null : pattern.toArray(String[]::new),
                cookingTime,
                result.id(),
                group,
                category,
                pluginName + ":" + result.id(),
                amount,
                (float) experience,
                priority
        );
    }
}
