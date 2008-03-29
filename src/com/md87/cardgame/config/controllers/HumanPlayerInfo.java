/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.controllers;

import com.md87.cardgame.ui.GameWindow;
import com.md87.cardgame.controllers.HumanPlayer;
import com.md87.cardgame.interfaces.Game;
import com.md87.cardgame.interfaces.PlayerController;

/**
 *
 * @author Chris
 */
public class HumanPlayerInfo extends ControllerInfo {
    
    public HumanPlayerInfo() {
    }
    
    public String getName() {
        return "Human: Local Player";
    }
    
    public PlayerController getController(final Game game, final GameWindow gameWindow) {
        return new HumanPlayer(gameWindow);
    }

}
