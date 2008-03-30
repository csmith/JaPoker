/*
 * Copyright (c) Chris 'MD87' Smith, 2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.interfaces;

/**
 *
 * @author chris
 */
public interface BetController {
    
    /**
     * Retrieves the maximum bet allowed in the specified circumstances.
     * 
     * @param total The total of the current pot
     * @param lastBet The size of the last bet
     * @param lastRaise The size of the last raise
     * @return The maximum bet that may be played
     */
    int getMaxBet(int total, int lastBet, int lastRaise);
    
    /**
     * Retrieves the minimum bet allowed in the specified circumstances.
     * 
     * @param total The total of the current pot
     * @param lastBet The size of the last bet
     * @param lastRaise The size of the last raise
     * @return The minimum bet that may be played
     */
    int getMinBet(int total, int lastBet, int lastRaise);    

}
