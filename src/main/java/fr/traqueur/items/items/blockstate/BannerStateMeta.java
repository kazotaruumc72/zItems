package fr.traqueur.items.items.blockstate;

import fr.traqueur.items.api.annotations.AutoBlockStateMeta;
import fr.traqueur.items.api.items.BlockStateMeta;
import fr.traqueur.items.settings.models.PatternWrapper;
import fr.traqueur.structura.annotations.Options;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * BlockState configuration for banner blocks.
 * Allows setting banner patterns.
 */
@AutoBlockStateMeta("banner")
public record BannerStateMeta(
        @Options(optional = true) List<PatternWrapper> patterns
) implements BlockStateMeta<Banner> {

    @Override
    public void apply(Player player, Banner banner) {
        if (patterns != null && !patterns.isEmpty()) {
            banner.setPatterns(patterns.stream().map(PatternWrapper::toPattern).toList());
        }
    }
}