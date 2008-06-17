/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.interfaces;

import com.md87.cardgame.Card;
import com.md87.cardgame.Player;

/**
 * The main interface for objects that wish to observe game events as they
 * happen.
 * 
 * @author Chris
 */
public interface GameObserver {

    /**
     * Called when the community cards have been updated.
     */
    void communityCardsUpdated();
    
    /**
     * Called when the players' hole cards have been updated.
     * 
     * @deprecated Behaviour should be done in cardDealt instead
     */
    @Deprecated
    void playerCardsUpdated();
    
    /**
     * Called when a card is dealt to the specified player.
     * 
     * @param player The player the card is being dealt to
     * @param card The card that was dealt
     */
    void cardDealt(final Player player, final Card card);
    
    /**
     * Called at the start of a player's turn.
     * 
     * @param player The player whose turn it is now
     */
    void playersTurn(final Player player);
    
    /**
     * Called when a new player enters the game.
     * 
     * @param player The player that has joined
     */
    void newPlayer(final Player player);
    
    /**
     * Called at the start of a game.
     */
    void newGame();
    
    /**
     * Called at the end of a game.
     */
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
