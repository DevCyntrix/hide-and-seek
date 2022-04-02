package de.helixdevs.hideandseek.api;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class GameContext {

    private final JavaPlugin plugin;
    private final Game game;

    public GameContext(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public @NotNull JavaPlugin getPlugin() {
        return plugin;
    }

    public @NotNull Game getGame() {
        return game;
    }


}
