package de.helixdevs.hideandseek.api;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class GameStencil {

    @NotNull
    private final String name;
    private final int maxPlayers;

    public GameStencil(@NotNull String name, int maxPlayers) {
        this.name = name;
        this.maxPlayers = maxPlayers;
    }

    public abstract GamePhase[] createPhases(@NotNull JavaPlugin plugin, @NotNull Game game);

    public abstract Service[] createServices(@NotNull JavaPlugin plugin, @NotNull Game game);

    public @NotNull String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}
