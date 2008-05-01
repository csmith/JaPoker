/*
 * Copyright (c) Chris 'MD87' Smith, 2007-2008. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config;

import com.md87.cardgame.config.cards.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Chris
 */
public class AppearancePanel extends JPanel implements ActionListener, ChangeListener {

    private static final long serialVersionUID = 1;
    
    private final PreviewPanel preview = new PreviewPanel();
    
    private final JComboBox frontCombo = new JComboBox();
    private final JComboBox backCombo = new JComboBox();
    
    private final JSpinner redSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
    private final JSpinner greenSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 255, 1));
    private final JSpinner blueSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));

    public AppearancePanel() {
        setLayout(new MigLayout("fill"));
        
        frontCombo.addItem(new ClassicFront1());
        frontCombo.addItem(new ClassicFront2());
        frontCombo.addItem(new OcaWhiteFront());
        
        backCombo.addItem(new ClassicBack1());
        backCombo.addItem(new ClassicBack2());
        backCombo.addItem(new OcaBlueHatchBack());
        backCombo.addItem(new OcaRedHatchBack());
        backCombo.addItem(new OcaBlueCardsBack());
        backCombo.addItem(new OcaRedCardsBack());
        backCombo.addItem(new OcaBlueSuits1Back());
        backCombo.addItem(new OcaBlueSuits2Back());
        backCombo.addItem(new OcaRedSuits1Back());
        backCombo.addItem(new OcaRedSuits2Back());
        
        frontCombo.addActionListener(this);
        backCombo.addActionListener(this);
        
        redSpinner.addChangeListener(this);
        greenSpinner.addChangeListener(this);
        blueSpinner.addChangeListener(this);
        
        final JPanel optionsPanel = new JPanel();
        final JPanel previewPanel = new JPanel();        
        final JPanel colourPanel = new JPanel();
        
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Card style"));
        optionsPanel.setLayout(new MigLayout("fillx"));
        optionsPanel.add(new JLabel("Front:"));
        optionsPanel.add(frontCombo, "growx");
        optionsPanel.add(new JLabel("Back:"));
        optionsPanel.add(backCombo, "growx");
        
        colourPanel.setBorder(BorderFactory.createTitledBorder("Background colour"));
        colourPanel.setLayout(new MigLayout("fillx"));
        colourPanel.add(new JLabel("Red:"));
        colourPanel.add(redSpinner, "growx");
        colourPanel.add(new JLabel("Green:"));
        colourPanel.add(greenSpinner, "growx");
        colourPanel.add(new JLabel("Blue:"));
        colourPanel.add(blueSpinner, "growx");
        
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview"));
        previewPanel.setLayout(new MigLayout());
        previewPanel.add(preview, "grow, push");
        
        add(optionsPanel, "growx, wrap");
        add(colourPanel, "growx, wrap");
        add(previewPanel, "growx, growy");
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e) {
        preview.repaint();
    }
    
    /** {@inheritDoc} */
    @Override
    public void stateChanged(final ChangeEvent e) {
        preview.repaint();
    }
    
    public String getBackName() {
        return ((CardStyle) backCombo.getSelectedItem()).getFolderName();
    }
    
    public String getFrontName() {
        return ((CardStyle) frontCombo.getSelectedItem()).getFolderName();
    }
    
    public Color getColour() {
        return new Color((Integer) redSpinner.getValue(),
                (Integer) greenSpinner.getValue(), (Integer) blueSpinner.getValue());
    }

    private class PreviewPanel extends JPanel {

        public PreviewPanel() {
            super(new MigLayout("fill"));
        }

        @Override
        public void paint(final Graphics g) {
            g.setColor(AppearancePanel.this.getColour());
            g.fillRect(0, 0, getWidth(), getHeight());
            
            final String backFile = "/com/md87/cardgame/res/backs/"
                    + ((CardStyle) backCombo.getSelectedItem()).getFolderName() + ".png";
            final String frontFile1 = "/com/md87/cardgame/res/fronts/"
                    + ((CardStyle) frontCombo.getSelectedItem()).getFolderName() + "/sace.png";
            final String frontFile2 = "/com/md87/cardgame/res/fronts/"
                    + ((CardStyle) frontCombo.getSelectedItem()).getFolderName() + "/hdeuce.png";
            final String frontFile3 = "/com/md87/cardgame/res/fronts/"
                    + ((CardStyle) frontCombo.getSelectedItem()).getFolderName() + "/djack.png";

            try {
                final BufferedImage front1 = ImageIO.read(getClass().getResource(frontFile1));
                final BufferedImage front2 = ImageIO.read(getClass().getResource(frontFile2));
                final BufferedImage front3 = ImageIO.read(getClass().getResource(frontFile3));
                final BufferedImage back1 = ImageIO.read(getClass().getResource(backFile));
                final int width = front1.getWidth();
                final int height = front1.getHeight();
                
                final int offsetX = (getWidth() - width * 4 - 30) / 2;
                final int offsetY = (getHeight() - height) / 2;

                ((Graphics2D) g).drawImage(front1, offsetX, offsetY, offsetX + width, 
                        offsetY + height, 0, 0, width, height, this);
                ((Graphics2D) g).drawImage(front2, offsetX + width + 10, offsetY, 
                        offsetX + 2 * width + 10, offsetY + height, 0, 0, width, height, this);
                ((Graphics2D) g).drawImage(front3, offsetX + 2 * (width + 10), offsetY, 
                        offsetX + 3 * width + 20, offsetY + height, 0, 0, width, height, this);
                ((Graphics2D) g).drawImage(back1, offsetX + 3 * (width + 10), offsetY, 
                        offsetX + 4 * width + 30, offsetY + height, 0, 0, back1.getWidth(),
                        back1.getHeight(), this);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        
    }

}
