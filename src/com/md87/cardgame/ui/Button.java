/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.ui;

import com.md87.cardgame.controllers.HumanPlayer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author chris
 */
public class Button {
    
    private final Color activeColour;
    private final Color inactiveColour;
    
    public static enum TYPE {
        SLOW {
            public boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer) {
                return speed == GameWindow.SPEED_SLOW;
            }
        },
        
        NORMAL {
            public boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer) {
                return speed == GameWindow.SPEED_NORMAL;
            }
        },
        
        FAST {
            public boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer) {
                return speed == GameWindow.SPEED_FAST;
            }
        },
        
        CONTINUE {
            public boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer) {
                return waitPlayer != null;
            }
        },
        
        CHECK {
            public boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer) {
                return player != null;
            }
        },
        
        OPEN {
            public boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer) {
                return player != null && canRaise;
            }
        },
        
        FOLD {
            public boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer) {
                return player != null && canFold;
            }
        },
        
        DISCARD {
            public boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer) {
                return discardPlayer != null;
            }            
        };
        
        public abstract boolean test(int speed, HumanPlayer waitPlayer,
                HumanPlayer player, boolean canRaise, boolean canFold,
                HumanPlayer discardPlayer);
    }
    
    private String text;
    private Rectangle bounds;
    
    public Button(final String text, final Rectangle bounds, final Color base) {
        this.text = text;
        this.bounds = bounds;
        this.inactiveColour = base.brighter();
        this.activeColour = this.inactiveColour.brighter();
    }
    
    public String getText() {
        return text;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public boolean contains(final Point p) {
        return bounds.contains(p);
    }
    
    public void render(final Graphics2D g, final boolean enabled) {
        g.setColor(enabled ? activeColour : inactiveColour);
        g.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 5, 5);

        renderTextCentered(g);
    }
    
    /**
     * Renders the specified text horizontally centered within the bounds.
     * 
     * @param g The graphics object to render to
     */
    protected void renderTextCentered(final Graphics2D g) {
        
        final Rectangle2D actualBounds = g.getFontMetrics().getStringBounds(text, g);
        
        final int yOffset = bounds.y + (int) actualBounds.getHeight()
                +(bounds.height - (int) actualBounds.getHeight()) / 2;
        final int xOffset = bounds.x + (bounds.width - (int) actualBounds.getWidth()) / 2;
        
        g.drawString(text, xOffset, yOffset - 2);
    }    

}
