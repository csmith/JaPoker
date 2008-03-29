/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

import com.md87.cardgame.Card;
import com.md87.cardgame.Rank;
import com.md87.cardgame.Suit;

import java.util.Collections;

/**
 *
 * @author Chris
 */
public class AsianFiveCardStud extends FiveCardStud {
    
    public AsianFiveCardStud(final int numplayers, final int bigblind, final int ante,
            final int raises) {
        super(numplayers, bigblind, ante, raises);
    }
    
    @Override
    protected void shuffle() {
        deck.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if (rank.compareTo(Rank.SEVEN) <= 0) {
                    deck.add(new Card(suit, rank));
                }
            }
        }
        
        Collections.shuffle(deck);
    }
    
}
