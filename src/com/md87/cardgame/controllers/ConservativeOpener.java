/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.controllers;

import com.md87.cardgame.Card;
import com.md87.cardgame.Player.CallRaiseFold;

/**
 *
 * @author Chris
 */
public class ConservativeOpener extends RandomPlayer {
    
    @Override
    public CallRaiseFold doCallRaiseFold(int callAmount, boolean canRaise) {
        Card c1 = player.getCards().get(0);
        Card c2 = player.getCards().get(1);
        
        if (c1.getSuit() == c2.getSuit() || Math.abs(c1.getRank().compareTo(c2.getRank())) < 2) {
            return super.doCallRaiseFold(callAmount, canRaise);
        } else {
            return CallRaiseFold.FOLD;
        }
    }
    
    
    
}
