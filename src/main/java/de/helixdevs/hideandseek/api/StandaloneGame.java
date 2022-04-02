package de.helixdevs.hideandseek.api;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Closeable;
import java.io.IOException;

public abstract class StandaloneGame extends JavaPlugin implements Closeable, Listener {

    private final GameStencil stencil;
    private Game game;

    public StandaloneGame(GameStencil stencil) {
        this.stencil = stencil;
    }

    @Override
    public void onEnable() {
        this.game = new Game(this, this.stencil);
        Events.registerEvent(this, PlayerJoinEvent.class, event -> {
            event.joinMessage(null);

            Player player = event.getPlayer();
            this.game.join(player);
        });
        Events.registerEvent(this, PlayerQuitEvent.class, event -> {
            event.quitMessage(null);
            this.game.quit(event.getPlayer());
        });
        Events.registerEvent(this, PaperServerListPingEvent.class, event -> {
            event.setMaxPlayers(stencil.getMaxPlayers());
            event.setNumPlayers(game.getPlayers().size());
        });
        this.game.start();

        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            this.game.join(onlinePlayer);
        }
    }

    @Override
    public void onDisable() {
        try {
            this.game.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        getServer().getPluginManager().disablePlugin(this);
    }
}
