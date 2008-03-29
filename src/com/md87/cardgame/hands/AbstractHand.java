/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.hands;

import com.md87.cardgame.Deck;
import com.md87.cardgame.Rank;
import com.md87.cardgame.Suit;
import com.md87.cardgame.interfaces.Hand;

import java.util.Collections;

/**
 *
 * @author Chris
 */
public abstract class AbstractHand implements Hand {
    
    protected Deck cards;
    
    protected Rank high;
    protected Rank low;
    protected Deck kickers = new Deck();
    
    protected boolean usesLow = false;
    protected boolean usesAllCards = false;
    
    public Rank getHigh() {
        return high;
    }
    
    public Rank getLow() {
        return low;
    }
    
    public Deck getDeck() {
        return cards;
    }
    
    public Deck getKickers() {
        return kickers;
    }    
    
    protected boolean isStraightFlush(final Deck deck) {
        usesLow = false;
        usesAllCards = false;
        
        for (Suit suit : Suit.values()) {
            Deck mySuit = deck.getSuit(suit);
            
            if (isStraight(mySuit)) {
                return true;
            }
        }
        
        return false;
    }
    
    protected boolean isFourOfAKind(final Deck deck) {
        usesLow = false;
        usesAllCards = false;
        
        for (Rank rank : Rank.values()) {
            if (deck.getRank(rank).size() == 4) {
                high = rank;
                kickers = new Deck(deck);
                kickers.removeByRank(high, 4);
                kickers.limitTo(1);
                return true;
            }
        }
        
        return false;
    }
    
    protected boolean isFullHouse(final Deck deck) {
        usesLow = true;
        usesAllCards = false;
        high = null;
        low = null;
        
        for (Rank rank : Rank.values()) {
            if (high == null && deck.getRank(rank).size() >= 3) {
                high = rank;
            } else if (low == null && deck.getRank(rank).size() >= 2) {
                low = rank;
            }
            
            if (high != null && low != null) {
                return true;
            }
        }
        
        return false;
    }
    
    protected boolean isFlush(final Deck deck) {
        usesLow = false;
        usesAllCards = true;
        
        for (Suit suit : Suit.values()) {
            Deck myDeck = deck.getSuit(suit);
            
            if (myDeck.size() >= 5) {    
                Collections.sort(myDeck);
                
                high = myDeck.get(0).getRank();
                kickers.clear();
                
                myDeck.limitTo(5);
                deck.clear();
                deck.addAll(myDeck);
                
                return true;
            }
        }
        
        return false;
    }
    
    protected boolean isStraight(final Deck deck) {
        usesLow = false;
        usesAllCards = false;
        
        for (Rank rank : Rank.values()) {
            high = rank;
            low = rank;
            
            kickers.clear();
            
            boolean fail = false;
            
            for (int i = 0; i < 5; i++) {
                if (low == null || deck.getRank(low).size() == 0) {
                    fail = true;
                    break;
                }
                
                if (low == Rank.DEUCE && i == 3) {
                    low = Rank.ACE;
                } else {
                    low = low.getLower();
                }
            }
            
            if (!fail) {
                low = null;
                return true;
            }
        }
        
        low = null;
        return false;
    }
    
    protected boolean isThreeOfAKind(final Deck deck) {
        usesLow = false;
        usesAllCards = false;
        
        for (Rank rank : Rank.values()) {
            if (deck.getRank(rank).size() == 3) {
                high = rank;
                kickers = new Deck(deck);
                kickers.removeByRank(high, 3);
                kickers.limitTo(2);
                return true;
            }
        }
        
        return false;
    }
    
    protected boolean isTwoPairs(final Deck deck) {
        usesLow = true;
        usesAllCards = false;
        
        high = null;
        low = null;
        
        kickers = new Deck(deck);
        
        for (Rank rank : Rank.values()) {
            Deck myDeck = deck.getRank(rank);
            
            if (myDeck.size() == 2) {
                if (high == null) {
                    high = rank;
                    kickers.removeByRank(rank, 2);
                } else {
                    low = rank;
                    kickers.removeByRank(rank, 2);
                    kickers.limitTo(1);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    protected boolean isOnePair(final Deck deck) {
        usesLow = false;
        usesAllCards = false;
        
        for (Rank rank : Rank.values()) {
            if (deck.getRank(rank).size() > 1) {
                high = rank;
                kickers = new Deck(deck);
                kickers.removeByRank(high, 2);
                kickers.limitTo(3);
                return true;
            }
        }
        
        return false;
    }
    
}
