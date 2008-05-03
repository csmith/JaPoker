/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.games;

import com.md87.cardgame.Card;
import com.md87.cardgame.Deck;
import com.md87.cardgame.Player;
import com.md87.cardgame.Rank;
import com.md87.cardgame.Suit;
import com.md87.cardgame.hands.StandardHand;
import com.md87.cardgame.interfaces.Game;
import com.md87.cardgame.interfaces.GameObserver;
import com.md87.cardgame.interfaces.Hand;
import com.md87.cardgame.interfaces.PlayerController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements some basic functions that may be used by a game.
 *
 * @author Chris
 */
public abstract class AbstractGame implements Game, Runnable {
    
    /** The maximum number of players this game will contain. */
    protected int numplayers = 0;
    
    /** Size of the big blind. */
    protected int bigblind = 0;
    /** Size of the ante. */
    protected int ante = 0;
    /** Maximum number of raises. */
    protected int raises = 4;
    
    protected int raisesLeft;
    
    protected int dealer = 0;
    protected int player = 0;
    
    /** A list of players who're playing in this game. */
    protected final List<Player> players = new ArrayList<Player>();
    
    /** The deck used for this game. */
    protected final Deck deck = new Deck();
    
    /** A list of observers registered for this game. */
    private final List<GameObserver> observers = new ArrayList<GameObserver>();
    
    public AbstractGame(final int numplayers, final int bigblind, final int ante,
            final int raises) {
        
        this.numplayers = numplayers;
        this.bigblind = bigblind;
        this.ante = ante;
        this.raises = raises;
        
        dealer = (int) Math.round(Math.random() * (numplayers - 1));
    }
    
    /** {@inheritDoc} */
    public void addPlayer(final String name, final int cash,
            final PlayerController controller) {
        
        if (numplayers <= players.size()) {
            return;
        }
        
        final Player myPlayer = new Player(this, name, cash, controller);
        
        addPlayer(myPlayer);
    }
    
    /** {@inheritDoc} */
    public void addPlayer(final Player player) {
        players.add(player);
        
        notifyNewPlayer(player);
    }
    
    /** {@inheritDoc} */
    public Player getDealer() {
        if (players.size() > dealer) {
            return players.get(dealer);
        } else {
            return null;
        }
    }
    
    /** {@inheritDoc} */
    public int getBigBlind() {
        return bigblind;
    }
    
    /** {@inheritDoc} */
    public int getCurrentPot() {
        int pot = 0;
        
        for (Player myPlayer : players) {
            pot += myPlayer.getBet();
        }
        
        return pot;
    }
    
    /** {@inheritDoc} */
    public int getMaxBet() {
        int max = 0;
        
        for (Player player : players) {
            if (player.getBet() > max) {
                max = player.getBet();
            }
        }
        
        return max;
    }
    
    /** {@inheritDoc} */
    public int getNumPlayers() {
        return numplayers;
    }
    
    /**
     * Clears and rebuilds the deck used for this game.
     */
    protected void shuffle() {
        deck.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
        
        Collections.shuffle(deck);
    }
    
    /**
     * Clears all player cards.
     */
    protected void discardCards() {
        for (Player player : players) {
            player.discardCards();
        }
    }
    
    /**
     * Deals a single card to each active player, starting with the
     * specified player, and working clockwise.
     *
     * @param start The player to start dealing with
     * @param isPublic Whether the card is a public card or not
     */
    protected void dealCard(final Player start, final boolean isPublic) {
        int i = players.indexOf(start);
        for (int x = 0; x < numplayers; x++) {
            final Player player = players.get((i + x) % numplayers);
            
            if (!player.isOut() && !player.hasFolded()) {
                final Card card = deck.deal();
                card.setPublic(isPublic);
                
                player.dealCard(card);
                notifyCardDealt(player, card);
                notifyPlayerCardsUpdated();
            }
        }
    }
    
    protected void doDrawRound(final Player start, final int min, final int max, boolean replace) {
        int i = players.indexOf(start);
        for (int x = 0; x < numplayers; x++) {
            final Player player = players.get((i + x) % numplayers);
            
            if (!player.isOut() && !player.hasFolded()) {
                notifyPlayersTurn(player);
                
                final Deck discarded = player.doCardDiscard(min, max);
                
                for (Card card : discarded) {
                    player.removeCard(card);
                    if (replace) {
                        player.dealCard(deck.deal());
                    }
                }
                
                notifyDiscards(player, discarded.size());
            }
        }
    }    
    
