/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame;

/**
 * An enumeration of achievements that may be earnt by the player.
 * 
 * @author chris
 */
public enum Achievement {
   
    /** Win a hand after going all in. */
    ALL_IN_WIN("All in", "Go all in and win the hand"),
    /** Win a tournament. */
    TOURNAMENT_WIN("Winner", "Beat at least three other players to win a tournament"),
    /** Get a flush. */
    FLUSH("Flushed", "Make a five-card flush"),
    /** Win both high and low pots. */
    HIGH_LOW("High/low", "Win both the high and low pots in a game of Omaha High/Low"),
    /** Win five hands in a row. */
    STREAK("Winning streak", "Win five consecutive hands in a tournament"),
    /** Win a hand with no pair. */
    BEST_OF_WORST("Best of the worst", "Win a hand with the highest 'no pair' hand"),
    /** Go from last place to first. */
    COMEBACK("Comeback", "Win a tournament after being last at some point"),
    /** Poker addict. */
    ADDICT("Addicted to poker", "Play 100 tournaments");
    
    protected String name;
    protected String desc;
    
    Achievement(final String name, final String desc) {
        this.name = name;
        this.desc = desc;
    }

}
