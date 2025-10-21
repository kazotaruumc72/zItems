package fr.traqueur.items.effects;

import fr.traqueur.items.api.effects.Effect;
import fr.traqueur.items.api.managers.EffectsManager;
import fr.traqueur.items.serialization.Keys;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ZEffectsManager implements EffectsManager {

    @Override
    public void applyEffect(Player player, ItemStack item, Effect effect) {
        item.editPersistentDataContainer(container -> {
            List<Effect> effects = new ArrayList<>(Keys.EFFECTS.get(container, new ArrayList<>()));
            effects.add(effect);
            Keys.EFFECTS.set(container, effects);
        });
        this.getPlugin().getDispatcher().applyNoEventEffect(player, item, effect);
    }

}
