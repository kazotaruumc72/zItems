package fr.traqueur.items.api.managers;

import fr.traqueur.items.api.items.Item;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public non-sealed interface ItemsManager extends Manager {
    void generateRecipesFromLoadedItems();

    /**
     * Gets the custom item associated with an ItemStack.
     *
     * @param itemStack the ItemStack to check
     * @return an Optional containing the custom Item if found, empty otherwise
     */
    Optional<Item> getCustomItem(ItemStack itemStack);
}
