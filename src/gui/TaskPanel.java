package gui;

import core.Category;
import core.Task;
import core.config;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JRadioButton;


import javax.swing.JLabel;

import java.awt.Color;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Insets;

import javax.swing.JFormattedTextField;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import controlers.Controler;
import exceptions.BadDateInput;
import exceptions.BadName;
import exceptions.IncorrectDateRange;
import core.LongTask;

import javax.swing.JTextField;
import javax.swing.JButton;

/*
 * Affiche un JPANl pour les tâches
 */
public class TaskPanel extends JPanel implements Observer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Category none = null;
	
	private boolean isActive = true;
	private Task task;
	private Controler controler;
	private JLabel catName;
	private JLabel label;
	private JComboBox<Category> catChoose;
	private boolean refreshing = false;
	private JTextField dateFrom;
	private JTextField dateTo;
	private JLabel date;
	
	public void setEnabled(boolean value){
		this.isActive = value;
	}
	
	public void refresh(){
		if(refreshing)//On évite les refresh concurrents
			return;
		refreshing = true;
		//On ré-écris les données à partir de la tâche
		setToolTipText(task.getDescription());
		catName.setText(task.getCategoryName());
		label.setText(task.getName());
		
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		date.setText(format.format(task.getEndDate()));
		
		//Si une catégorie a changé
		//On supprime toute catégorie
		int itemCount = catChoose.getItemCount();
        for(int i=0;i<itemCount;i++)
        	catChoose.removeItemAt(0);
		
        //On récécris tout
		catChoose.addItem(none);
		for(Category cat : controler.getCategories())
			catChoose.addItem(cat);
		
		//On sélectionne par défaut la catégorie actuelle
		if(task.getCategory()!=null)
			catChoose.setSelectedItem(task.getCategory());
		refreshing = false;
	}
	
	/*
	 * Se charge de créer 
	 */
	public void createPopupMenu(){
		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(this, popupMenu);
		
		JMenuItem mntmSupprimer = new JMenuItem("Supprimer");
		mntmSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controler.removeTask(task);
			}
		});
		mntmSupprimer.setIcon(config.cancelIcon);
		popupMenu.add(mntmSupprimer);
	}
	
	public TaskPanel(Task task, Controler controler, boolean showDate) {
		this.task = task;
		this.controler = controler;
		task.addObserver(this);
		
		//Create comps
		setMaximumSize(new Dimension(32767, 40));
		setOpaque(false);
		
		//Crée le menu contextuel
		createPopupMenu();
		
		setLayout(new BorderLayout(0, 0));
		
		Component verticalStrut = Box.createVerticalStrut(5);
		add(verticalStrut, BorderLayout.NORTH);
		
		JPanel leftPart = new JPanel();
		FlowLayout flowLayout = (FlowLayout) leftPart.getLayout();
		flowLayout.setVgap(0);
		leftPart.setOpaque(false);
		add(leftPart, BorderLayout.WEST);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		leftPart.add(horizontalStrut);
		
		JRadioButton radioButton = new JRadioButton("");
		radioButton.setMargin(new Insets(2, 2, 2, 2));
		//On "trompe" le comportement normal d'un radio bouton
		radioButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!isActive)
					return;
				controler.markTaskAsDone(task);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!isActive)
					return;
				controler.markTaskAsDone(task);
			}
		});
		radioButton.setOpaque(false);
		leftPart.add(radioButton);
		
		label = new JLabel("");
		if(task.isLate())
			label.setForeground(new Color(255, 0, 0));
		JFormattedTextField formattedTextField = new JFormattedTextField();
		formattedTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					controler.setTaskName(task, formattedTextField.getText());
					label.setVisible(true);
					date.setVisible(showDate);
					formattedTextField.setVisible(false);
				}
			}
		});
		
		formattedTextField.setBounds(new Rectangle(0, 0, 200, 0));
		
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				label.setVisible(false);
				date.setVisible(false);
				formattedTextField.setVisible(true);
				formattedTextField.setText(task.getName());
				formattedTextField.setPreferredSize( new Dimension( 200, 24 ) );
			}
		});
		
		
		label.setFont(config.lightFontName.deriveFont(19f));
		leftPart.add(label);

		formattedTextField.setVisible(false);
		
		date = new JLabel("");
		date.setFont(config.lightFontName.deriveFont(13f));
		leftPart.add(date);
		leftPart.add(formattedTextField);
		date.setVisible(showDate);
		
		JPanel rightPart = new JPanel();
		rightPart.setOpaque(false);
		add(rightPart, BorderLayout.EAST);
		rightPart.setLayout(new BoxLayout(rightPart, BoxLayout.Y_AXIS));
		
		Component verticalStrut_1 = Box.createVerticalStrut(5);
		rightPart.add(verticalStrut_1);
		
		JPanel contentRightPart = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) contentRightPart.getLayout();
		flowLayout_1.setVgap(0);
		rightPart.add(contentRightPart);
		contentRightPart.setOpaque(false);
		
		JPanel category = new JPanel();
		category.setOpaque(false);
		contentRightPart.add(category);
		CardLayout categoryLayout = new CardLayout(0, 0);
		category.setLayout(categoryLayout);
		
		JPanel catNamePanel = new JPanel();
		catNamePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				categoryLayout.show(category, "edit");
			}
		});
		catNamePanel.setOpaque(false);
		FlowLayout flowLayout_2 = (FlowLayout) catNamePanel.getLayout();
		flowLayout_2.setAlignment(FlowLayout.RIGHT);
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		category.add(catNamePanel, "view");
		
		try {
			none = new Category("Pas de catégorie");
		} catch (BadName e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catName = new JLabel("");
		catNamePanel.add(catName);
		catName.setForeground(Color.GRAY);
		catName.setFont(config.lightFontName.deriveFont(16f));
		catName.setBackground(Color.LIGHT_GRAY);
		
		catChoose = new JComboBox<Category>();
		category.add(catChoose, "edit");
		
		catChoose.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	if(refreshing)
		    		return;
		    	if(catChoose.getSelectedItem()!=null){
		    		Category cat = (Category) catChoose.getSelectedItem();
		    		if(cat.equals(none))
		    			task.removeCategory();
		    		else
			    		task.setCategory(cat);
		    		//switch
					categoryLayout.show(category, "view");
		    	}
		    }
		});
		
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(5);
		contentRightPart.add(horizontalStrut_1);
		
		JLabel label_2 = new JLabel("");
		label_2.setIcon(config.editIcon);
		contentRightPart.add(label_2);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(10);
		contentRightPart.add(horizontalStrut_2);
		
		JPanel AdditionalContentBox = new JPanel();
		AdditionalContentBox.setVisible(false);
		AdditionalContentBox.setBackground(Color.GREEN);
		AdditionalContentBox.setOpaque(false);
		FlowLayout flowLayout_3 = (FlowLayout) AdditionalContentBox.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		add(AdditionalContentBox, BorderLayout.SOUTH);
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!isActive)
					return;
				dateTo.setText(formater.format(task.getEndDate()));
				if(task.getType()==Task.Types.LONG_TASK){
					dateFrom.setText(formater.format(((LongTask) task).getStartDate()));
				}
				//Pour modifier les dates
				leftPart.setVisible(false);
				rightPart.setVisible(false);
				AdditionalContentBox.setVisible(true);
			}
		});
		
		//alignement
		Component verticalStrut_2 = Box.createVerticalStrut(30);
		AdditionalContentBox.add(verticalStrut_2);
		
		//Du ... au label
		JLabel lblDe = new JLabel("Du");
		AdditionalContentBox.add(lblDe);
		
		//alignement
		Component horizontalStrut_5 = Box.createHorizontalStrut(20);
		AdditionalContentBox.add(horizontalStrut_5);
		
		
		//Parametrage
		if(task.getType()==Task.Types.LONG_TASK){
			dateFrom = new JTextField();
			AdditionalContentBox.add(dateFrom);
			dateFrom.setColumns(10);

			Component horizontalStrut_3 = Box.createHorizontalStrut(20);
			AdditionalContentBox.add(horizontalStrut_3);

			JLabel lblAu = new JLabel("au");
			AdditionalContentBox.add(lblAu);
			
			Component horizontalStrut_4 = Box.createHorizontalStrut(20);
			AdditionalContentBox.add(horizontalStrut_4);
		}else{
			lblDe.setText("Pour le ");
		}
		dateTo = new JTextField();
		AdditionalContentBox.add(dateTo);
		dateTo.setColumns(10);
		
		
		JButton btnOk = new JButton("OK");
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!isActive)
					return;
				try{
					//On test et ajoute
					task.setEndDate(formater.parse(dateTo.getText()));
					if(task.getType()==Task.Types.LONG_TASK)
						((LongTask) task).setStartDate(formater.parse(dateFrom.getText()));
					controler.NotifyTaskListEdition();
				}catch(ParseException|BadDateInput | IncorrectDateRange e){
					//Réinitialisation
					dateTo.setText(formater.format(task.getEndDate()));
					if(task.getType()==Task.Types.LONG_TASK)
						dateFrom.setText(formater.format(((LongTask) task).getStartDate()));
				}
			}
		});
		AdditionalContentBox.add(btnOk);
		
		refresh();
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}


	@Override
	public void update(Observable o, Object arg) {
		//on rafraichit les données en utiliant les catégories et la tâche courante
		refresh();
	}
}
