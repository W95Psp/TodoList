package gui;

import core.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;

import javax.swing.JToggleButton;
import javax.swing.Box;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.CardLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EtchedBorder;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;

public class MainWindow {

	JFrame frmTodoList;
	Manager manager;
	JPanel contentPanel;

	/**
	 * Create the application.
	 */
	public MainWindow(Manager m) {
		this.manager = m;
		initialize();
	}
	
	public void populate(){
		Map<Long, ArrayList<Task>> tasksByDay = new HashMap<Long, ArrayList<Task>>();
		
		contentPanel.removeAll();
		
		for (Task t : manager.tasks) {
			if(!t.isDone()){
				Long dueDate = t.getEndDate().getTime();
				dueDate = dueDate - dueDate % (1000 * 60 * 60 * 24);
				if(!tasksByDay.containsKey(dueDate))
					tasksByDay.put(dueDate, new ArrayList<Task>());
				ArrayList<Task> listTasks = tasksByDay.get(dueDate);
				listTasks.add(t);
			}
		}
		
		for(Entry<Long, ArrayList<Task>> entry : tasksByDay.entrySet()) {
			Long d = entry.getKey();
		    ArrayList<Task> listTasks = entry.getValue();
		    
		    contentPanel.add(new TitleDay(d+"", "27861"));
		    for(Task t : listTasks)
		    	contentPanel.add(new TaskPanel(t, this));
		}
		
		contentPanel.repaint();
		contentPanel.revalidate();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (InstantiationException e) {
	        e.printStackTrace();
	    } catch (IllegalAccessException e) {
	        e.printStackTrace();
	    } catch (UnsupportedLookAndFeelException e) {
	        e.printStackTrace();
	    }
		
		Color cGreen = new Color(0x1abc9c);
		
		frmTodoList = new JFrame();
		frmTodoList.setTitle("Todo List");
		frmTodoList.setBounds(100, 100, 599, 572);
		frmTodoList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTodoList.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel topbar = new JPanel();
		topbar.setBackground(cGreen);
		frmTodoList.getContentPane().add(topbar, BorderLayout.NORTH);
		topbar.setLayout(new BorderLayout(0, 0));
		
		JPanel left = new JPanel();
		left.setOpaque(false);
		topbar.add(left, BorderLayout.WEST);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		left.add(horizontalStrut_2);
		
		JLabel lblNewLabel_1 = new JLabel("Pas de retard");
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		lblNewLabel_1.setForeground(Color.WHITE);
		left.add(lblNewLabel_1);
		
		JPanel right = new JPanel();
		FlowLayout flowLayout = (FlowLayout) right.getLayout();
		flowLayout.setVgap(10);
		right.setOpaque(false);
		topbar.add(right, BorderLayout.EAST);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon("C:\\Users\\W95psp\\Documents\\GitHub\\TodoList\\images\\add64.png"));
		right.add(label_2);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(20);
		right.add(horizontalStrut_3);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\W95psp\\Documents\\GitHub\\TodoList\\images\\gear39.png"));
		right.add(lblNewLabel);
		
		Component horizontalStrut_4 = Box.createHorizontalStrut(20);
		right.add(horizontalStrut_4);
		
		JScrollPane content = new JScrollPane();
		content.setBorder(null);
		frmTodoList.getContentPane().add(content, BorderLayout.CENTER);
		content.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		content.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		content.setViewportView(contentPanel);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		populate();
		
		JPanel bottom = new JPanel();
		bottom.setBackground(cGreen);
		frmTodoList.getContentPane().add(bottom, BorderLayout.SOUTH);
	}

}
