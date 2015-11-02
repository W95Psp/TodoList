package gui;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;

public class TitleDay extends JPanel {

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
		label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
		
		JLabel label_1 = new JLabel(date);
		add(label_1);
		label_1.setForeground(Color.GRAY);
	}

}
