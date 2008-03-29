/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Chris
 */
public class Deck extends ArrayList<Card> implements Comparable<Deck> {
    
    private static final long serialVersionUID = 1;
    
    public Deck() {
        super();
    }
    
    public Deck(final List<Card> parent) {
        super(parent);
    }
    
    public Deck(final Card ... cards) {
        super();
        
        for (Card card : cards) {
            add(card);
        }
    }
    
    public Deck getSuit(final Suit suit) {
        final Deck res = new Deck();
        
        for (Card card : this) {
            if (card.getSuit() == suit) {
                res.add(card);
            }
        }
        
        return res;
    }
    
    public Deck getRank(final Rank rank) {
        final Deck res = new Deck();
        
        for (Card card : this) {
            if (card.getRank() == rank) {
                res.add(card);
            }
        }
        
        return res;
    }
    
    public void removeByRank(final Rank rank, final int count) {
        final Deck newDeck = new Deck();
        int remaining = count;
        
        for (Card card : this) {
            if (card.getRank().equals(rank) && remaining > 0) {
                remaining--;
            } else {
                newDeck.add(card);
            }
        }
        
        clear();
        addAll(newDeck);
    }
    
    public Card deal() {
        final Card res = get(0);
        
        remove(0);
        
        return res;
    }
    
    public void limitTo(final int number) {
        Collections.sort(this);
        
        while (size() > number) {
            remove(0);
        }
    }
    
    public void reverseLimitTo(final int number) {
        Collections.sort(this);
        
        while (size() > number) {
            remove(size() - 1);
        }
    }    
    
    public int compareTo(Deck o) {
        return compareTo(o, false);
    }
    
    public int compareTo(Deck o, boolean acesLow) {
        Collections.sort(this);
        Collections.sort(o);
        
        if (size() != o.size()) {
            System.err.println("Warning: Deck.compareTo() called with different sized deck:");
            System.err.println("   Me: " + this);
            System.err.println(" Them: " + o);
            new Exception().printStackTrace();
        }
        
        for (int i = Math.min(this.size(), o.size()) - 1; i >= 0; i--) {
            int res = this.get(i).compareTo(o.get(i), acesLow);
            
            if (res != 0) {
                return res;
            }
        }
        
        return 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        
        for (Card card : this) {
            builder.append(' ');
            builder.append(card.toString());
        }
        
        return builder.length() == 0 ? "" : builder.substring(1);
    }
    
}
