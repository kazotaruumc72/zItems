package fr.maxlego08.items.listener;

import fr.maxlego08.items.api.Item;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerListener extends ListenerAdapter {

    @Override
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {

        Block block = event.getBlock();
        Material material = block.getType();
        if (material != Material.SPAWNER) return;

        ItemStack itemStack = event.getItemInHand();
        if (!itemStack.hasItemMeta()) return;

        ItemMeta itemMeta = itemStack.getItemMeta();

        // Transfer MythicMobs mob type to the placed spawner block
        PersistentDataContainer itemPdc = itemMeta.getPersistentDataContainer();
        String mythicMobType = itemPdc.get(Item.MYTHICMOB_TYPE_KEY, PersistentDataType.STRING);
        if (mythicMobType != null && block.getState() instanceof CreatureSpawner spawner) {
            PersistentDataContainer spawnerPdc = spawner.getPersistentDataContainer();
            spawnerPdc.set(Item.MYTHICMOB_TYPE_KEY, PersistentDataType.STRING, mythicMobType);
            spawner.update();
        }

        // Transfer vanilla entity type
        if (itemMeta instanceof BlockStateMeta blockStateMeta) {
            if (blockStateMeta.getBlockState() instanceof CreatureSpawner creatureSpawner) {
                EntityType entity = creatureSpawner.getSpawnedType();
                if (block.getState() instanceof CreatureSpawner spawner) {
                    spawner.setSpawnedType(entity);
                    spawner.update();
                }
            }
        }
    }
}
