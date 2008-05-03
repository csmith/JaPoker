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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

/**
 * Allows the user to customise options before starting a game.
 *
 * @author Chris
 */
public class ConfigScreen extends JDialog {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 1;

    /** The URL to prefix icons with. */
    private static final String ICON_URL = "/com/md87/cardgame/res/icons/";

    /** The configuration directory. */
    private static String configdir;

    /** The panel that allows the user to customise the players. */
    private final PlayerPanel playerPanel = new PlayerPanel();
    /** The panel that allows the user to select the game type. */
    private final GamePanel gamePanel = new GamePanel(this);
    /** The panel that allows the user to alter betting options. */
    private final BettingPanel bettingPanel = new BettingPanel();
    /** The panel that allows the user to alter apperance options. */
    private final AppearancePanel stylePanel = new AppearancePanel();
    /** The panel that allows the user to save/load settings. */
    private final JPanel savePanel = new JPanel();

    /** The type of game that is currently selected. */
    private GameInfo game;

    /**
     * Creates and displays a new config screen.
     */
    public ConfigScreen() {
        super();

        Image gameIcon = null;
        Image playerIcon = null;
        Image moneyIcon = null;
        Image saveIcon = null;
        Image styleIcon = null;

        try {
            gameIcon = ImageIO.read(getClass().getResource(ICON_URL + "icon.png"));
            playerIcon = ImageIO.read(getClass().getResource(ICON_URL + "user.png"));
            moneyIcon = ImageIO.read(getClass().getResource(ICON_URL + "money.png"));
            saveIcon = ImageIO.read(getClass().getResource(ICON_URL + "save.png"));
            styleIcon = ImageIO.read(getClass().getResource(ICON_URL + "style.png"));
        } catch (IOException ex) {
            System.err.println("Unable to load icons");
        }

        setIconImage(gameIcon);

        setTitle("JaPoker: Game configuration");

        final JTabbedPane tabbedPane = new JTabbedPane();
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

        final File configDir = new File(getConfigDir());
        if (!configDir.isDirectory()) {
            configDir.mkdirs();
        } else if (new File(configDir, "last_used.config").exists()) {
            loadSettings("last_used");
        }

        pack();
        setVisible(true);
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Sets the game type that is currently selected.
     *
     * @param newGame The type of game that is now selected
     */
    public void setGame(final GameInfo newGame) {
        game = newGame;

        playerPanel.setMaxPlayers(game.getNumPlayers());
    }

    /**
     * Retrieves the current game type.
     *
     * @return The type of game that is currently selected
     */
    public GameInfo getGame() {
        return game;
    }

    /**
     * Starts a game with the given settings.
     */
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
        
        saveSettings("last_used");
        dispose();
    }

    /**
     * Loads and applies the settings from the specified file.
     *
     * @param name The name of the file
     */
    @SuppressWarnings("unchecked")
    public void loadSettings(final String name) {
        try {
            final ObjectInputStream ois
                    = new ObjectInputStream(new FileInputStream(
                    new File(getConfigDir(), name + ".config")));

            ois.readObject();
            
            final GameInfo gameInfo = (GameInfo) ois.readObject();
            final List<Object[]> playerData = (List<Object[]>) ois.readObject();
            final Object[] bettingData = (Object[]) ois.readObject();
            final Object[] styleData = (Object[]) ois.readObject();
            
            gamePanel.setGame(gameInfo);
            playerPanel.setData(playerData);
            bettingPanel.setData(bettingData);
            stylePanel.setData(styleData);
        } catch (IOException ex) {
            System.err.println("Unable to load settings from " + name);
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.err.println("Unable to load settings from " + name);
            ex.printStackTrace();
        } catch (ClassCastException ex) {
            System.err.println("Unable to load settings from " + name);
            ex.printStackTrace();
        }
    }

    /**
     * Saves the current settings to the specified file.
     * 
     * @param name The name of the file
     */
    public void saveSettings(final String name) {
        try {
            final ObjectOutputStream oos
                    = new ObjectOutputStream(new FileOutputStream(
                    new File(getConfigDir(), name + ".config")));
            oos.writeObject(new ConfigVersioner());
            oos.writeObject(game);
            oos.writeObject(playerPanel.getData());
            oos.writeObject(bettingPanel.getData());
            oos.writeObject(stylePanel.getData());
        } catch (IOException ex) {
            System.err.println("Unable to save settings to " + name);
            ex.printStackTrace();
        }
    }

    /**
     * Returns the application's config directory.
     *
     * @return configuration directory
     */
    public static String getConfigDir() {
        if (configdir == null) {
            final String fs = System.getProperty("file.separator");
            final String osName = System.getProperty("os.name");
            if (osName.startsWith("Mac OS")) {
                configdir = System.getProperty("user.home") + fs + "Library"
                        + fs + "Preferences" + fs + "japoker" + fs;
            } else if (osName.startsWith("Windows")) {
                if (System.getenv("APPDATA") == null) {
                    configdir = System.getProperty("user.home") + fs + "japoker" + fs;
                } else {
                    configdir = System.getenv("APPDATA") + fs + "japoker" + fs;
                }
            } else {
                configdir = System.getProperty("user.home") + fs + ".japoker" + fs;
            }
        }

        return configdir;
    }
    
}

/**
 * A class used for versioning config files.
 */
class ConfigVersioner implements Serializable {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 2;

}
