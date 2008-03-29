/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

/**
 * Implements a standard (local) Super Hold'em game.
 *
 * @author Chris
 */
public class SuperHoldEm extends TexasHoldEm {

    public SuperHoldEm(int numplayers, int bigblind, int ante, int raises) {
        super(numplayers, bigblind, ante, raises);
    }

    @Override
    protected void dealPlayerCards() {
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
        dealCard(players.get((dealer + 1) % numplayers), false);
    }

    @Override
    public int holeCardCount() {
        return 3;
    }

}
