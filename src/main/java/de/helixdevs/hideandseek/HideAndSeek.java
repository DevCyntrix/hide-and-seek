package de.helixdevs.hideandseek;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public interface HideAndSeek {

    Component prefix = Component.text("HS")
            .color(NamedTextColor.DARK_AQUA)
            .append(Component.text(" » ").color(NamedTextColor.DARK_GRAY));
    int maxPlayers = 16;
    int minPlayers = 1;


}
