package fr.traqueur.items.effects;

import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.effects.EffectHandler;
import fr.traqueur.items.api.managers.EffectsManager;
import fr.traqueur.items.api.registries.HandlersRegistry;
import fr.traqueur.items.api.registries.Registry;
import fr.traqueur.items.serialization.Keys;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ZEffectsManager implements EffectsManager {

    @Override
    public void applyEffect(Player player, ItemStack item, Effect effect) {
        // Validate incompatibilities before applying
        if (!validateCompatibility(item, effect)) {
            Logger.warning("Cannot apply effect {} to item: incompatible with existing effects", effect.type());
            return;
        }

        item.editPersistentDataContainer(container -> {
            List<Effect> effects = new ArrayList<>(Keys.EFFECTS.get(container, new ArrayList<>()));
            effects.add(effect);
            Keys.EFFECTS.set(container, effects);
        });
        this.getPlugin().getDispatcher().applyNoEventEffect(player, item, effect);
    }

    /**
     * Validates that the new effect is compatible with all existing effects on the item.
     * Checks bidirectional incompatibilities.
     *
     * @param item the item to check
     * @param newEffect the effect to be applied
     * @return true if compatible, false if incompatible
     */
    private boolean validateCompatibility(ItemStack item, Effect newEffect) {
        // Get the handler for the new effect
        HandlersRegistry registry = Registry.get(HandlersRegistry.class);
        EffectHandler<?> newHandler = registry.getById(newEffect.type());
        if (newHandler == null) {
            Logger.warning("Handler not found for effect type: {}", newEffect.type());
            return false;
        }

        // Get incompatible handlers for the new effect
        Set<Class<? extends EffectHandler<?>>> newIncompatibles = newHandler.getIncompatibleHandlers();

        // Get all existing effects from the item
        List<Effect> existingEffects = Keys.EFFECTS.get(item.getItemMeta().getPersistentDataContainer(), new ArrayList<>());

        if (existingEffects == null || existingEffects.isEmpty()) {
            return true; // No existing effects, nothing to conflict with
        }

        // Check each existing effect for incompatibility
        for (Effect existingEffect : existingEffects) {
            EffectHandler<?> existingHandler = registry.getById(existingEffect.type());
            if (existingHandler == null) {
                continue;
            }

            // Check if new effect is incompatible with existing effect
            if (newIncompatibles.contains(existingHandler.getClass())) {
                Logger.debug("Effect {} is incompatible with existing effect {}",
                        newEffect.type(), existingEffect.type());
                return false;
            }

            // Check bidirectional: if existing effect is incompatible with new effect
            Set<Class<? extends EffectHandler<?>>> existingIncompatibles = existingHandler.getIncompatibleHandlers();
            if (existingIncompatibles.contains(newHandler.getClass())) {
                Logger.debug("Existing effect {} is incompatible with new effect {}",
                        existingEffect.type(), newEffect.type());
                return false;
            }
        }

        return true;
    }

}
