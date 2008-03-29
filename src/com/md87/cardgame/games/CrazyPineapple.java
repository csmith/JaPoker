/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

import com.md87.cardgame.Player;

/**
 * Implements a standard local game of Crazy Pineapple.
 * 
 * @author chris
 */
public class CrazyPineapple extends Pineapple {

    public CrazyPineapple(int numplayers, int bigblind, int ante, int raises) {
        super(numplayers, bigblind, ante, raises);
    }
    
    @Override
    protected void dealPlayerCards() {
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
    }

    @Override
    protected void startGame() {
        notifyNewGame();
        
        discardCards();
        shuffle();
        
        community.clear();
        
        doAntes();
        
        doBlinds();
        
        doneFlop = false;
        doneTurn = false;
        doneRiver = false;        
        
        dealPlayerCards();
        
        for (int i = 0; i < 5; i++) {
            community.add(deck.deal());
        }
        
        waitForBets();
        
        if (countPlayers(true, true, false) > 1) {
            doneFlop = true;
            
            doDrawRound(players.get((dealer + 1) % numplayers), 1, 1, false);
            
            doBettingRound();
        }
        
        if (countPlayers(true, true, false) > 1) {
            doneTurn = true;
            
            doBettingRound();
        }
        
        if (countPlayers(true, true, false) > 1) {
            doneRiver = true;
            
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
    
}
