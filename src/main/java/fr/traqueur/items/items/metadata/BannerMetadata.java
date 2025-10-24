package fr.traqueur.items.items.metadata;

import fr.traqueur.items.api.annotations.MetadataMeta;
import fr.traqueur.items.api.items.ItemMetadata;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@MetadataMeta("banner")
public record BannerMetadata(List<PatternSettings> patterns) implements ItemMetadata {

    @Override
    public void apply(ItemStack itemStack, @Nullable Player player) {
        itemStack.editMeta(meta -> {
            if (meta instanceof BannerMeta bannerMeta) {
                bannerMeta.setPatterns(patterns.stream().map(PatternSettings::toPattern).toList());
            }
        });
    }

    public record PatternSettings(PatternType type, DyeColor color) implements Loadable {

        public Pattern toPattern() {
            return new Pattern(color, type);
        }

    }

}
