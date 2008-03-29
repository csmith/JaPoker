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
 *
 * @author Chris
 */
public class SevenCardStud extends AbstractGame {
    
    protected final Deck community = new Deck();
    
    public SevenCardStud(final int numplayers, final int bigblind, final int ante,
            final int raises) {
        super(numplayers, bigblind, ante, raises);
    }
    
    /** {@inheritDoc} */
    public int holeCardCount() {
        return 7;
    }
    
    /** {@inheritDoc} */
    public Deck getCommunityCards() {
        return community;
    }
    
    protected void startGame() {
        notifyNewGame();
        
        discardCards();
        community.clear();
        
        shuffle();
        
        doAntes();
        
        doBlinds();
        
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), true);
        
        waitForBets();
        
        if (countPlayers(true, true, false) > 1) {
            dealCard(players.get((dealer + 1) % numplayers), true);
            doBettingRound();
        }
        
        if (countPlayers(true, true, false) > 1) {
            dealCard(players.get((dealer + 1) % numplayers), true);
            doBettingRound();
        }
        
        if (countPlayers(true, true, false) > 1) {
            dealCard(players.get((dealer + 1) % numplayers), true);
            doBettingRound();
        }
        
        if (countPlayers(true, true, false) > 1) {
            if (deck.size() >= countPlayers(true, true, false)) {
                dealCard(players.get((dealer + 1) % numplayers), false);
            } else {
                community.add(deck.deal());
                notifyCommunityCardsUpdated();
            }
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

    protected boolean canDoBringIns() {
        return true;
    }    
}
