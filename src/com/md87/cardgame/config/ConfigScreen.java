/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config;

import com.md87.cardgame.ui.GameWindow;
import com.md87.cardgame.Player;
import com.md87.cardgame.config.games.GameInfo;
import com.md87.cardgame.interfaces.Game;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/**
 *
 * @author Chris
 */
public class ConfigScreen extends JDialog {
    
    private static final long serialVersionUID = 1;
    
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final PlayerPanel playerPanel = new PlayerPanel();
    private final GamePanel gamePanel = new GamePanel(this);
    private final BettingPanel bettingPanel = new BettingPanel();
    private final AppearancePanel stylePanel = new AppearancePanel();
    private final JPanel savePanel = new JPanel();
    
    private GameInfo game;

    public ConfigScreen() {
        super();
        
        Image gameIcon = null;
        Image playerIcon = null;
        Image moneyIcon = null;
        Image saveIcon = null;
        Image styleIcon = null;
        
        try {
            gameIcon = ImageIO.read(getClass().getResource("/com/md87/cardgame/res/icons/icon.png"));
            playerIcon = ImageIO.read(getClass().getResource("/com/md87/cardgame/res/icons/user.png"));
            moneyIcon = ImageIO.read(getClass().getResource("/com/md87/cardgame/res/icons/money.png"));
            saveIcon = ImageIO.read(getClass().getResource("/com/md87/cardgame/res/icons/save.png"));
            styleIcon = ImageIO.read(getClass().getResource("/com/md87/cardgame/res/icons/style.png"));
        } catch (IOException ex) {
            System.err.println("Unable to load icons");
        }   
        
        setIconImage(gameIcon);
        
        setTitle("JaPoker: Game configuration");
        
        tabbedPane.addTab("Game", new ImageIcon(gameIcon), gamePanel);
        tabbedPane.addTab("Players", new ImageIcon(playerIcon), playerPanel);
        tabbedPane.addTab("Betting", new ImageIcon(moneyIcon), bettingPanel);
        tabbedPane.addTab("Appearance", new ImageIcon(styleIcon), stylePanel);
        tabbedPane.addTab("Save/Load", new ImageIcon(saveIcon), savePanel);
        tabbedPane.setEnabledAt(4, false);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.NORTH);
        
        final JButton netButton = new JButton("Join Network Game");
        netButton.setEnabled(false);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        buttonPanel.add(netButton);
        add(buttonPanel, BorderLayout.WEST);
        
        final JButton startButton = new JButton("Start Game");
        buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        buttonPanel.add(startButton);        
        add(buttonPanel, BorderLayout.EAST);
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                startGame();
            }
        });
        
        pack();
        setVisible(true);
        setResizable(false);
        
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    public void setGame(final GameInfo newGame) {
        game = newGame;
        
        playerPanel.setMaxPlayers(game.getNumPlayers());
    }
    
    public GameInfo getGame() {
        return game;
    }
    
    private void startGame() {
        final Game myGame = game.getGame(playerPanel.getNumPlayers(), 
                bettingPanel.getBigBlind(), bettingPanel.getAnte(), 
                bettingPanel.getRaises());
        final GameWindow window = new GameWindow(myGame,
                stylePanel.getFrontName(), stylePanel.getBackName(),
                stylePanel.getColour());
        
        for (Player player : playerPanel.getPlayers(myGame, window)) {
            myGame.addPlayer(player);
        }
        
        myGame.startTournament();
        dispose();
    }

}
