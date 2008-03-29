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
import com.md87.cardgame.hands.StandardHand;
import com.md87.cardgame.Player;
import com.md87.cardgame.Player.CallRaiseFold;
import com.md87.cardgame.Player.OpenCheck;
import com.md87.cardgame.interfaces.Game;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Chris
 */
public class EndGameAI extends ConservativeOpener implements GameObserver {
    
    private boolean shouldBluff;
    private boolean shouldFold;
    private boolean endGame = false;
    private StandardHand bestHand;
    
    private Deck communityCards = null;
    
    private double myLevel = 0;
    
    private final Map<StandardHand.Ranking, Integer> handRanks = new HashMap<StandardHand.Ranking, Integer>();
    
    public EndGameAI(final Game game) {
        game.registerObserver(this);
        
        handRanks.put(StandardHand.Ranking.FLUSH, 4047644);
        handRanks.put(StandardHand.Ranking.FOUR_OF_A_KIND, 224848);
        handRanks.put(StandardHand.Ranking.FULL_HOUSE, 3473184);
        handRanks.put(StandardHand.Ranking.NO_PAIR, 23294460);
        handRanks.put(StandardHand.Ranking.ONE_PAIR, 58627800);
        handRanks.put(StandardHand.Ranking.STRAIGHT, 6180020);
        handRanks.put(StandardHand.Ranking.STRAIGHT_FLUSH, 41584);
        handRanks.put(StandardHand.Ranking.THREE_OF_A_KIND, 6461620);
        handRanks.put(StandardHand.Ranking.TWO_PAIR, 31433400);
    }
    
    @Override
    public CallRaiseFold doCallRaiseFold(int callAmount, boolean canRaise) {
        if (endGame) {
            if (canRaise && (shouldBluff || (!shouldFold && Math.random() > 0.5))) {
                return CallRaiseFold.RAISE;
            } else if (shouldFold) {
                return CallRaiseFold.FOLD;
            } else {
                return CallRaiseFold.CALL;
            }
        } else {
            return super.doCallRaiseFold(callAmount, canRaise);
        }
    }
    
    @Override
    public OpenCheck doOpenCheck() {
        return super.doOpenCheck();
    }
    
    @Override
    public int getRaise(int minimum) {
        if (endGame) {
            if (shouldBluff) {
                return minimum + (int) (Math.random() * (player.getCash() - minimum)/2);
            } else {
                return minimum + (int) (Math.random() * (player.getCash() - minimum));
            }
        } else {
            return super.getRaise(minimum);
        }
    }
    
    public void communityCardsUpdated() {
        communityCards = game.getCommunityCards();
        communityCards.addAll(player.getCards());
        
        bestHand = new StandardHand(communityCards);
        
        if (communityCards.size() == 7) {
            endGame = true;
            shouldFold = false;
            
            final int myFreq = handRanks.get(bestHand.getBestRank());
            double betterFreq = 0;
            double totalFreq = 0;
            
            for (StandardHand.Ranking ranking : StandardHand.Ranking.values()) {
                if (ranking.ordinal() < bestHand.getBestRank().ordinal()) {
                    betterFreq += handRanks.get(ranking);
                }
                
                totalFreq += handRanks.get(ranking);
            }
            
            // 0 <= score < 1
            // Lower the better
            double score = betterFreq / totalFreq;
            
            if (score > myLevel) {
                shouldFold = true;
            }
        }
    }
    
    public void playersTurn(Player player) {
        // Do nothing
    }
    
    public void newPlayer(Player player) {
        // Do nothing
    }
    
    public void newGame() {
        shouldBluff = Math.random() > 0.7;
        myLevel = Math.random();
        endGame = false;
    }
    
    public void endGame() {
        // Do nothing
    }
    
    public void setDealer(Player player) {
        // Do nothing
    }
    
    public void placeBlind(Player player, int blind, String name) {
        // Do nothing
    }
    
    public void raise(Player player, int amount) {
        // Do nothing
    }
    
    public void fold(Player player) {
        // Do nothing
    }
    
    public void call(Player player) {
        // Do nothing
    }
    
    public void check(Player player) {
        // Do nothing
    }
    
    public void open(Player player, int amount) {
        // Do nothing
    }
    
    public void winner(Player players) {
        // Do nothing
    }
    
    public void showdown() {
        // Do nothing
    }
    
    public void playerCardsUpdated() {
        // Do nothing
    }

    public void discards(Player player, int number) {
        // Do nothing
    }

    public void cardDealt(Player player, Card card) {
        // Do nothing
    }
}
