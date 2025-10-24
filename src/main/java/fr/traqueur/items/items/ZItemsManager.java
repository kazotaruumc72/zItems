package fr.traqueur.items.items;

import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.items.Item;
import fr.traqueur.items.api.managers.ItemsManager;
import fr.traqueur.items.api.registries.ItemsRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.recipes.api.RecipesAPI;
import fr.traqueur.recipes.impl.domains.ItemRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ZItemsManager implements ItemsManager {

    private final List<ItemRecipe> recipes;

    public ZItemsManager() {
        this.recipes = new ArrayList<>();
    }

    @Override
    public void generateRecipesFromLoadedItems() {
        RecipesAPI recipesAPI = this.getPlugin().getRecipesManager();
        for (ItemRecipe recipe : recipes) {
            recipesAPI.removeRecipe(recipe);
        }
        Collection<Item> items = Registry.get(ItemsRegistry.class).getAll();
        for (Item item : items) {
            if (item.settings().recipe() != null) {
                ItemRecipe itemRecipe = item.settings().recipe().build(this.getPlugin().getName(), item);
                this.recipes.add(itemRecipe);
                recipesAPI.addRecipe(itemRecipe);
            }
        }
        Logger.debug("Generated " + this.recipes.size() + " item recipes from loaded items.");
    }

}
