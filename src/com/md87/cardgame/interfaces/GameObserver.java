/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.interfaces;

import com.md87.cardgame.Card;
import com.md87.cardgame.Player;

/**
 *
 * @author Chris
 */
public interface GameObserver {

    void communityCardsUpdated();
    
    void playerCardsUpdated();
    
    void cardDealt(final Player player, final Card card);
    
    void playersTurn(final Player player);
    
    void newPlayer(final Player player);
    
    void newGame();
    
    void endGame();
    
    void setDealer(final Player player);
    
    void placeBlind(final Player player, final int blind, final String name);
    
    void raise(final Player player, final int amount);
    
    void fold(final Player player);
    
    void call(final Player player);
    
    void check(final Player player);
    
    void open(final Player player, final int amount);
    
    void winner(final Player players);
    
    void discards(final Player player, final int number);
    
    void showdown();

}
