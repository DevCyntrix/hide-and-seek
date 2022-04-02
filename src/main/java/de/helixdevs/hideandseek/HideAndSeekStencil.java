package de.helixdevs.hideandseek;

import de.helixdevs.hideandseek.api.Game;
import de.helixdevs.hideandseek.api.GamePhase;
import de.helixdevs.hideandseek.api.GameStencil;
import de.helixdevs.hideandseek.api.Service;
import de.helixdevs.hideandseek.phase.EndingPhase;
import de.helixdevs.hideandseek.phase.LobbyPhase;
import de.helixdevs.hideandseek.phase.SeekingPhase;
import de.helixdevs.hideandseek.service.BlockSelectionService;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class HideAndSeekStencil extends GameStencil {

    public HideAndSeekStencil() {
        super("§3Hide and seek", HideAndSeek.maxPlayers);
    }

    @Override
    public GamePhase[] createPhases(@NotNull JavaPlugin plugin, @NotNull Game game) {
        return new GamePhase[]{
                new LobbyPhase(plugin, game),
                new SeekingPhase(plugin, game),
                new EndingPhase(plugin, game)
        };
    }

    @Override
    public Service[] createServices(@NotNull JavaPlugin plugin, @NotNull Game game) {
        return new Service[] {
                new BlockSelectionService(
                        Bukkit.createBlockData(Material.CRAFTING_TABLE),
                        Bukkit.createBlockData(Material.CHEST),
                        Bukkit.createBlockData(Material.BOOKSHELF))
        };
    }
}