    /**
     * Counts the number of players with the specified properties.
     *
     * @param mustBeIn Whether or not the players must be in
     * @param mustNotFolded Whether or not the players must not have folded to be in
     * @param mustNotAllIn Whether or not the players must not be all in
     * @return The number of players matching the properties specified
     */
    public int countPlayers(final boolean mustBeIn,
            final boolean mustNotFolded, final boolean mustNotAllIn) {
        int count = 0;
        
        for (Player player : players) {
            if ((!mustBeIn || !player.isOut())
                    && (!mustNotFolded || !player.hasFolded())
                    && (!mustNotAllIn || !player.isAllIn())) {
                count++;
            }
        }
        
        return count;
    }
    
    /** {@inheritDoc} */
    public List<Player> getPlayers() {
        return players;
    }
    
    /** {@inheritDoc} */
    public void startTournament() {
        new Thread(this).start();
    }
    
    /** {@inheritDoc} */
    public void run() {
        while (countPlayers(true, false, false) > 1) {
            startGame();
        }
    }
    
    protected abstract void startGame();
    
    protected void doAntes() {
        if (ante > 0) {
            for (Player player : players) {
                if (!player.isOut()) {
                    player.forceBet(ante);
                    
                    notifyPlaceBlind(player, ante, "ante");
                }
            }
        }
    }
    
    protected void doBlinds() {
        if (countPlayers(true, false, false) > 2) {
            if (!players.get((dealer + 1) % numplayers).isOut()) {
                doSmallBlind(players.get((dealer + 1) % numplayers));
            }
            
            doBigBlind(players.get((dealer + 2) % numplayers));
            
            player = (dealer + 2) % numplayers;
        } else {
            Player big = null;
            Player small = null;
            
            for (Player player : players) {
                if (!player.isOut()) {
                    if (player == players.get(dealer)) {
                        small = player;
                    } else {
                        big = player;
                    }
                }
            }
            
            doSmallBlind(small);
            doBigBlind(big);
            
            player = players.indexOf(big);
        }
    }
    
    protected void doSmallBlind(final Player player) {
        player.forceBet(bigblind / 2);
        
        notifyPlaceBlind(player, bigblind / 2, "small blind");
    }
    
    protected void doBigBlind(final Player player) {
        player.forceBet(bigblind);
        
        notifyPlaceBlind(player, bigblind, "big blind");
    }
    
    @SuppressWarnings("fallthrough")
    protected void waitForBets() {
        int maxbet = getMaxBet();
        int endPlayer = player;
        boolean reachedEnd = false;
        raisesLeft = raises;
        Player myPlayer;
        
        do {
            player = (player + 1) % numplayers;
            myPlayer = players.get(player);
            
            if (!myPlayer.hasFolded() && !myPlayer.isAllIn() && !myPlayer.isOut()) {
                notifyPlayersTurn(myPlayer);
                
                if (playersHaveBet(maxbet)) {
                    // He can check or open
                    switch (myPlayer.doOpenCheck()) {
                    case CHECK:
                        // Do nothing
                        
                        notifyCheck(myPlayer);
                        break;
                    case OPEN:
                        int raiseAmount = myPlayer.getRaiseAmount(bigblind);
                        
                        myPlayer.forceBet(raiseAmount);
                        maxbet += raiseAmount;
                        
                        notifyOpen(myPlayer, raiseAmount);
                        break;
                    }
                } else {
                    final boolean canRaise = raisesLeft != 0
                            && countPlayers(true, true, true) > 1;
                    
                    // He can call, raise or fold
                    switch (myPlayer.doCallRaiseFold(maxbet - myPlayer.getBet(), canRaise)) {
                    case RAISE:
                        if (canRaise) {
                            myPlayer.forceBet(maxbet - myPlayer.getBet());
                            
                            int raiseAmount = myPlayer.getRaiseAmount(bigblind);
                            myPlayer.forceBet(raiseAmount);
                            maxbet += raiseAmount;
                            
                            notifyRaise(myPlayer, raiseAmount);
                            
                            raisesLeft--;
                            
                            break;
                        } // Fall through: call instead
                    case CALL:
                        myPlayer.forceBet(maxbet - myPlayer.getBet());
                        
                        notifyCall(myPlayer);
                        break;
                    case FOLD:
                        myPlayer.setFold();
                        
                        notifyFold(myPlayer);
                        break;
                    }
                }
            }
            
            if (player == endPlayer) {
                reachedEnd = true;
            }
            
        } while (!playersHaveBet(maxbet) || (!reachedEnd && countPlayers(true, true, true) > 1));
    }
    
