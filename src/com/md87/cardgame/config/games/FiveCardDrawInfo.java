/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config.games;

import com.md87.cardgame.games.FiveCardDraw;
import com.md87.cardgame.interfaces.Game;

/**
 *
 * @author chris
 */
public class FiveCardDrawInfo extends GameInfo {

    @Override
    public String getName() {
        return "Five Card Draw";
    }

    @Override
    public GameType getGameType() {
        return GameInfo.GameType.DRAW;
    }

    @Override
    public int getNumPlayers() {
        return 10;
    }

    @Override
    public boolean usesBringIns() {
        return false;
    }

    @Override
    public Game getGame(int numplayers, int bigblind, int ante, int raises) {
        return new FiveCardDraw(numplayers, bigblind, ante, raises);
    }

}
