package gui;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;

import core.config;

/*
 * Affiche un header
 */
public class TitleDay extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public TitleDay(String day, String date) {
		setMaximumSize(new Dimension(32767, 40));
		setOpaque(false);
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setAlignOnBaseline(true);
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		add(horizontalStrut);
		
		JLabel label = new JLabel(day);
		add(label);
		label.setForeground(Color.DARK_GRAY);
		
		label.setFont(config.semiBoldFontName.deriveFont(24f));
		
		JLabel label_1 = new JLabel(date);
		add(label_1);
		label_1.setForeground(Color.GRAY);
	}

}
