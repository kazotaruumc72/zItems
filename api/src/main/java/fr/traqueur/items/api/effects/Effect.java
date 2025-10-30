package fr.traqueur.items.api.effects;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface Effect {

    String id();

    String type();

    EffectSettings settings();


    Component displayName();

}
