/*
 * Copyright (c) Chris 'MD87' Smith, 2007. All rights reserved.
 *
 * This code may not be redistributed without prior permission from the
 * aforementioned copyright holder(s).
 */

package com.md87.cardgame.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Chris
 */
public class BettingPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1;

    private final JPanel panel1 = new JPanel();
    private final JPanel panel2 = new JPanel();
    private final JPanel panel3 = new JPanel();

    private final List<JRadioButton> types = new ArrayList<JRadioButton>();

    private final JSpinner ante = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 10));
    private final JSpinner bringin = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 10));
    private final JSpinner bigblind = new JSpinner(new SpinnerNumberModel(100, 0, 100000, 10));
    private final JSpinner smallblind = new JSpinner(new SpinnerNumberModel(50, 0, 100000, 10));

    private final JSpinner raises = new JSpinner(new SpinnerNumberModel(4, -1, 100, 1));

    public BettingPanel() {
        panel1.setBorder(BorderFactory.createTitledBorder(" Limit type "));
        panel1.setLayout(new MigLayout("fillx"));

        JRadioButton limitType;

        limitType = new JRadioButton("Fixed limit", false);
        limitType.addActionListener(this);
        limitType.setEnabled(false);
        types.add(limitType);
        panel1.add(limitType, "wrap, growx");

        limitType = new JRadioButton("No limit", true);
        limitType.addActionListener(this);
        limitType.setEnabled(false);
        types.add(limitType);
        panel1.add(limitType, "wrap, growx");

        limitType = new JRadioButton("Pot limit", false);
        limitType.addActionListener(this);
        limitType.setEnabled(false);
        types.add(limitType);
        panel1.add(limitType, "wrap, growx");

        limitType = new JRadioButton("Spread limit", false);
        limitType.addActionListener(this);
        limitType.setEnabled(false);
        types.add(limitType);
        panel1.add(limitType, "wrap, growx");

        panel2.setBorder(BorderFactory.createTitledBorder(" Options "));
        panel2.setLayout(new MigLayout("fillx"));

        panel2.add(new JLabel("Max raises:", SwingConstants.RIGHT), "growx");
        panel2.add(raises, "wrap, growx");

        panel3.setBorder(BorderFactory.createTitledBorder(" Forced bets "));
        panel3.setLayout(new MigLayout("fillx"));

        panel3.add(new JLabel("Big blind:", SwingConstants.RIGHT), "growx");
        panel3.add(bigblind, "wrap, growx");

        smallblind.setEnabled(false);
        panel3.add(new JLabel("Small blind:", SwingConstants.RIGHT), "growx");
        panel3.add(smallblind, "wrap, growx");
        
        panel3.add(new JLabel("Ante:", SwingConstants.RIGHT), "growx");
        panel3.add(ante, "wrap, growx");        
        
        bringin.setEnabled(false);
        panel3.add(new JLabel("Bring-in:", SwingConstants.RIGHT), "growx");
        panel3.add(bringin, "wrap, growx");                

        setLayout(new MigLayout("gap 10 10, fill"));

        add(panel1, "grow, width 50%");
        add(panel2, "grow, width 50%, spany 2, wrap");
        add(panel3, "grow, width 50%");
    }

    public int getAnte() {
        return (Integer) ante.getValue();
    }

    public int getBigBlind() {
        return (Integer) bigblind.getValue();
    }

    public int getRaises() {
        return (Integer) raises.getValue();
    }

    public void actionPerformed(ActionEvent e) {
        for (JRadioButton button : types) {
            if (button.equals(e.getSource())) {
                button.setSelected(true);
            } else {
                button.setSelected(false);
            }
        }
    }

}
