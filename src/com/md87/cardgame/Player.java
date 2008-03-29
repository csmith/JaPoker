/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame;

import com.md87.cardgame.interfaces.Game;
import com.md87.cardgame.interfaces.PlayerController;

/**
 *
 * @author Chris
 */
public class Player implements Comparable<Player> {
    
    public static enum OpenCheck {OPEN, CHECK};
    public static enum CallRaiseFold {CALL, RAISE, FOLD};
    
    private String name;
    private Game game;
    
    private int cash;
    private int bet;
    
    private PlayerController controller;
    
    private final Deck cards = new Deck();
    private Deck bestDeck = null;
    
    private boolean hasFolded = false;
    private boolean isOut = false;
    private boolean isAllIn = false;
    
    public Player(final Game game, final String name, final int cash, final PlayerController controller) {
        this.name = name;
        this.cash = cash;
        this.game = game;
        
        this.controller = controller;
        controller.setPlayer(this);
        controller.setGame(game);
    }
    
    public Deck getCards() {
        return cards;
    }
    
    public Deck getBestDeck() {
        calculateBestDeck();
        
        return bestDeck;
    }
    
    public Deck doCardDiscard(int min, int max) {
        return controller.discardCards(min, max);
    }
    
    public void calculateBestDeck() {
        bestDeck = game.getBestDeck(cards);
    }
    
    public OpenCheck doOpenCheck() {
        OpenCheck res = controller.doOpenCheck();
        
        if (cash <= 0 && res == OpenCheck.OPEN) {
            res = OpenCheck.CHECK;
        }
        
        return res;
    }
    
    public CallRaiseFold doCallRaiseFold(final int callAmount, boolean canRaise) {
        CallRaiseFold res = controller.doCallRaiseFold(callAmount, canRaise);
        
        if (cash - callAmount <= 0 && res == CallRaiseFold.RAISE) {
            res = Player.CallRaiseFold.CALL;
        }
        
        return res;
    }
    
    public boolean shouldShowCards() {
        return controller.shouldShowCards();
    }
    
    public void forceBet(final int blind) {
        int size = Math.min(cash, blind);
        
        bet += size;
        cash -= size;
        
        if (cash == 0) {
            isAllIn = true;
        }
    }
    
    public void resetBet() {
        this.bet = 0;
    }
    
    public void addCash(final int amount) {
        cash += amount;
    }
    
    public void setOut() {
        isOut = true;
    }
    
    public String getName() {
        return name + " (" + (isAllIn ? "all in" : cash) + ")";
    }
    
    public void setFold() {
        hasFolded = true;
    }
    
    public void dealCard(final Card card) {
        cards.add(card);
    }
    
    public void removeCard(final Card card) {
        cards.remove(card);
    }
    
    public void discardCards() {
        synchronized(cards) {
            cards.clear();
        }
        
        bet = 0;
        hasFolded = false;
        isOut = cash <= 0;
        isAllIn = false;
        bestDeck = null;
    }
    
    public int getCash() {
        return cash;
    }
    
    public int getBet() {
        return bet;
    }
    
    public boolean hasFolded() {
        return hasFolded;
    }
    
    public boolean isOut() {
        return isOut;
    }
    
    public boolean isAllIn() {
        return isAllIn;
    }
    
    public int getRaiseAmount(final int minimum) {
        if (minimum > cash) {
            return cash;
        } else {
            return controller.getRaise(minimum);
        }
    }
    
    public int compareTo(Player o) {
        return -1 * game.getHand(getBestDeck()).compareTo(game.getHand(o.getBestDeck()));
    }
    
    public boolean isLocalHuman() {
        return controller.isLocalHuman();
    }
    
}
