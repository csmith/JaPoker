/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.hands;

import com.md87.cardgame.Deck;
import com.md87.cardgame.interfaces.Hand;

/**
 *
 * @author Chris
 */
public class StandardHand extends AbstractHand {
    
    public static enum Ranking {
        STRAIGHT_FLUSH,
        FOUR_OF_A_KIND,
        FULL_HOUSE,
        FLUSH,
        STRAIGHT,
        THREE_OF_A_KIND,
        TWO_PAIR,
        ONE_PAIR,
        NO_PAIR
    };
    
    protected Ranking bestRank;
    
    public StandardHand(final Deck cards) {
        this.cards = cards;
        
        if (isStraightFlush(cards)) {
            bestRank = StandardHand.Ranking.STRAIGHT_FLUSH;
        } else if (isFourOfAKind(cards)) {
            bestRank = StandardHand.Ranking.FOUR_OF_A_KIND;
        } else if (isFullHouse(cards)) {
            bestRank = StandardHand.Ranking.FULL_HOUSE;
        } else if (isFlush(cards)) {
            bestRank = StandardHand.Ranking.FLUSH;
        } else if (isStraight(cards)) {
            bestRank = StandardHand.Ranking.STRAIGHT;
        } else if (isThreeOfAKind(cards)) {
            bestRank = StandardHand.Ranking.THREE_OF_A_KIND;
        } else if (isTwoPairs(cards)) {
            bestRank = StandardHand.Ranking.TWO_PAIR;
        } else if (isOnePair(cards)) {
            bestRank = StandardHand.Ranking.ONE_PAIR;
        } else {
            high = null;
            low = null;
            usesLow = false;
            usesAllCards = false;
            kickers = new Deck(cards);
            kickers.limitTo(5);
            
            bestRank = StandardHand.Ranking.NO_PAIR;
        }
    }
    
    public Ranking getBestRank() {
        return bestRank;
    }
    
    public int compareTo(final Hand ob) {
        final StandardHand o = (StandardHand) ob;
        
        if (o.getBestRank() == bestRank) {
            if (o.getHigh() == high) {
                
                if (usesLow && low != o.getLow()) {
                    return -1 * low.compareTo(o.getLow());
                }
                
                if (usesAllCards) {
                    Deck myCards = new Deck(cards);
                    myCards.removeAll(kickers);
                    
                    Deck theirCards = new Deck(o.getDeck());
                    theirCards.removeAll(o.getKickers());
                    
                    int result = myCards.compareTo(theirCards);
                    
                    if (result != 0) {
                        return result;
                    }
                }
                
                return kickers.compareTo(o.getKickers());
            } else {
                return -1 * high.compareTo(o.getHigh());
            }
        } else {
            return -1 * bestRank.compareTo(o.getBestRank());
        }
    }
        
    public String getFriendlyName() {
        switch (bestRank) {
        case FLUSH:
            return "Flush";
        case FOUR_OF_A_KIND:
            return "Four of a kind: " + high.capitalise() + "s";
        case FULL_HOUSE:
            return "Full house: " + high.capitalise() + "s full of "
                    + low.capitalise() + "s";
        case NO_PAIR:
            return "No pair";
        case ONE_PAIR:
            return "One pair: " + high.capitalise() + "s";
        case STRAIGHT:
            return "Straight: " + high.capitalise() + " high";
        case STRAIGHT_FLUSH:
            return "Straight flush: " + high.capitalise() + " high";
        case THREE_OF_A_KIND:
            return "Three of a kind: " + high.capitalise() + "s";
        case TWO_PAIR:
            return "Two pair: " + high.capitalise() + "s over "
                    + low.capitalise() + "s";
        default:
            return "Unknown hand";
        }
    }
}