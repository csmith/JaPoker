/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.controllers;

import com.md87.cardgame.ui.GameWindow;
import com.md87.cardgame.controllers.ConservativeOpener;
import com.md87.cardgame.interfaces.Game;
import com.md87.cardgame.interfaces.PlayerController;

/**
 *
 * @author Chris
 */
public class ConservativeOpenerInfo extends ControllerInfo {

    public ConservativeOpenerInfo() {
    }

    public String getName() {
        return "AI: Conservative Opener";
    }

    public PlayerController getController(final Game game, final GameWindow gameWindow) {
        return new ConservativeOpener();
    }

}