    protected void doDealerAdvance() {
        if (countPlayers(true, false, false) > 2) {
            do {
                dealer = (dealer + 1) % numplayers;
            } while (players.get((dealer + 2) % numplayers).isOut());
        } else if (countPlayers(true, false, false) == 2) {
            do {
                dealer = (dealer + 1) % numplayers;
            } while (players.get(dealer).isOut());
        }
    }
    
    protected void doBettingRound() {
        notifyCommunityCardsUpdated();
        
        player = dealer;
        waitForBets();
    }
    
    protected boolean playersHaveBet(final int bet) {
        for (Player player : players) {
            if (!player.isOut() && !player.hasFolded() && !player.isAllIn()) {
                if (player.getBet() < bet) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    protected void doShowDown() {
        for (Player player : players) {
            if (!player.isOut()) {
                player.calculateBestDeck();
            }
        }
        
        notifyShowdown();
        
        doWinner();
        
        for (Player player : players) {
            player.resetBet();
        }
    }
    
    protected void doWinner() {
        doWinner(false);
    }
    
    protected void doWinner(final boolean doHalf) {
        if (countPlayers(true, true, false) == 1) {
            int pot = 0;
            Player winner = null;
            
            for (Player player : players) {
                if (!player.isOut()) {
                    
                    if (!player.hasFolded()) {
                        winner = player;
                    }
                    
                    if (doHalf) {
                        pot += player.getBet() / 2;
                    } else {
                        pot += player.getBet();
                    }
                }
            }
            
            winner.addCash(pot);
            notifyWinner(winner);
            
            return;
        }
        
        // tempPlayers is a list of everyone involved in the round
        List<Player> tempPlayers = new ArrayList<Player>();
        Map<Player, Integer> playerBets = new HashMap<Player, Integer>();
        int maxbet = 0;
        
        for (Player player : players) {
            if (!player.isOut()) {
                tempPlayers.add(player);
                
                final int bet = doHalf ? player.getBet() / 2 : player.getBet();
                
                playerBets.put(player, bet);
                maxbet = Math.max(bet, maxbet);
            }
        }
        
        while (maxbet > 0 && tempPlayers.size() > 0) {
            int minbet = maxbet;
            
            for (Player player : tempPlayers) {
                if (playerBets.get(player) < minbet) {
                    minbet = playerBets.get(player);
                }
            }
            
            int potsize = minbet * tempPlayers.size();
            
            List<Player> minPlayers = new ArrayList<Player>();
            
            for (Player player : tempPlayers) {
                if (playerBets.get(player) == minbet) {
                    minPlayers.add(player);
                }
                playerBets.put(player, playerBets.get(player) - minbet);
            }
            
            List<Player> possibleWinners = new ArrayList<Player>();
            
            for (Player player : tempPlayers) {
                if (!player.hasFolded() && !player.isOut()) {
                    possibleWinners.add(player);
                }
            }
            
            List<Player> theseWinners = getWinners(possibleWinners);
            
            potsize = potsize / theseWinners.size();
            
            if (potsize != 0) {
                
                for (Player player : theseWinners) {
                    player.addCash(potsize);
                    
                    notifyWinner(player);
                }
                
            }
            
            tempPlayers.removeAll(minPlayers);
            
            maxbet = 0;
            for (Integer bet : playerBets.values()) {
                if (bet > maxbet) {
                    maxbet = bet;
                }
            }
        }
        
    }
    
    /** {@inheritDoc} */
    public Deck getBestDeck(final Deck cards) {
        final Deck res = new Deck();
        res.addAll(cards);
        res.addAll(getCommunityCards());
        
        return res;
    }
    
    /** {@inheritDoc} */
    public boolean hasActiveHuman() {
        for (Player player : players) {
            if (player.isLocalHuman() && !player.isOut() && !player.hasFolded()) {
                return true;
            }
        }
        
        return false;
    }
    
    /** {@inheritDoc} */
    public Hand getHand(final Deck deck) {
        return new StandardHand(deck);
    }
    
    /** {@inheritDoc} */
    public String getHandText(final Player player) {
        String[] parts = getHand(player.getBestDeck()).getFriendlyName().split(": ");
        
        String res = "";
        for (String part : parts) {
            res = res + "\n" + part;
        }
        
        return res.substring(1);
    }
    
    protected List<Player> getWinners(final List<Player> winners) {
        final List<Player> res = new ArrayList<Player>();
        
        Collections.sort(winners);
        
        res.add(winners.get(0));
        
        for (int i = 1; i < winners.size(); i++) {
            if (winners.get(i).compareTo(winners.get(0)) == 0) {
                res.add(winners.get(i));
            }
        }
        
        return res;
    }
    
    protected abstract boolean canDoBringIns();
    
    /** {@inheritDoc} */
    public void registerObserver(final GameObserver observer) {
        observers.add(observer);
    }
    
    /** {@inheritDoc} */
    public void unregisterObserver(final GameObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Notifies all observers that the community cards have been upated.
     */
    protected void notifyCommunityCardsUpdated() {
        for (GameObserver observer : observers) {
            observer.communityCardsUpdated();
        }
    }
    
    /**
     * Notifies all observers that the player cards have been updated.
     */
    protected void notifyPlayerCardsUpdated() {
        for (GameObserver observer : observers)  {
            observer.playerCardsUpdated();
        }
    }
    
    protected void notifyCardDealt(final Player player, final Card card) {
        for (GameObserver observer : observers) {
            observer.cardDealt(player, card);
        }
    }
    
    /**
     * Notifies all observers that it is the specified player's turn.
     *
     * @param player The player whose turn it is
     */
    protected void notifyPlayersTurn(final Player player) {
        for (GameObserver observer : observers)  {
            observer.playersTurn(player);
        }
    }
    
    /**
     * Notifies all observers that a new player has joined.
     *
     * @param player The player who has joined
     */
    protected void notifyNewPlayer(final Player player) {
        for (GameObserver observer : observers)  {
            observer.newPlayer(player);
        }
    }
    
    /**
     * Notifies all observers that a new game has started.
     */
    protected void notifyNewGame() {
        for (GameObserver observer : observers)  {
            observer.newGame();
        }
    }
    
    /**
     * Notifies all observers that a game has ended.
     */
    protected void notifyEndGame() {
        for (GameObserver observer : observers)  {
            observer.endGame();
        }
    }
    
    /**
     * Notifies all observers that the specified player is now the dealer.
     *
     * @param player The player who is now the dealer
     */
    protected void notifySetDealer(final Player player) {
        for (GameObserver observer : observers)  {
            observer.setDealer(player);
        }
    }
    
    /**
     * Notifies all observers that the specified player has payed the specified
     * blind.
     *
     * @param player The player who's paying the blind
     * @param blind The value of the blind
     * @param name The name of the blind (big blind, small blind, ante, etc)
     */
    protected void notifyPlaceBlind(final Player player, final int blind,
            final String name) {
        for (GameObserver observer : observers)  {
            observer.placeBlind(player, blind, name);
        }
    }
    
    /**
     * Notifies all observers that the specified player has raised.
     *
     * @param player The player that has raised
     * @param amount The amount the player raised by
     */
    protected void notifyRaise(final Player player, final int amount) {
        for (GameObserver observer : observers)  {
            observer.raise(player, amount);
        }
    }
    
    /**
     * Notifies all observers that the specified player has folded.
     *
     * @param player The player who has folded
     */
    protected void notifyFold(final Player player) {
        for (GameObserver observer : observers)  {
            observer.fold(player);
        }
    }
    
    /**
     * Notifies all observers that the specified player has called.
     *
     * @param player The player who has called
     */
    protected void notifyCall(final Player player) {
        for (GameObserver observer : observers)  {
            observer.call(player);
        }
    }
    
    /**
     * Notifies all observers that the specified player has checked.
     *
     * @param player The player who has checked
     */
    protected void notifyCheck(final Player player) {
        for (GameObserver observer : observers)  {
            observer.check(player);
        }
    }
    
    /**
     * Notifies all observers that the specified player has opened.
     *
     * @param player The player who has opened
     * @param amount The amount they opened at
     */
    protected void notifyOpen(final Player player, final int amount) {
        for (GameObserver observer : observers)  {
            observer.open(player, amount);
        }
    }
    
    protected void notifyDiscards(final Player player, final int amount) {
        for (GameObserver observer : observers) {
            observer.discards(player, amount);
        }
    }
    
    /**
     * Notifies all observers that the specified player is a winner.
     *
     * @param player The player who has won
     */
    protected void notifyWinner(final Player player) {
        for (GameObserver observer : observers)  {
            observer.winner(player);
        }
    }
    
    /**
     * Notifies all observers that the showdown is taking place.
     */
    protected void notifyShowdown() {
        for (GameObserver observer : observers)  {
            observer.showdown();
        }
    }
    
}
