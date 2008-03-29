/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.controllers;

import com.md87.cardgame.Card;
import com.md87.cardgame.Deck;
import com.md87.cardgame.interfaces.GameObserver;
import com.md87.cardgame.Player;
import com.md87.cardgame.Player.CallRaiseFold;
import com.md87.cardgame.Player.OpenCheck;
import com.md87.cardgame.interfaces.Game;
import com.md87.cardgame.interfaces.PlayerController;

/**
 * Provides the server-side interface for network games. Each network player
 * listens on its own port, waiting for connections to come in.
 * 
 * @author Chris
 */
public class NetworkPlayer implements PlayerController, GameObserver {

    /**
     * Creates a new instance of NetworkPlayer.
     */
    public NetworkPlayer() {
    }

    /** {@inheritDoc} */
    public CallRaiseFold doCallRaiseFold(int callAmount, boolean canRaise) {
        return CallRaiseFold.CALL;
        // Do nothing
    }

    /** {@inheritDoc} */
    public OpenCheck doOpenCheck() {
        return OpenCheck.CHECK;
        // Do nothing
    }

    /** {@inheritDoc} */
    public boolean shouldShowCards() {
        return false;
        // Do nothing
    }

    /** {@inheritDoc} */
    public void setPlayer(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void setGame(Game game) {
        game.registerObserver(this);
    }

    /** {@inheritDoc} */
    public int getRaise(int minimum) {
        return minimum;
        // Do nothing
    }

    /** {@inheritDoc} */
    public void communityCardsUpdated() {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void playerCardsUpdated() {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void playersTurn(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void newPlayer(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void newGame() {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void endGame() {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void setDealer(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void placeBlind(Player player, int blind, String name) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void raise(Player player, int amount) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void fold(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void call(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void check(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void open(Player player, int amount) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void winner(Player players) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void showdown() {
        // Do nothing
    }

    /** {@inheritDoc} */
    public boolean isLocalHuman() {
        return false;
    }
    
    /** {@inheritDoc} */
    public Deck discardCards(final int minimum, final int maximum) {
        return new Deck();
    }

    public void discards(Player player, int number) {
        // Do nothing
    }

    public void cardDealt(Player player, Card card) {
        // Do nothing
    }

}
