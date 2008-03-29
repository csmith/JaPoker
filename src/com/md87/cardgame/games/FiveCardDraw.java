/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

import com.md87.cardgame.Deck;
import com.md87.cardgame.Player;

/**
 * Implements a standard (local) Five Card Draw game.
 * 
 * @author chris
 */
public class FiveCardDraw extends AbstractGame {

    public FiveCardDraw(int numplayers, int bigblind, int ante, int raises) {
        super(numplayers, bigblind, ante, raises);
    }

    @Override
    protected void startGame() {
        notifyNewGame();
        
        discardCards();
        shuffle();
        
        doAntes();
        doBlinds();
        
        for (int i = 0; i < 5; i++) {
            dealCard(players.get((dealer + 1) % numplayers), false);
        }
        
        waitForBets();
        
        if (countPlayers(true, true, false) > 1) {
            doDrawRound(players.get((dealer + 1) % numplayers), 0, 5, true);
            doBettingRound();
        }        
        
        if (countPlayers(true, true, false) > 1) {
            doShowDown();
        } else {
            doWinner();
        }
        
        for (Player player : players) {
            if (player.getCash() <= 0) {
                player.setOut();
            }
        }
        
        notifyEndGame();
        
        doDealerAdvance();        
    }

    @Override
    protected boolean canDoBringIns() {
        return false;
    }

    @Override
    public int holeCardCount() {
        return 5;
    }

    @Override
    public Deck getCommunityCards() {
        return new Deck();
    }

}
