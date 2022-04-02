package de.helixdevs.hideandseek.api;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;

public abstract class GamePhase implements Listener, Closeable {

    private final JavaPlugin plugin;
    private final Game game;

    private boolean running;

    public GamePhase(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    protected abstract void onStart();

    protected abstract void onEnd();

    public void start() {
        onStart();
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
        this.running = true;
    }

    @Override
    public void close() {
        if (!running)
            return;
        onEnd();
        HandlerList.unregisterAll(this);
        this.running = false;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Game getGame() {
        return game;
    }

    public GameSequence getGameSequence() {
        return getGame().getSequence();
    }

    public <T extends Service> @Nullable T getService(Class<T> serviceClass) {
        return getGame().getService(serviceClass);
    }

    public void nextPhase() throws IOException {
        getGameSequence().next();
    }
}
