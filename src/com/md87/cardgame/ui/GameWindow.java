/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.ui;

import com.md87.cardgame.Card;
import com.md87.cardgame.Deck;
import com.md87.cardgame.Player;
import com.md87.cardgame.interfaces.GameObserver;
import com.md87.cardgame.interfaces.Game;
import com.md87.cardgame.controllers.HumanPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author Chris
 */
public class GameWindow extends JFrame implements GameObserver, MouseListener, KeyListener {

    /**
     * A version number for this class. It should be changed whenever the class
     * structure is changed (or anything else that would prevent serialized
     * objects being unserialized with the new class).
     */
    private static final long serialVersionUID = 1;

    /** The delay between events for the "fast" speed. */
    public final static int SPEED_FAST = 200;
    /** The delay between events for the "normal" speed. */
    public final static int SPEED_NORMAL = 1000;
    /** The delay between events for the "slow" speed. */
    public final static int SPEED_SLOW = 5000;

    /** The initial width of the window. */
    private final static int WIDTH = 800;
    /** The initial height of the window. */
    private final static int HEIGHT = 600;

    /** The number of buttons shown on each side. */
    private final static int NUM_BUTTONS = 4;

    /** The width of buttons. */
    private final static int BUTTON_WIDTH = 100;
    /** The height of buttons. */
    private final static int BUTTON_HEIGHT = 25;
    /** The offset from the screen edge of the buttons. */
    private final static int BUTTON_OFFSET = 15;
    /** The space between each button. */
    private final static int BUTTON_SPACER = 10;

    /** The width of the cards in use. */
    private int cardWidth = 71;
    /** The height of the cards in use. */
    private int cardHeight = 96;
    /** The space between each card. */
    private final static int CARD_SPACER = 4;

    /** The horizontal offset of the community ccards. */
    private int communityOffset = 150;
    /** The offset of each community card from the others if there's enough space. */
    private int cardLargeOffset = cardWidth + CARD_SPACER;
    /** The offset of each community card from the others if there's not enough space. */
    private int cardShortOffset = 25;
    /** The amount of space required to use the large offset. */
    private int communityMinSize = 2 * communityOffset + 6 * cardWidth + 5 * CARD_SPACER;

    /** The background colour to use. */
    private Color backgroundColour = new Color(0, 100, 0);

    /** The current speed. */
    private int speed = SPEED_NORMAL;

    /** The game we're playing. */
    private final Game game;

    /** The player whose turn it is. */
    private Player turn;
    /** The player who has an outstanding message. */
    private Player messagePlayer;

    /** A list of known winners. */
    private final List<Player> winners = new ArrayList<Player>();
    /** A position where the next card should be dealt to for each player. */
    private final Map<Player, Point> nextCardPos = new HashMap<Player, Point>();

    /** The current player message. */
    private String message;

    /** The human player who we're waiting for to chose his move. */
    private HumanPlayer player = null;
    /** The human player who we're waiting for to continue. */
    private HumanPlayer waitPlayer = null;
    /** The human player who we're waiting for to discard. */
    private HumanPlayer discardPlayer = null;

    /** The minimum number of cards that need discarding. */
    private int discardMin = -1;
    /** The maximum number of cards that need discarding. */
    private int discardMax = -1;
    /** The cards that have been chosen to discard. */
    private Deck discards = null;

    /** Whether the current player can fold. */
    private boolean canFold = false;
    /** Whether the current player can raise. */
    private boolean canRaise = true;
    /** Whether or not we're in showdown. */
    private boolean inShowdown = false;

    /** The image buffer we render to. */
    private BufferedImage buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

    /** A cache of card images. */
    private final Map<String, BufferedImage> cards = new HashMap<String, BufferedImage>();
    /** A cache of card positions (for discarding). */
    private final Map<Rectangle, Card> cardPositions = new HashMap<Rectangle, Card>();

    /** The buttons we're using. */
    private final Map<Button.TYPE, Button> buttons = new HashMap<Button.TYPE, Button>();

