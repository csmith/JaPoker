/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.cards;

/**
 *
 * @author chris
 */
public interface CardStyle {

    /** {@inheritDoc} */
    @Override
    String toString();
    
    /**
     * Retrieves the folder name for the card front.
     * 
     * @return The card front's folder name
     */
    String getFolderName();
    
}
