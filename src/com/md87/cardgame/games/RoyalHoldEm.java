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
 * Implements a standard Royal Hold'Em game.
 *
 * @author Chris
 */
public class RoyalHoldEm extends TexasHoldEm {
    
    /**
     * Creates a new instance of RoyalHoldEm.
     *
     * @param numplayers The number of players who are taking part
     * @param bigblind The size of the big blind
     * @param ante The size of the ante
     * @param raises The maximum number of raises per round
     */
    public RoyalHoldEm(final int numplayers, final int bigblind, final int ante,
            final int raises) {
        super(numplayers, bigblind, ante, raises);
        
        this.numplayers = Math.min(6, numplayers);
    }
    
    /** {@inheritDoc} */
    @Override
    protected void shuffle() {
        deck.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if (rank.compareTo(Rank.TEN) <= 0) {
                    deck.add(new Card(suit, rank));
                }
            }
        }
        
        Collections.shuffle(deck);
    }
    
    
    
}