    /** A semaphore used to control rendering access. */
    private final Semaphore drawSem = new Semaphore(1);
    
    /** The style of the card to use. */
    private final String frontStyle, backStyle;

    /**
     * Creates a new game window for the specified game.
     *
     * @param game The game that this window should display
     * @param frontStyle The folder name to use for the front of card images
     * @param backStyle The file name to use for the back of card images
     * @param backgroundColour The background colour to use
     */
    public GameWindow(final Game game, final String frontStyle, 
            final String backStyle, final Color backgroundColour) {
        super("JaPoker");

        this.game = game;
        this.frontStyle = frontStyle;
        this.backStyle = backStyle;
        this.backgroundColour = backgroundColour;
        
        // Determine card size
        try {
            final String cardFile = "/com/md87/cardgame/res/fronts/" + frontStyle + "/sace.png";
            final BufferedImage cardImage = ImageIO.read(getClass().getResource(cardFile));
            
            cardWidth = cardImage.getWidth();
            cardHeight = cardImage.getHeight();
            cardLargeOffset = cardWidth + CARD_SPACER;
            communityMinSize = 2 * communityOffset + 6 * cardWidth + 5 * CARD_SPACER;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        game.registerObserver(this);

        setSize(WIDTH, HEIGHT);

        initButtons();

        addMouseListener(this);
        addKeyListener(this);

        setVisible(true);

        try {
            setIconImage(ImageIO.read(getClass()
                    .getResource("/com/md87/cardgame/res/icons/icon.png")));
        } catch (IOException ex) {
            System.err.println("Unable to load icon");
        }

        setDefaultCloseOperation(GameWindow.EXIT_ON_CLOSE);
    }

    /**
     * Initialises the buttons used in the UI.
     */
    private void initButtons() {
        int topButton = (getHeight() - (BUTTON_HEIGHT * NUM_BUTTONS
                + BUTTON_SPACER * (NUM_BUTTONS - 1))) / 2;
        final int buttonOffset = BUTTON_SPACER + BUTTON_HEIGHT;
        final int rightButton = getWidth() - BUTTON_WIDTH - BUTTON_OFFSET;

        buttons.put(Button.TYPE.FAST, new Button("Fast",
                new Rectangle(BUTTON_OFFSET, topButton + 0 * buttonOffset, BUTTON_WIDTH,
                BUTTON_HEIGHT)));
        buttons.put(Button.TYPE.NORMAL, new Button("Medium",
                new Rectangle(BUTTON_OFFSET, topButton + 1 * buttonOffset, BUTTON_WIDTH,
                BUTTON_HEIGHT)));
        buttons.put(Button.TYPE.SLOW, new Button("Slow",
                new Rectangle(BUTTON_OFFSET, topButton + 2 * buttonOffset, BUTTON_WIDTH,
                BUTTON_HEIGHT)));
        buttons.put(Button.TYPE.CONTINUE, new Button("Continue",
                new Rectangle(BUTTON_OFFSET, topButton + 3 * buttonOffset, BUTTON_WIDTH,
                BUTTON_HEIGHT)));

        buttons.put(Button.TYPE.CHECK, new Button("Check / Call",
                new Rectangle(rightButton, topButton + 0 * buttonOffset, BUTTON_WIDTH,
                BUTTON_HEIGHT)));
        buttons.put(Button.TYPE.OPEN, new Button("Open / Raise",
                new Rectangle(rightButton, topButton + 1 * buttonOffset, BUTTON_WIDTH,
                BUTTON_HEIGHT)));
        buttons.put(Button.TYPE.FOLD, new Button("Fold",
                new Rectangle(rightButton, topButton + 2 * buttonOffset, BUTTON_WIDTH,
                BUTTON_HEIGHT)));
        buttons.put(Button.TYPE.DISCARD, new Button("Discard",
                new Rectangle(rightButton, topButton + 3 * buttonOffset, BUTTON_WIDTH,
                BUTTON_HEIGHT)));
    }

    /**
     * Renders the game window.
     *
     * @param gr The graphics object to render to
     */
    @Override
    public void paint(final Graphics gr) {
        drawSem.acquireUninterruptibly();

        // Resize the buffer if the window has been resized
        if (buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight()) {
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        cardPositions.clear();

        Graphics g = buffer.getGraphics();

        g.setColor(backgroundColour);
        g.fillRect(0, 0, getWidth(), getHeight());

        paintButtons((Graphics2D) g);
        paintCommunityCards(g);

        int yCardPosition = 55;
        int yNamePosition = 74 + cardHeight;
        int yHandPosition = 38;
        int x = 28;

        int xDirection = 1;

        boolean largeOffset = (game.getPlayers().size() / 2) * (10 + cardLargeOffset
            * game.holeCardCount()) < getWidth() - 28 * 2;

        int playerNumber = 0;
        for (Player myPlayer : game.getPlayers()) {
            playerNumber++;

            // Flip the direction of players for the lower half of the screen
            if (playerNumber > game.getNumPlayers() / 2 && yCardPosition == 55) {
                yCardPosition = getHeight() - 45 - cardHeight;
                yNamePosition = getHeight() - 56 - cardHeight;
                yHandPosition = getHeight() - 30;

                if (largeOffset) {
                    x = getWidth() - 28 - cardLargeOffset * game.holeCardCount();
                } else {
                    x = getWidth() - 28 - cardLargeOffset - cardShortOffset
                            * (game.holeCardCount() - 1);
                }

                xDirection = -1;
            }

            g.setColor(myPlayer.isOut() ? Color.BLACK : Color.WHITE);
            g.drawString(myPlayer.getName(), x, yNamePosition);

            if (messagePlayer == myPlayer) {
                paintMessage(g, x, yNamePosition + 15 * xDirection);
            }

            if (myPlayer == turn && !inShowdown && !myPlayer.isOut()) {
                paintTurnToken(g, x, yNamePosition);
            }

            if (myPlayer == game.getDealer()) {
                paintDealerToken(g, x, yNamePosition < getHeight() / 2 ?
                    yNamePosition + 20 : yNamePosition - 55);
            }

            if (winners.contains(myPlayer)) {
                paintWinnerToken(g, x, yNamePosition < 400 ?
                    yNamePosition + 25 : yNamePosition - 45);
            }

            if (!myPlayer.hasFolded()) {
                int xOffset = 0;
                int cardNumber = 0;

                for (Card card : myPlayer.getCards()) {
                    cardNumber++;
                    if ((myPlayer.shouldShowCards() || inShowdown
                            || card.isPublic() || !game.hasActiveHuman())
                            && (discardPlayer == null
                            || discardPlayer.getPlayer() != myPlayer
                            || !discards.contains(card))) {
                        showCard(g, card, x + xOffset, yCardPosition);
                    } else {
                        showCard(g, null, x + xOffset, yCardPosition);
                    }

                    cardPositions.put(new Rectangle(x + xOffset, yCardPosition,
                            ((largeOffset || cardNumber == myPlayer.getCards().size()) ?
                                cardLargeOffset : cardShortOffset), cardHeight), card);

                    if (largeOffset) {
                        xOffset += cardLargeOffset;
                    } else {
                        xOffset += cardShortOffset;
                    }
                }

                nextCardPos.put(myPlayer, new Point(x + xOffset, yCardPosition));

                if (inShowdown && !myPlayer.hasFolded() && myPlayer.getCards().size() > 0) {
                    g.setColor(Color.GREEN);
                    String[] hand = game.getHandText(myPlayer).split("\n");
                    int yo = 0;
                    for (String line : hand) {
                        g.drawString(line, x, yHandPosition + yo);
                        yo += 13;
                    }
                }
            }

            x += xDirection * (getWidth() * 2 - 28*4)
                    / (game.getNumPlayers() + (game.getNumPlayers() % 2));
        }

        g.setColor(Color.WHITE);
        g.drawString("POT: " + game.getCurrentPot(), getWidth() * 3 / 4, getHeight() / 2);
        g.drawString("BET: " + game.getMaxBet(), getWidth() * 3 / 4, getHeight() / 2 + 20);

        ((Graphics2D) gr).drawImage(buffer, 0, 0, this);

        drawSem.release();
    }

    /**
     * Renders the buttons used by the UI.
     *
     * @param g The graphics object to render the buttons to.
     */
    private void paintButtons(final Graphics2D g) {
        initButtons();

        for (Map.Entry<Button.TYPE, Button> entry : buttons.entrySet()) {
            entry.getValue().render(g,
                    entry.getKey().test(speed, waitPlayer, player, canRaise,
                    canFold, discardPlayer));
        }
    }

    /**
     * Paints the visible community cards.
     * 
     * @param g The graphics object to render the buttons to.
     */
    private void paintCommunityCards(final Graphics g) {
        int i = 0;
        int xOffset = getWidth() < communityMinSize ? cardShortOffset : cardLargeOffset;

        for (Card card : game.getCommunityCards()) {
            showCard(g, card, communityOffset + i * xOffset, (getHeight() - cardHeight)/2);
            i++;
        }
    }

    /**
     * Paints the dealer token at the specified position.
     * 
     * @param g The graphics object to render the token to.
     * @param x The x co-ordinate of the token
     * @param y The y co-ordinate of the token
     */
    private void paintDealerToken(final Graphics g, final int x, final int y) {
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, 25, 25);
        g.setColor(Color.BLACK);
        g.drawString("D", x + 9, y + 17);
    }

    /**
     * Paints the winner token at the specified position.
     * 
     * @param g The graphics object to render the token to.
     * @param x The x co-ordinate of the token
     * @param y The y co-ordinate of the token
     */
    private void paintWinnerToken(final Graphics g, final int x, final int y) {
        g.setColor(Color.GREEN);
        g.fillOval(x + 37, y - 3, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("W", x + 42, y + 12);
    }

    /**
     * Paints the turn token at the specified position.
     * 
     * @param g The graphics object to render the token to.
     * @param x The x co-ordinate of the token
     * @param y The y co-ordinate of the token
     */
    private void paintTurnToken(final Graphics g, final int x, final int y) {
        g.setColor(Color.WHITE);
        g.fillOval(x - 12, y - 10, 10, 10);
    }

    /**
     * Paints the current message at the specified position.
     * 
     * @param g The graphics object to render the message to.
     * @param x The x co-ordinate of the message
     * @param y The y co-ordinate of the message
     */
    private void paintMessage(final Graphics g, final int x, final int y) {
        g.setColor(Color.YELLOW);
        g.drawString(message, x, y);
    }

    /**
     * Displays the specified card at the specified co-ordinates.
     *
     * @param g The graphics object to render the buttons to
     * @param card The card to be drawn, or null for a blank
     * @param x The x-coordinate of the card
     * @param y The y-coordinate of the card
     */
    private void showCard(final Graphics g, final Card card, final int x, final int y) {
        showCard(g, card, x, y, cardWidth);
    }

    /**
     * Displays the specified card at the specified co-ordinates.
     *
     * @param g The graphics object to render the buttons to
     * @param card The card to be drawn, or null for a blank
     * @param x The x-coordinate of the card
     * @param y The y-coordinate of the card
     * @param width The width that the card should be scaled to
     */
    private void showCard(final Graphics g, final Card card, final int x, final int y,
            final int width) {
        try {
            final String file = "/com/md87/cardgame/res/"
                    + (card == null ? "backs/" + backStyle : "fronts/" + frontStyle + "/"
                    + card.getFileName()) + ".png";
            if (!cards.containsKey(file)) {
                BufferedImage im = ImageIO.read(getClass().getResource(file));
                cards.put(file, im);
            }
            
            if (cards.get(file) == null) {
                System.err.println("No card image for: " + file);
            }

            ((Graphics2D) g).drawImage(cards.get(file), x + (cardWidth - width)/2, y,
                    x + width + (cardWidth - width)/2, y + cardHeight,
                    0, 0, cards.get(file).getWidth(), cards.get(file).getHeight(), this);
        } catch (IOException ex) {
            System.err.print("Error loading image: " + ex);
        }
    }

    /**
     * Requests a repaint, and sleeps for a period of time based on the speed
     * of the game.
     */
    private void doUpdate() {
        repaint();

        try {
            Thread.sleep(speed);
        } catch (InterruptedException ex) {
            // Do nothing
        }
    }

    // ----------------------- OBSERVER METHODS -------------------------------

    /** {@inheritDoc} */
    @Override
    public void playerCardsUpdated() {
        inShowdown = false;

        repaint();

        try {
            Thread.sleep(speed / 4);
        } catch (InterruptedException ex) {
            // Do nothing
        }
    }

    /** {@inheritDoc} */
    @Override
    public void communityCardsUpdated() {
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void playersTurn(final Player player) {
        if (!player.hasFolded() && !player.isOut() && !player.isAllIn()) {
            turn = player;
            messagePlayer = null;

            doUpdate();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void newPlayer(final Player player) {
        repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void newGame() {
        inShowdown = false;

        winners.clear();

        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void endGame() {
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void setDealer(final Player player) {
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void placeBlind(final Player player, final int blind, final String name) {
        winners.clear();

        messagePlayer = player;
        message = "Pays " + name;
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void raise(final Player player, final int amount) {
        messagePlayer = player;
        message = "Raises " + amount;
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void fold(final Player player) {
        messagePlayer = player;
        message = "Folds";

        if (!game.hasActiveHuman()) {
            flipAllCards();
        }

        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void call(final Player player) {
        messagePlayer = player;
        message = "Calls";
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void check(final Player player) {
        messagePlayer = player;
        message = "Checks";
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void open(final Player player, final int amount) {
        messagePlayer = player;
        message = "Opens at " + amount;
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void winner(final Player player) {
        winners.add(player);
        doUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void showdown() {
        inShowdown = true;

        flipAllCards();

        doUpdate();
    }


    // ------------------ HUMAN PLAYER INTERFACE ------------------------------

    /**
     * Indicates that a human player needs to make a decision as to whether to
     * call/fold/raise.
     *
     * @param player The player who has to make the decision
     * @param canFold Whether or not the player can fold
     * @param canRaise Whether or not the player can raise
     */
    public void setHumanPlayer(final HumanPlayer player, final boolean canFold,
            final boolean canRaise) {
        this.player = player;
        this.canFold = canFold;
        this.canRaise = canRaise;

        repaint();
    }

    /**
     * Indicates that a human player controller is waiting for the player to
     * indicate that it's OK to continue.
     *
     * @param player The player who has to make the indication
     */
    public void setWaitPlayer(final HumanPlayer player) {
        this.waitPlayer = player;

        repaint();
    }

    /**
     * Indicates that a human player controller is waiting for the player to
     * discard some cards.
     * 
     * @param player The player who has to do the discarding
     * @param min The minimum number of cards to discard
     * @param max The maximum number of cards to discard
     */
    public void setDiscardPlayer(final HumanPlayer player, final int min, final int max) {
        this.discardPlayer = player;
        discardMin = min;
        discardMax = max;
        discards = new Deck();

        repaint();
    }

    // ----------------- UI CALLBACKS ------------------------------------------

    /** {@inheritDoc} */
    @Override
    public void mouseClicked(final MouseEvent e) {
        for (Map.Entry<Button.TYPE, Button> entry : buttons.entrySet()) {
            if (entry.getValue().contains(e.getPoint())) {
                processMouseClick(entry.getKey());
            }
        }

        if (discardPlayer != null) {
            Card best = null;

            for (Map.Entry<Rectangle, Card> entry : cardPositions.entrySet()) {
                if (entry.getKey().contains(e.getPoint())) {
                    best = entry.getValue();
                }
            }

            if (best != null && discardPlayer.getPlayer().getCards().contains(best)) {
                if (discards.contains(best)) {
                    discards.remove(best);
                } else {
                    discards.add(best);
                }

                repaint();
            }
        }
    }

    /**
     * Handles the user clicking on a button.
     * 
     * @param type The type of button that was clicked on
     */
    private void processMouseClick(final Button.TYPE type) {
        boolean done = false;

        switch(type) {
        case FAST:
            speed = SPEED_FAST;
            break;

        case NORMAL:
            speed = SPEED_NORMAL;
            break;

        case SLOW:
            speed = SPEED_SLOW;
            break;
        }

        if (type.test(speed, waitPlayer, player, canRaise, canFold, discardPlayer)) {
            switch(type) {
            case CONTINUE:
                synchronized(waitPlayer) {
                    waitPlayer.notifyAll();
                }

                waitPlayer = null;
                break;

            case CHECK:
                synchronized(player) {
                    player.move = 0;
                    player.notifyAll();
                    done = true;
                }

                break;

            case OPEN:
                synchronized(player) {
                    player.move = 1;
                    player.notifyAll();
                    done = true;
                }

                break;

            case FOLD:
                synchronized(player) {
                    player.move = 2;
                    player.notifyAll();
                    done = true;
                }

                break;

            case DISCARD:
                if (discards.size() >= discardMin && discards.size() <= discardMax) {
                    synchronized(discardPlayer) {
                        discardPlayer.discards = discards;
                        discardPlayer.notifyAll();
                        done = true;
                    }
                } else {
                    messagePlayer = discardPlayer.getPlayer();
                    message = "Must discard " + discardMin + "-" + discardMax + " cards";
                    repaint();
                }

            }
        }

        if (done) {
            player = null;
            discardPlayer = null;
        }

        repaint();
    }

    /** {@inheritDoc} */
    @Override
    public void mousePressed(MouseEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void mouseEntered(MouseEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void mouseExited(MouseEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent e) {
        if (player != null) {
            boolean done = false;

            synchronized(player) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_C:
                    player.move = 0;
                    player.notifyAll();
                    done = true;
                    break;
                case KeyEvent.VK_O:
                case KeyEvent.VK_R:
                    if (canRaise) {
                        player.move = 1;
                        player.notifyAll();
                        done = true;
                    }
                    break;
                case KeyEvent.VK_F:
                    if (canFold) {
                        player.move = 2;
                        player.notifyAll();
                        done = true;
                    }
                    break;
                }
            }

            if (done) {
                player = null;
            }
        }

        if (discardPlayer != null && e.getKeyCode() == KeyEvent.VK_D) {
            if (discards.size() >= discardMin && discards.size() <= discardMax) {
                synchronized(discardPlayer) {
                    discardPlayer.discards = discards;
                    discardPlayer.notifyAll();
                }
                discardPlayer = null;
            } else {
                messagePlayer = discardPlayer.getPlayer();
                message = "Must discard " + discardMin + "-" + discardMax + " cards";
                repaint();
            }
        }

        if (waitPlayer != null && (e.getKeyCode() == KeyEvent.VK_SPACE
                || e.getKeyCode() == KeyEvent.VK_ENTER)) {
            synchronized(waitPlayer) {
                waitPlayer.notifyAll();
            }

            waitPlayer = null;
        }

        if (discardPlayer != null) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1:
                    toggleDiscard(1);
                    break;
                case KeyEvent.VK_2:
                    toggleDiscard(2);
                    break;
                case KeyEvent.VK_3:
                    toggleDiscard(3);
                    break;
                case KeyEvent.VK_4:
                    toggleDiscard(4);
                    break;
                case KeyEvent.VK_5:
                    toggleDiscard(5);
                    break;
                case KeyEvent.VK_6:
                    toggleDiscard(6);
                    break;
                case KeyEvent.VK_7:
                    toggleDiscard(7);
                    break;
                case KeyEvent.VK_8:
                    toggleDiscard(8);
                    break;
                case KeyEvent.VK_9:
                    toggleDiscard(9);
                    break;
            }
        }
    }

    /**
     * Toggles the "discard" state of the specified card.
     * 
     * @param card The index of the card to be toggled
     */
    private void toggleDiscard(final int card) {
        final Deck playerCards = discardPlayer.getPlayer().getCards();

        if (playerCards.size() >= card) {
            final Card myCard = playerCards.get(card - 1);

            if (discards.contains(myCard)) {
                discards.remove(myCard);
                flipCard(false, myCard);
            } else {
                discards.add(myCard);
                flipCard(true, myCard);
            }

            repaint();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void discards(Player player, int number) {
        messagePlayer = player;
        message = "Discards " + number + " card" + (number == 1 ? "" : "s");

        doUpdate();
    }

    /**
     * Flips all cards over.
     */
    private void flipAllCards() {
        final List<Card> unflipped = new ArrayList<Card>();

        for (Player curPlayer : game.getPlayers()) {
            if (!curPlayer.isOut() && !curPlayer.hasFolded()
                    && !curPlayer.shouldShowCards()) {
                for (Card card : curPlayer.getCards()) {
                    if (!card.isPublic()) {
                        unflipped.add(card);
                    }
                }
            }
        }

        flipCard(false, unflipped.toArray(new Card[0]));
    }

    /**
     * Flips the specified cards over.
     * 
     * @param startsVisible Whether or not the card starts visible
     * @param cards The cards to be flipped
     */
    private void flipCard(final boolean startsVisible, final Card ... cards) {
        drawSem.acquireUninterruptibly();
        boolean visible = startsVisible;

        final BufferedImage newbuffer = new BufferedImage(getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        for (int progress = 0; progress < 100; progress += 5) {
            final Graphics g = newbuffer.getGraphics();
            g.drawImage(buffer, 0, 0, this);

            if (progress == 50) {
                visible = !visible;
            }


            for (Card card : cards) {
                int x = 0;
                int y = 0;
                Rectangle rect = new Rectangle();

                for (Map.Entry<Rectangle, Card> entry : cardPositions.entrySet()) {
                    if (entry.getValue().equals(card)) {
                        x = entry.getKey().x;
                        y = entry.getKey().y;
                        rect = entry.getKey();
                    }
                }

                if (x == 0 && y == 0) {
                    continue;
                }

                int size = 50 - (progress > 50 ? 100 - progress : progress);

                g.setColor(backgroundColour);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);

                showCard(g, visible ? card : null, x, y, (int) (cardWidth 
                        * ((float) size/50)));
                }

            getGraphics().drawImage(newbuffer, 0, 0, this);

            try {
                Thread.sleep(speed / 500);
            } catch (InterruptedException ex) {
                // Do nothing
            }

        }
        drawSem.release();
    }

    /** {@inheritDoc} */
    @Override
    public void cardDealt(final Player player, final Card card) {
        drawSem.acquireUninterruptibly();

        final int targetX = nextCardPos.get(player).x;
        final int targetY = nextCardPos.get(player).y;

        cardPositions.put(new Rectangle(targetX, targetY, cardWidth, cardHeight), card);

        final BufferedImage newbuffer = new BufferedImage(getWidth(), getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < targetX + cardWidth; x += Math.max(15, targetX / 15)) {
            int y = ((HEIGHT - cardHeight) / 2)
                    - Math.round(((float) x / (float) (targetX + cardWidth))
                    * ((HEIGHT - cardHeight) / 2 - targetY));
            newbuffer.getGraphics().drawImage(buffer, 0, 0, this);

            showCard(newbuffer.getGraphics(), card.isPublic() ? card : null,
                    x - cardWidth, y);

            getGraphics().drawImage(newbuffer, 0, 0, this);

            try {
                Thread.sleep(speed / 500);
            } catch (InterruptedException ex) {
                // Do nothing
            }
        }

        drawSem.release();

        if (!card.isPublic() && player.shouldShowCards()) {
            flipCard(false, card);
        }
    }

}
