package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.structura.annotations.Options;
import org.bukkit.Material;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * BlockState configuration for jukebox blocks.
 * Allows setting the music disc.
 */
@AutoBlockStateMeta("jukebox")
public record JukeboxStateMeta(
        @Options(optional = true) Material record
) implements BlockStateMeta<Jukebox> {

    @Override
    public void apply(Player player, Jukebox jukebox) {
        if (record != null && record.isRecord()) {
            jukebox.setRecord(new ItemStack(record));
        }
    }
}