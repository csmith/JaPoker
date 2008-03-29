/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config;

import com.md87.cardgame.config.games.GameInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Chris
 */
public class GamePanel extends JPanel implements ActionListener {
    
    private static final long serialVersionUID = 1;

    private final Map<GameInfo, JRadioButton> games = new HashMap<GameInfo, JRadioButton>();
    
    private final ConfigScreen owner;
    
    public GamePanel(final ConfigScreen owner) {
        this.owner = owner;
        
        setLayout(new MigLayout("flowy, fill"));
        
        GameInfo.GameType type = GameInfo.GameType.HOLDEM;
        
        for (GameInfo game : GameInfo.getGames()) {
            JRadioButton button = buildButton(game);
            
            if (type != game.getGameType()) {
                type = game.getGameType();
                
                add(new JSeparator(), "growx");
            }
            
            add(button);
        }
    }
    
    private JRadioButton buildButton(final GameInfo game) {
        final JRadioButton res = new JRadioButton(game.getName());

        if (games.size() == 0) {
            res.setSelected(true);
            owner.setGame(game);
        }
        
        res.addActionListener(this);
        games.put(game, res);
        
        return res;
    }
    
    public void actionPerformed(ActionEvent e) {
        for (Map.Entry<GameInfo, JRadioButton> entry : games.entrySet()) {
            if (entry.getValue().equals(e.getSource())) {
                owner.setGame(entry.getKey());
                entry.getValue().setSelected(true);
            } else {
                entry.getValue().setSelected(false);
            }
        }
    }
    
}
