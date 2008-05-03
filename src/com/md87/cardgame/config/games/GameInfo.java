/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.games;

import com.md87.cardgame.interfaces.Game;

import java.io.Serializable;

/**
 *
 * @author Chris
 */
public abstract class GameInfo implements Serializable {
    
    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 1;
    
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

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        return getClass().equals(obj.getClass());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
}
