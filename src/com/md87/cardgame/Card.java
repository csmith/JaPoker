/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame;

/**
 * Represents a single card, consisting of a rank and suit combination.
 * 
 * @author Chris
 */
public class Card implements Comparable<Card> {
   
    /** The suit of this card. */
    private final Suit suit;
    
    /** The rank of this card. */
    private final Rank rank;
    
    /** Whether or not this card is public. */
    private boolean isPublic = false;

    /**
     * Creates a new instance of Card.
     * 
     * @param suit The suit of this card
     * @param rank The rank of this card
     */
    public Card(final Suit suit, final Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }
    
    /**
     * Sets the "public" property of this card. Public cards are displayed
     * to all players.
     * 
     * @param isPublic The new public property of this card
     */
    public void setPublic(final boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    /**
     * Determines if this card is public or not.
     * 
     * @return True if the card is public, false otherwise
     */
    public boolean isPublic() {
        return isPublic;
    }
    
    /**
     * Retrieves the rank of this card.
     * 
     * @return This card's rank
     */
    public Rank getRank() {
        return rank;
    }
    
    /**
     * Retrieves the suit of this card.
     * 
     * @return This card's suit
     */
    public Suit getSuit() {
        return suit;
    }
    
    /**
     * Retrieves the file name used to display this card.
     * 
     * @return The file name used to display this card
     */
    public String getFileName() {
        return (suit.toString().charAt(0) + rank.toString()).toLowerCase();
    }

    /** {@inheritDoc} */
    public int compareTo(Card o) {
        return compareTo(o, false);
    }
    
    public int compareTo(Card o, boolean acesLow) {
        return -1 * rank.compareTo(o.getRank(), acesLow);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return rank.toString() + "/" + suit.toString().charAt(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (this.suit != other.suit) {
            return false;
        }
        if (this.rank != other.rank) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.suit != null ? this.suit.hashCode() : 0);
        hash = 89 * hash + (this.rank != null ? this.rank.hashCode() : 0);
        return hash;
    }

}
