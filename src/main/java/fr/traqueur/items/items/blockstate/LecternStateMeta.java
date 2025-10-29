package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.items.settings.models.ItemStackWrapper;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.annotations.defaults.DefaultInt;
import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;

/**
 * BlockState configuration for lectern blocks.
 * Allows setting the book and page.
 */
@AutoBlockStateMeta("lectern")
public record LecternStateMeta(
        @Options(optional = true) ItemStackWrapper book,
        @Options(optional = true) @DefaultInt(0) int page
) implements BlockStateMeta<Lectern> {

    @Override
    public void apply(Player player, Lectern lectern) {
        if (book != null) {
            lectern.getInventory().setItem(0, book.build(player));
        }

        if (page >= 0) {
            lectern.setPage(page);
        }
    }
}