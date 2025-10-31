package fr.traqueur.items.api.settings;

import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.settings.models.IngredientWrapper;
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

public record RecipeSettings(RecipeType type,
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
        throw new UnsupportedOperationException("Use build(ItemStack result) instead.");
    }

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
