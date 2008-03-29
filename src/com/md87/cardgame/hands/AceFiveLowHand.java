/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.hands;

import com.md87.cardgame.Card;
import com.md87.cardgame.Deck;
import com.md87.cardgame.Rank;
import com.md87.cardgame.interfaces.Hand;

/**
 *
 * @author Chris
 */
public class AceFiveLowHand extends AbstractHand {
    
    public static enum Ranking {
        NO_PAIR,
        
        NON_QUALIFIER
    }
    
    protected Ranking bestRank;
    
    public AceFiveLowHand(final Deck cards) {
        this.cards = cards;
        
        bestRank = AceFiveLowHand.Ranking.NON_QUALIFIER;
        
        while (isOnePair(cards)) {
            cards.removeByRank(high, 1);
        }
        
        boolean valid = true;
        
        if (cards.size() >= 5) {
            kickers = new Deck(cards);
            kickers.reverseLimitTo(5);
            
            for (Card kicker : kickers) {
                if (kicker.getRank().compareTo(Rank.NINE, true) <= 0) {
                    valid = false;
                }
            }
        } else {
            valid = false;
        }
        
        if (valid) {
            bestRank = Ranking.NO_PAIR;
        }
    }
    
    public String getFriendlyName() {
        switch (bestRank) {
        case NO_PAIR:
            return kickers.toString();
        case NON_QUALIFIER:
            return "(Doesn't qualify)";
        default:
            return "Unknown hand";
        }
    }
    
    public Ranking getBestRank() {
        return bestRank;
    }
    
    public int compareTo(Hand ob) {
        final AceFiveLowHand o = (AceFiveLowHand) ob;
        
        if (o.getBestRank() == bestRank) {
            if (bestRank == Ranking.NON_QUALIFIER) {
                return 0;
            } else {
                return -1 * kickers.compareTo(o.getKickers(), true);
            }
        } else {
            return -1 * bestRank.compareTo(o.getBestRank());
        }
    }
    
}
