/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.games;

import com.md87.cardgame.games.AsianFiveCardStud;
import com.md87.cardgame.interfaces.Game;

/**
 *
 * @author Chris
 */
public class AsianFiveCardStudInfo extends GameInfo {

    public String getName() {
        return "Asian 5-Card Stud";
    }

    public GameType getGameType() {
        return GameInfo.GameType.STUD;
    }

    public int getNumPlayers() {
        return 6;
    }

    public boolean usesBringIns() {
        return true;
    }

    public Game getGame(int numplayers, int bigblind, int ante, int raises) {
        return new AsianFiveCardStud(numplayers, bigblind, ante, raises);
    }

}
