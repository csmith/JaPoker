/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.controllers;

import com.md87.cardgame.ui.GameWindow;
import com.md87.cardgame.interfaces.Game;
import com.md87.cardgame.interfaces.PlayerController;

/**
 *
 * @author Chris
 */
public abstract class ControllerInfo {

    public abstract String getName();
    
    public abstract PlayerController getController(final Game game, final GameWindow gameWindow);
    
    public String toString() {
        return getName();
    }
    
    public static ControllerInfo[] getControllers() {
        return new ControllerInfo[]{
            new HumanPlayerInfo(),
            new ConservativeOpenerInfo(),
            new EndGameInfo(),
            new RandomPlayerInfo()
        };
    }

}
