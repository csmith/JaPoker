/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
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
    @Override
    public CallRaiseFold doCallRaiseFold(int callAmount, boolean canRaise) {
        return CallRaiseFold.CALL;
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public OpenCheck doOpenCheck() {
        return OpenCheck.CHECK;
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public boolean shouldShowCards() {
        return false;
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void setPlayer(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void setGame(Game game) {
        game.registerObserver(this);
    }

    /** {@inheritDoc} */
    @Override
    public int getRaise(int minimum) {
        return minimum;
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void communityCardsUpdated() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void playerCardsUpdated() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void playersTurn(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void newPlayer(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void newGame() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void endGame() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void setDealer(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void placeBlind(Player player, int blind, String name) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void raise(Player player, int amount) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void fold(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void call(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void check(Player player) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void open(Player player, int amount) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void winner(Player players) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void showdown() {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public boolean isLocalHuman() {
        return false;
    }
    
    /** {@inheritDoc} */
    @Override
    public Deck discardCards(final int minimum, final int maximum) {
        return new Deck();
    }

    @Override
    public void discards(Player player, int number) {
        // Do nothing
    }

    @Override
    public void cardDealt(Player player, Card card) {
        // Do nothing
    }

}
