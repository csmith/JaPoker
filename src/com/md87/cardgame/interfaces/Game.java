/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.interfaces;

import com.md87.cardgame.Deck;
import com.md87.cardgame.Player;

import java.util.List;

/**
 * Defines a standard interface for games.
 *
 * @author Chris
 */
public interface Game {
    
    /**
     * Starts a new tournament.
     */
    void startTournament();
    
    /**
     * Returns the current total size of the pot.
     *
     * @return The size of the pot
     */
    int getCurrentPot();
    
    /**
     * Returns the number of hole cards generally used by this game.
     * 
     * @return The usual number of hole cards
     */
    int holeCardCount();
    
    /**
     * Returns the player who currently holds the dealer token.
     *
     * @return The current dealer
     */
    Player getDealer();
    
    /**
     * Returns the (visible) community cards.
     *
     * @return The community cards
     */
    Deck getCommunityCards();
    
    /**
     * Retrieves the size of the big blind for this game.
     *
     * @return The size of the big blind
     */
    int getBigBlind();
    
    /**
     * Retrieves the size of the maximum bet that has been placed.
     *
     * @return The maximum bet
     */
    int getMaxBet();
    
    /**
     * Retrieves a list of players that are part of this game.
     *
     * @return A list of players participating in the game
     */
    List<Player> getPlayers();
    
    /**
     * Adds a new player to this game.
     *
     * @param name The name of the player that's being added
     * @param cash The amount of cash the player has
     * @param controller The controller for the player
     */
    void addPlayer(String name, int cash, PlayerController controller);    
    
    /**
     * Adds a new player to this game.
     *
     * @param player The player to be added
     */
    void addPlayer(Player player);
    
    /**
     * Retrieves the total number of players in this game.
     *
     * @return The number of players in this game
     */
    int getNumPlayers();
    
    /**
     * Retrieves the best possible deck made up of the community cards and
     * some of the specified hole cards, if applicable.
     * 
     * @param cards The hole cards to be used
     * @return The best possible deck made up of the hole and community cards
     */
    Deck getBestDeck(final Deck cards);
    
    /**
     * Determines if this game has an active, local, human player.
     *
     * @return True if the game has an active, local, human player.
     */
    boolean hasActiveHuman();
    
    /**
     * Retrieves a hand for the specified deck.
     * 
     * @param deck The deck to be used for the hand
     * @return A hand containing the cards in the specified deck
     */
    Hand getHand(final Deck deck);
    
    /**
     * Retrieve the text to display for the specified player's hand(s).
     * 
     * @param player The player whose text should be determined
     * @return A textual description of the player's hand(s).
     */
    String getHandText(final Player player);
    
    /**
     * Registers a new game observer for this game.
     *
     * @param observer The game observer to be registered
     */
    void registerObserver(GameObserver observer);
    
    /**
     * Unregisters a game observer for this game.
     *
     * @param observer The game observer to be unregistered
     */
    void unregisterObserver(GameObserver observer);
    
}
