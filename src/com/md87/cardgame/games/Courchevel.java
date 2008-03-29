/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

import com.md87.cardgame.Deck;

/**
 * Implements a standard (local) Courchevel game.
 *
 * @author Chris
 */
public class Courchevel extends OmahaHoldEm {

    public Courchevel(int numplayers, int bigblind, int ante, int raises) {
        super(numplayers, bigblind, ante, raises);
    }

    @Override
    public Deck getCommunityCards() {
        if (community.isEmpty()) {
            return new Deck();
        } else if (!doneFlop || community.size() < 3) {
            return new Deck(community.subList(0, 1));
        } else if (!doneTurn || community.size() < 4) {
            return new Deck(community.subList(0, 3));
        } else if (!doneRiver || community.size() < 5) {
            return new Deck(community.subList(0, 4));
        } else {
            return new Deck(community);
        }
    }

}
