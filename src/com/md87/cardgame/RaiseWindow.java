/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 * 
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame;

import com.md87.cardgame.controllers.HumanPlayer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Chris
 */
public class RaiseWindow extends JDialog implements ActionListener, KeyListener {
    
    private static final long serialVersionUID = 1;
    
    private final HumanPlayer player;
    
    private JSpinner raise;
    
    public RaiseWindow(final HumanPlayer player, final int minimum) {
        super();
        
        this.player = player;
        
        setTitle("Enter amount to raise");
        
        setLayout(new BorderLayout(5, 5));
        
        JLabel label = new JLabel("Minimum: " + minimum + "; Maximum: " + player.getPlayer().getCash());
        label.setPreferredSize(new Dimension(300, 20));
        label.setBorder(new EmptyBorder(5, 5, 5, 5));
        add(label, BorderLayout.NORTH);
        
        raise = new JSpinner(new SpinnerNumberModel(minimum, minimum, player.getPlayer().getCash(), 10));
        raise.setPreferredSize(new Dimension(300, 20));
        raise.addKeyListener(this);
        add(raise);
        
        JButton button = new JButton("Raise");
        button.addActionListener(this);
        button.setPreferredSize(new Dimension(300, 25));
        button.setDefaultCapable(true);
        add(button, BorderLayout.SOUTH);
        
        getRootPane().setDefaultButton(button);
        
        pack();
        
        setLocationRelativeTo(player.getWindow());
        
        setVisible(true);
        setResizable(false);
        
        addKeyListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        synchronized(player) {
            player.bet = (Integer) raise.getValue();
            player.notify();
        }
        
        this.dispose();
    }
    
    public void keyTyped(KeyEvent e) {
        // Do nothing
    }
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            synchronized(player) {
                player.bet = (Integer) raise.getValue();
                player.notify();
            }
            
            this.dispose();
        }
    }
    
    public void keyReleased(KeyEvent e) {
        // Do nothing
    }
    
}
