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
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;

import javax.swing.JToggleButton;
import javax.swing.Box;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.CardLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EtchedBorder;
import javax.swing.JRadioButton;
import javax.swing.ImageIcon;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainWindow {

	JFrame frmTodoList;
	Manager manager;
	JPanel contentPanel;
	JPanel panel;
	JPanel menu;
	JPanel effect;
	int posMenuX;

	/**
	 * Create the application.
	 */
	public MainWindow(Manager m) {
		this.manager = m;
		posMenuX = 0;
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


		enableComponents(panel, posMenuX==0);
	}
	
	Timer timer;
	float vit;
	int maxPosMenu = 220;
	int oldPosMenu = -1;

	private void launchMenuAnnimation(){
		if(timer!=null)
			return;
		vit = 10;
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				redrawStuff();
				if(posMenuX>=maxPosMenu){
					timer.stop();
					timer = null;
					posMenuX = maxPosMenu;
				}else
					posMenuX+=(vit+=0.5);
			}
		});

		timer.start();
	}
	private void launchMenuAnnimationReverse(){
		if(timer!=null)
			return;
		vit = 15;
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				redrawStuff();
				if(posMenuX<0){
					timer.stop();
					timer = null;
					posMenuX = 0;
					frmTodoList.getContentPane().setComponentZOrder(panel, 2);
					frmTodoList.getContentPane().setComponentZOrder(effect, 1);
					frmTodoList.getContentPane().setComponentZOrder(menu, 0);
					

					menu.repaint();
					menu.revalidate();
				}else
					posMenuX-=(vit+=0.5);
			}
		});

		timer.start();
	}
	public void enableComponents(Container container, boolean enable) {
	    Component[] components = container.getComponents();
	    for (Component component : components) {
	        component.setEnabled(enable);
	        if (component instanceof Container) {
	            enableComponents((Container)component, enable);
	        }
	    }
	}

	
	private void redrawStuff(){
		Dimension size = frmTodoList.getContentPane().getSize();
		panel.setSize(size);
		menu.setSize(new Dimension(posMenuX, size.height));
		System.out.print(oldPosMenu+"\t"+posMenuX+"\n\r");
		if(posMenuX>0){
			effect.setLocation(posMenuX, 0);
			effect.setBackground(new Color(0, 0, 0, 50 * posMenuX/maxPosMenu));
			effect.setSize(new Dimension(size.width-posMenuX, size.height));
		
			enableComponents(panel, posMenuX==0);
			effect.setOpaque(true);
		}else{
			effect.setOpaque(false);
			effect.setLocation(0, 0);
			effect.setSize(new Dimension(0, 0));
			enableComponents(panel, true);
		}
		oldPosMenu = posMenuX;
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
		frmTodoList.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				redrawStuff();
			}
		});
		frmTodoList.setTitle("Todo List");
		frmTodoList.setBounds(100, 100, 599, 572);
		frmTodoList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frmTodoList.getContentPane().setLayout(null);
		
		effect = new JPanel();
		effect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				launchMenuAnnimationReverse();
			}
		});
		effect.setBackground(Color.DARK_GRAY);
		effect.setOpaque(false);
		effect.setBounds(365, 0, 135, 500);
		frmTodoList.getContentPane().add(effect);
		
		menu = new JPanel();
		menu.setBounds(0, 0, 227, 516);
		frmTodoList.getContentPane().add(menu);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 577, 516);
		frmTodoList.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel topbar = new JPanel();
		panel.add(topbar, BorderLayout.NORTH);
		topbar.setBackground(cGreen);
		topbar.setLayout(new BorderLayout(0, 0));
		 
		JPanel left = new JPanel();
		left.setOpaque(false);
		topbar.add(left, BorderLayout.WEST);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(10);
		left.add(horizontalStrut_2);
		
		JLabel label = new JLabel("");
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				launchMenuAnnimation();
			}
		});
		label.setIcon(new ImageIcon("C:\\Users\\W95psp\\Documents\\GitHub\\TodoList\\images\\menu48 (2).png"));
		left.add(label);
		
		Component horizontalStrut = Box.createHorizontalStrut(5);
		left.add(horizontalStrut);
		
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
		panel.add(content, BorderLayout.CENTER);
		content.setBorder(null);
		content.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		content.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				redrawStuff();
			}
		});
		content.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		content.setViewportView(contentPanel);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		JPanel bottom = new JPanel();
		panel.add(bottom, BorderLayout.SOUTH);
		bottom.setBackground(cGreen);
		

		populate();
	}
}
