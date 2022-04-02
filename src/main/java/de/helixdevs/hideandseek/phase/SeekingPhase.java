package de.helixdevs.hideandseek.phase;

import com.google.common.base.Preconditions;
import de.helixdevs.hideandseek.api.Game;
import de.helixdevs.hideandseek.api.GamePhase;
import de.helixdevs.hideandseek.service.BlockSelectionService;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SeekingPhase extends GamePhase {

    private Set<Player> playingPlayers;

    public SeekingPhase(@NotNull JavaPlugin plugin, @NotNull Game game) {
        super(plugin, game);
    }

    @Override
    protected void onStart() {

        Collection<@NotNull Player> players = getGame().getPlayers();
        this.playingPlayers = new HashSet<>(players);

        // Setting up players
        BlockSelectionService selectionService = getService(BlockSelectionService.class);
        Preconditions.checkNotNull(selectionService, "the selection service cannot be null");

        for (Player playingPlayer : this.playingPlayers) {
            BlockData data = selectionService.get(playingPlayer);
            if (data == null) {
                data = selectionService.selectRandom(playingPlayer);
            }
            // TODO: Make player to block
        }


    }

    @Override
    protected void onEnd() {

    }
}
