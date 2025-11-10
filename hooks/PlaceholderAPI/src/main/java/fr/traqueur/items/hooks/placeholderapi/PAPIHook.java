package fr.traqueur.items.hooks.placeholderapi;

import fr.traqueur.items.api.Logger;
import fr.traqueur.items.api.annotations.AutoHook;
import fr.traqueur.items.api.hooks.Hook;
import fr.traqueur.items.api.placeholders.PlaceholderParser;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

@AutoHook("PlaceholderAPI")
public class PAPIHook implements Hook, PlaceholderParser {

    @Override
    public void onEnable() {
        PlaceholderParser.Holder.setInstance(this);
        Logger.info("PlaceholderAPI hook enabled and registered as global placeholder parser.");
    }

    @Override
    public String parse(Player player, String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        return PlaceholderAPI.setPlaceholders(player, string);
    }

}
