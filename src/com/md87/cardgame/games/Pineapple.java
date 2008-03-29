/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

/**
 * Implements a standard (local) game of Pineapple.
 * 
 * @author chris
 */
public class Pineapple extends TexasHoldEm {

    public Pineapple(int numplayers, int bigblind, int ante, int raises) {
        super(numplayers, bigblind, ante, raises);
    }

    @Override
    protected void dealPlayerCards() {
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
        
        doDrawRound(players.get((dealer + 1) % numplayers), 1, 1, false);
    }

    @Override
    public int holeCardCount() {
        return 3;
    }

}
