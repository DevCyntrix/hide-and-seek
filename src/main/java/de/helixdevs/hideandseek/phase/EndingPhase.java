package de.helixdevs.hideandseek.phase;

import de.helixdevs.hideandseek.api.Game;
import de.helixdevs.hideandseek.api.GamePhase;
import de.helixdevs.hideandseek.api.GameSequence;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EndingPhase extends GamePhase {

    public EndingPhase(@NotNull JavaPlugin plugin, @NotNull Game game) {
        super(plugin, game);
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onEnd() {

    }
}
