/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.controllers;

import com.md87.cardgame.Deck;
import com.md87.cardgame.Player;
import com.md87.cardgame.Player.CallRaiseFold;
import com.md87.cardgame.Player.OpenCheck;
import com.md87.cardgame.interfaces.Game;
import com.md87.cardgame.interfaces.PlayerController;

/**
 *
 * @author Chris
 */
public class RandomPlayer implements PlayerController {
    
    protected Player player;
    protected Game game;

    public CallRaiseFold doCallRaiseFold(int callAmount, boolean canRaise) {
        if (Math.random() < 0.3) {
            if (player.getCash() - callAmount < game.getBigBlind() || !canRaise) {
                return CallRaiseFold.FOLD;
            } else {
                return CallRaiseFold.RAISE;
            }
        } else {
            return CallRaiseFold.CALL;
        }
    }

    public OpenCheck doOpenCheck() {
        if (Math.random() < 0.5 || player.getCash() < game.getBigBlind()) {
            return OpenCheck.CHECK;
        } else {
            return OpenCheck.OPEN;
        }
    }

    public boolean shouldShowCards() {
        return false;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public void setGame(Game game) {
        this.game = game;
    }

    public int getRaise(int minimum) {
        return minimum;
    }
    
    /** {@inheritDoc} */
    public boolean isLocalHuman() {
        return false;
    }

    /** {@inheritDoc} */
    public Deck discardCards(final int minimum, final int maximum) {
        return new Deck(player.getCards().subList(0, minimum));
    }

}
