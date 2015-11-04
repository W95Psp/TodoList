package gui;

import core.Task;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JRadioButton;

import java.awt.Font;

import javax.swing.JLabel;

import java.awt.Color;

import javax.swing.ImageIcon;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Insets;

public class TaskPanel extends JPanel {
	MainWindow parent;
	/**
	 * Create the panel.
	 */
	public TaskPanel(Task task, MainWindow parent) {
		setMaximumSize(new Dimension(32767, 40));
		this.parent = parent;
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut = Box.createVerticalStrut(1);
		add(verticalStrut, BorderLayout.NORTH);
		
		JPanel leftPart = new JPanel();
		leftPart.setOpaque(false);
		add(leftPart, BorderLayout.WEST);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		leftPart.add(horizontalStrut);
		
		JRadioButton radioButton = new JRadioButton("");
		radioButton.setMargin(new Insets(2, 2, 2, 2));
		radioButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(parent.posMenuX!=0)
					return;
				task.markAsDone();
				parent.populate();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(parent.posMenuX!=0)
					return;
				task.markAsDone();
				parent.populate();
			}
		});
		radioButton.setOpaque(false);
		leftPart.add(radioButton);
		
		JLabel label = new JLabel(task.getName());
		label.setFont(new Font("Segoe UI Light", Font.PLAIN, 19));
		leftPart.add(label);
		
		JPanel rightPart = new JPanel();
		rightPart.setOpaque(false);
		add(rightPart, BorderLayout.EAST);
		rightPart.setLayout(new BoxLayout(rightPart, BoxLayout.Y_AXIS));
		
		Component verticalStrut_1 = Box.createVerticalStrut(5);
		rightPart.add(verticalStrut_1);
		
		JPanel contentRightPart = new JPanel();
		rightPart.add(contentRightPart);
		contentRightPart.setOpaque(false);
		
		JLabel catName = new JLabel(task.getCategoryName());
		catName.setForeground(Color.GRAY);
		catName.setFont(new Font("Segoe UI Light", Font.PLAIN, 16));
		catName.setBackground(Color.LIGHT_GRAY);
		contentRightPart.add(catName);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		contentRightPart.add(horizontalStrut_1);
		
		JLabel label_2 = new JLabel("");
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(parent.posMenuX!=0)
					return;
				System.out.print("Click on task");
			}
		});
		label_2.setIcon(new ImageIcon("C:\\Users\\W95psp\\Documents\\GitHub\\TodoList\\images\\pencil41.png"));
		contentRightPart.add(label_2);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(10);
		contentRightPart.add(horizontalStrut_2);
	}

}
