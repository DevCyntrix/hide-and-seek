package de.helixdevs.hideandseek;

import de.helixdevs.hideandseek.api.StandaloneGame;

public class HideAndSeekPlugin extends StandaloneGame {

    public HideAndSeekPlugin() {
        super(new HideAndSeekStencil());
    }
}
