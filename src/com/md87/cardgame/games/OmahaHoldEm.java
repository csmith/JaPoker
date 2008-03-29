/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

import com.md87.cardgame.Deck;
import com.md87.cardgame.Player;
import com.md87.cardgame.interfaces.Hand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements a standard (local) Omaha Hold'em game.
 *
 * @author Chris
 */
public class OmahaHoldEm extends TexasHoldEm {
    
    public OmahaHoldEm(final int numplayers, final int bigblind, final int ante,
            final int raises) {
        super(numplayers, bigblind, ante, raises);
    }
    
    protected void startGame() {
        notifyNewGame();
        
        discardCards();
        shuffle();
        
        community.clear();
        
        doAntes();
        
        doBlinds();
        
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
        
        for (int i = 0; i < 5; i++) {
            community.add(deck.deal());
        }
        
        doneFlop = false;
        doneTurn = false;
        doneRiver = false;
        
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
    public int holeCardCount() {
        return 4;
    }
    
    @Override
    public Deck getBestDeck(final Deck cards) {
        if(cards.size() != 4) {
            System.err.println("getBestDeck(): card size != 4");
            new Exception().printStackTrace();
            return new Deck();
        }
        
        Deck tempCards = new Deck(cards);
        
        final List<Hand> hands = new ArrayList<Hand>();
        
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                final Deck base = new Deck();
                base.add(tempCards.get(i));
                base.add(tempCards.get(j));
                
                for (int k = 0; k < 5; k++) {
                    for (int l = k + 1; l < 5; l++) {
                        final Deck myDeck = new Deck(getCommunityCards());
                        myDeck.remove(getCommunityCards().get(k));
                        myDeck.remove(getCommunityCards().get(l));
                        myDeck.addAll(base);
                        
                        hands.add(getHand(myDeck));
                    }
                }
            }
        }
        
        Collections.sort(hands);
        
        return hands.get(hands.size() - 1).getDeck();
    }
    
}
