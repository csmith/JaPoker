/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame;

/**
 *
 * @author chris
 */
public enum Achievement {
    
    ALL_IN_WIN("All in", "Go all in and win the hand"),
    TOURNAMENT_WIN("Winner", "Beat at least three other players to win a tournament"),
    FLUSH("Flushed", "Make a five-card flush"),
    HIGH_LOW("High/low", "Win both the high and low pots in a game of Omaha High/Low"),
    STREAK("Winning streak", "Win five consecutive hands in a tournament")
    ;
    
    protected String name;
    protected String desc;
    
    Achievement(final String name, final String desc) {
        this.name = name;
        this.desc = desc;
    }

}
