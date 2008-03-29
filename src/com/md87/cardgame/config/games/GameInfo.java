/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.games;

import com.md87.cardgame.interfaces.Game;

/**
 *
 * @author Chris
 */
public abstract class GameInfo {
    
    public static enum GameType  {
        STUD, DRAW, HOLDEM
    };
    
    public abstract String getName();
    
    public abstract GameType getGameType();
    
    public abstract int getNumPlayers();
    
    public abstract boolean usesBringIns();
    
    public abstract Game getGame(final int numplayers, final int bigblind, final int ante,
            final int raises);
    
    public static GameInfo[] getGames() {
        return new GameInfo[]{
            new TexasHoldEmInfo(),
            new PineappleInfo(),
            new CrazyPineappleInfo(),
            new RoyalHoldEmInfo(),
            new SuperHoldEmInfo(),
            new OmahaHoldEmInfo(),
            new OmahaHighLowInfo(),
            new CourchevelInfo(),
            
            new FiveCardDrawInfo(),

            new FiveCardStudInfo(),            
            new SevenCardStudInfo(),
            new AsianFiveCardStudInfo(),
        };
    }
    
}
