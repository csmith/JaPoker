/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.games;

import com.md87.cardgame.games.SevenCardStud;
import com.md87.cardgame.interfaces.Game;

/**
 *
 * @author Chris
 */
public class SevenCardStudInfo extends GameInfo {

    public String getName() {
        return "7-Card Stud";
    }

    public GameType getGameType() {
        return GameInfo.GameType.STUD;
    }

    public int getNumPlayers() {
        return 8;
    }

    public boolean usesBringIns() {
        return true;
    }

    public Game getGame(int numplayers, int bigblind, int ante, int raises) {
        return new SevenCardStud(numplayers, bigblind, ante, raises);
    }

}
