/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame;

/**
 * Defines the standard ranks in a pack of cards.
 * 
 * @author Chris
 */
public enum Rank {
    
    ACE,
    KING,
    QUEEN,
    JACK,
    TEN,
    NINE,
    EIGHT,
    SEVEN,
    SIX,
    FIVE,
    FOUR,
    THREE,
    DEUCE;
   
    /**
     * Retrieves the rank that is one lower than this one, or null if this is
     * the lowest rank (deuce).
     * 
     * @return The next lower rank
     */
    public Rank getLower() {
        for (Rank target : values()) {
            if (target.compareTo(this) == 1) {
                return target;
            }
        }
        
        return null;
    }
    
    /**
     * Returns a nicely capitalised version of this rank's name.
     * 
     * @return A capitalised version of this rank's name
     */
    public String capitalise() {
        return this.toString().substring(0, 1) + this.toString().substring(1).toLowerCase();
    }

    public int compareTo(final Rank o, final boolean acesLow) {
        // ^ is the XOR operator
        if (acesLow && (this == ACE ^ o == ACE)) {
            int thisPos = ordinal();
            int thatPos = o.ordinal();
            
            if (this == ACE) {
                thisPos = DEUCE.ordinal() + 1;
            } else if (o == ACE) {
                thatPos = DEUCE.ordinal() + 1;
            }
            
            return thisPos - thatPos;
        } else {
            return compareTo(o);
        }
    }
}
