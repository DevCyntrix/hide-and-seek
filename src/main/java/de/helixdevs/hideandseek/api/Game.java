package de.helixdevs.hideandseek.api;

import com.google.common.base.Preconditions;
import de.helixdevs.hideandseek.api.event.GameJoinEvent;
import de.helixdevs.hideandseek.api.event.GamePreJoinEvent;
import de.helixdevs.hideandseek.api.event.GamePreQuitEvent;
import de.helixdevs.hideandseek.api.event.GameQuitEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serial;
import java.util.*;

public class Game implements Closeable {

    @NotNull
    private final JavaPlugin plugin;
    @NotNull
    private final GameStencil stencil;
    @NotNull
    private final String name;
    private final Set<Player> playerSet = new HashSet<>();

    private Map<Class<? extends Service>, Service> serviceMap;
    private GameSequence sequence;

    private boolean running;

    public Game(@NotNull JavaPlugin plugin, @NotNull GameStencil stencil) {
        this.plugin = plugin;
        this.stencil = stencil;
        this.name = stencil.getName();
    }

    public boolean join(@NotNull Player player) {
        if (playerSet.contains(player))
            return false;

        GamePreJoinEvent preJoinEvent = new GamePreJoinEvent(player, this);
        plugin.getServer().getPluginManager().callEvent(preJoinEvent);
        if (preJoinEvent.isCancelled())
            return false;
        playerSet.add(player);
        plugin.getServer().getPluginManager().callEvent(new GameJoinEvent(player, this));
        return true;
    }

    public boolean quit(@NotNull Player player) {
        if (!this.playerSet.contains(player))
            return false;
        GamePreQuitEvent preQuitEvent = new GamePreQuitEvent(player, this);
        plugin.getServer().getPluginManager().callEvent(preQuitEvent);
        if (preQuitEvent.isCancelled())
            return false;
        this.playerSet.remove(player);
        player.getServer().getPluginManager().callEvent(new GameQuitEvent(player, this));
        return true;
    }

    public void start() {
        if (running)
            return;
        Service[] services = stencil.createServices(plugin, this);
        this.serviceMap = new HashMap<>(services.length);
        for (Service service : services) {
            this.serviceMap.put(service.getClass(), service);
        }

        GamePhase[] phases = stencil.createPhases(plugin, this);
        Preconditions.checkArgument(phases.length > 0);
        this.sequence = new GameSequence(Arrays.asList(phases).listIterator());
        this.sequence.start();
    }

    @Override
    public void close() throws IOException {
        if (!running)
            return;
        this.sequence.close();
        this.serviceMap.forEach((aClass, service) -> {
            try {
                service.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.serviceMap.clear();
    }

    /**
     * @return an unmodifiable collection of the playing users.
     */
    public @NotNull Collection<@NotNull Player> getPlayers() {
        return Collections.unmodifiableCollection(playerSet);
    }

    public int getPlayerCount() {
        return getPlayers().size();
    }

    public int getMaxPlayers() {
        return this.stencil.getMaxPlayers();
    }

    public Audience getAudience() {
        return (ForwardingAudience) this::getPlayers;
    }

    public @NotNull String getName() {
        return name;
    }

    public @Nullable GameSequence getSequence() {
        return sequence;
    }

    public @NotNull Collection<@NotNull Service> getServices() {
        return this.serviceMap.values();
    }

    public <T extends Service> @Nullable T getService(Class<T> serviceClass) {
        return (T) this.serviceMap.get(serviceClass);
    }
}
