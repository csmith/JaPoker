/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.interfaces;

import com.md87.cardgame.Deck;

/**
 *
 * @author Chris
 */
public interface Hand extends Comparable<Hand> {
    
    String getFriendlyName();

    Deck getDeck();
    
}
