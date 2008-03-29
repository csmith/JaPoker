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
 * Implements a standard (local) Texas Hold'em game.
 *
 * @author Chris
 */
public class TexasHoldEm extends AbstractGame {
    
    protected final Deck community = new Deck();
    
    protected boolean doneFlop = false;
    protected boolean doneTurn = false;
    protected boolean doneRiver = false;
    
    public TexasHoldEm(final int numplayers, final int bigblind, final int ante,
            final int raises) {
        super(numplayers, bigblind, ante, raises);
    }
    
    protected void dealPlayerCards() {
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
    }
    
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
    
    /** {@inheritDoc} */
    public Deck getCommunityCards() {
        if (!doneFlop || community.size() < 3) {
            return new Deck();
        } else if (!doneTurn || community.size() < 4) {
            return new Deck(community.subList(0, 3));
        } else if (!doneRiver || community.size() < 5) {
            return new Deck(community.subList(0, 4));
        } else {
            return new Deck(community);
        }
    }
    
    /** {@inheritDoc} */
    public int holeCardCount() {
        return 2;
    }
    
    protected boolean canDoBringIns() {
        return true;
    }
}
