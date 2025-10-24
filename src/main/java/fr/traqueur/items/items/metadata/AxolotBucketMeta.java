package fr.traqueur.items.items.metadata;

import fr.traqueur.items.api.annotations.MetadataMeta;
import fr.traqueur.items.api.items.ItemMetadata;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.AxolotlBucketMeta;
import org.jetbrains.annotations.Nullable;

@MetadataMeta("axolot-bucket")
public record AxolotBucketMeta(Axolotl.Variant variant) implements ItemMetadata {

    @Override
    public void apply(ItemStack itemStack, @Nullable Player player) {
        itemStack.editMeta(meta -> {
            if (meta instanceof AxolotlBucketMeta axolotlBucketMeta) {
                axolotlBucketMeta.setVariant(variant);
            }
        });
    }
}
