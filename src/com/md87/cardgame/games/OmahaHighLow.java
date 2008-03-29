/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

import com.md87.cardgame.Card;
import com.md87.cardgame.Deck;
import com.md87.cardgame.Player;
import com.md87.cardgame.Rank;
import com.md87.cardgame.Suit;
import com.md87.cardgame.hands.AceFiveLowHand;
import com.md87.cardgame.hands.StandardHand;
import com.md87.cardgame.interfaces.Hand;
import java.util.Collections;
import java.util.concurrent.Semaphore;

/**
 * Implements a standard (local) Omaha Hold'em game.
 *
 * @author Chris
 */
public class OmahaHighLow extends OmahaHoldEm {
    
    protected static final Deck minDeck = new Deck(
            new Card(Suit.CLUBS, Rank.EIGHT),
            new Card(Suit.CLUBS, Rank.SEVEN),
            new Card(Suit.CLUBS, Rank.SIX),
            new Card(Suit.CLUBS, Rank.FIVE),
            new Card(Suit.CLUBS, Rank.FOUR)
            );
    
    protected boolean isHigh = true;
    
    protected final Semaphore highSemaphore = new Semaphore(1);
    
    public OmahaHighLow(final int numplayers, final int bigblind, final int ante,
            final int raises) {
        super(numplayers, bigblind, ante, raises);
    }
    
    @Override
    protected void doWinner() {
        if (countPlayers(true, true, false) == 1) {
            doWinner(false);
            return;
        }
        
        highSemaphore.acquireUninterruptibly();
        
        isHigh = false;
        boolean isLowPlayer = false;
        for (Player player : players) {
            if (!player.isOut()) {
                if (new AceFiveLowHand(player.getBestDeck()).getBestRank() != AceFiveLowHand.Ranking.NON_QUALIFIER) {
                    isLowPlayer = true;
                }
            }
        }
        isHigh = true;
        
        if (isLowPlayer) {
            doWinner(true);
            isHigh = false;
            doWinner(true);
            isHigh = true;
        } else {
            doWinner(false);
        }
        
        highSemaphore.release();
    }
    
    @Override
    public Hand getHand(Deck deck) {
        if (isHigh) {
            return new StandardHand(deck);
        } else {
            return new AceFiveLowHand(deck);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public String getHandText(final Player player) {
        highSemaphore.acquireUninterruptibly();
        
        StringBuffer buff = new StringBuffer("High: ");
        isHigh = true;
        buff.append(getHand(player.getBestDeck()).getFriendlyName());
        buff.append("\nLow: ");
        isHigh = false;
        
        final AceFiveLowHand theHand = new AceFiveLowHand(player.getBestDeck());
        
        if (theHand.getBestRank() == AceFiveLowHand.Ranking.NO_PAIR) {
            final Deck theDeck = theHand.getDeck();
            StringBuffer other = new StringBuffer();
            boolean addAce = false;
            
            Collections.sort(theDeck);
            
            for (Card card : theDeck) {
                if (card.getRank() != Rank.ACE) {
                    other.append('-');
                    other.append("" + (14 - card.getRank().ordinal()));
                } else {
                    addAce = true;
                }
            }
            
            if (addAce) {
                buff.append('A');
                buff.append(other);
            } else {
                buff.append(other.substring(1));
            }
        } else {
            buff.append("doesn't qualify");
        }
        
        isHigh = true;
        
        highSemaphore.release();
        
        return buff.toString();
    }
    
}
