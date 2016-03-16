package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.JLabel;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JRadioButton;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import core.Category;
import core.config;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

import controlers.Controler;

/*
 * Définit une nouvelle tâche
 */
public class NewTask extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField dueDate;
	private JTextField taskStart;
	JButton okButton;
	
	ImageIcon correct;
	ImageIcon incorrect;
	
	JLabel verifName;
	JLabel verifTaskStart;
	JLabel verifDueDate;
	
	boolean isShort = true;

	public void refreshOkState(){
		okButton.setEnabled(
				verifName.getIcon()!=null && verifName.getIcon().equals(correct)			&&
				(
						isShort ||
						verifTaskStart.getIcon()!=null && verifTaskStart.getIcon().equals(correct)
				)	&&
				verifDueDate.getIcon()!=null && verifDueDate.getIcon().equals(correct)
			);
	}
	
	/**
	 * Create the dialog.
	 */
	public NewTask(Controler controler, Category defaultCat) {
		//interface graphique, partie inintéressante, non commenée
		
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		setTitle("Ajouter une t\u00E2che");
		setIconImage(Toolkit.getDefaultToolkit().getImage("images/add64.png"));
		setBounds(100, 100, 450, 405);
		BorderLayout borderLayout = new BorderLayout();
		getContentPane().setLayout(borderLayout);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		correct = config.checkIcon;
		incorrect = config.cancelIcon;

		JLabel lblTaskName = new JLabel("Nom de la t\u00E2che :");
		lblTaskName.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblTaskName = new GridBagConstraints();
		gbc_lblTaskName.anchor = GridBagConstraints.WEST;
		gbc_lblTaskName.insets = new Insets(0, 0, 5, 5);
		gbc_lblTaskName.gridx = 0;
		gbc_lblTaskName.gridy = 1;
		contentPanel.add(lblTaskName, gbc_lblTaskName);

		JFormattedTextField taskName = new JFormattedTextField();
		GridBagConstraints gbc_taskName = new GridBagConstraints();
		gbc_taskName.gridwidth = 2;
		gbc_taskName.insets = new Insets(0, 0, 5, 5);
		gbc_taskName.fill = GridBagConstraints.HORIZONTAL;
		gbc_taskName.gridx = 1;
		gbc_taskName.gridy = 1;
		contentPanel.add(taskName, gbc_taskName);

		verifName = new JLabel("");
		taskName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				Boolean r = Pattern.matches(".+", taskName.getText());
				verifName.setIcon(r ? correct : incorrect);
				refreshOkState();
			}
		});
		verifName.setIcon(null);
		GridBagConstraints gbc_verifName = new GridBagConstraints();
		gbc_verifName.insets = new Insets(0, 0, 5, 0);
		gbc_verifName.gridx = 3;
		gbc_verifName.gridy = 1;
		contentPanel.add(verifName, gbc_verifName);
		
		JLabel lblDescription = new JLabel("Description :");
		GridBagConstraints gbc_lblDescription = new GridBagConstraints();
		gbc_lblDescription.anchor = GridBagConstraints.WEST;
		gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescription.gridx = 0;
		gbc_lblDescription.gridy = 2;
		contentPanel.add(lblDescription, gbc_lblDescription);
		
		JTextArea description = new JTextArea();
		GridBagConstraints gbc_description = new GridBagConstraints();
		gbc_description.gridheight = 2;
		gbc_description.gridwidth = 2;
		gbc_description.insets = new Insets(0, 0, 5, 5);
		gbc_description.fill = GridBagConstraints.BOTH;
		gbc_description.gridx = 1;
		gbc_description.gridy = 2;
		contentPanel.add(description, gbc_description);
		
		JLabel verifDescription = new JLabel("");
		GridBagConstraints gbc_verifDescription = new GridBagConstraints();
		gbc_verifDescription.insets = new Insets(0, 0, 5, 0);
		gbc_verifDescription.gridx = 3;
		gbc_verifDescription.gridy = 2;
		contentPanel.add(verifDescription, gbc_verifDescription);

		JLabel lblTaskType = new JLabel("Type de la t\u00E2che :");
		lblTaskType.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblTaskType = new GridBagConstraints();
		gbc_lblTaskType.anchor = GridBagConstraints.WEST;
		gbc_lblTaskType.insets = new Insets(0, 0, 5, 5);
		gbc_lblTaskType.gridx = 0;
		gbc_lblTaskType.gridy = 4;
		contentPanel.add(lblTaskType, gbc_lblTaskType);
		
		ButtonGroup TaskType = new ButtonGroup();
		JRadioButton shortTaskRadio = new JRadioButton("Courte");
		JRadioButton longTaskRadio = new JRadioButton("Longue");
		
		shortTaskRadio.setSelected(true);
		GridBagConstraints gbc_shortTaskRadio = new GridBagConstraints();
		gbc_shortTaskRadio.anchor = GridBagConstraints.WEST;
		gbc_shortTaskRadio.insets = new Insets(0, 0, 5, 5);
		gbc_shortTaskRadio.gridx = 1;
		gbc_shortTaskRadio.gridy = 4;
		contentPanel.add(shortTaskRadio, gbc_shortTaskRadio);
		
		GridBagConstraints gbc_longTaskRadio = new GridBagConstraints();
		gbc_longTaskRadio.anchor = GridBagConstraints.WEST;
		gbc_longTaskRadio.insets = new Insets(0, 0, 5, 5);
		gbc_longTaskRadio.gridx = 2;
		gbc_longTaskRadio.gridy = 4;
		contentPanel.add(longTaskRadio, gbc_longTaskRadio);

		TaskType.add(shortTaskRadio);
		shortTaskRadio.setActionCommand("short");
		TaskType.add(longTaskRadio);
		longTaskRadio.setActionCommand("long");

		JLabel verifTypeTask = new JLabel("");
		GridBagConstraints gbc_verifTypeTask = new GridBagConstraints();
		gbc_verifTypeTask.insets = new Insets(0, 0, 5, 0);
		gbc_verifTypeTask.gridx = 3;
		gbc_verifTypeTask.gridy = 4;
		contentPanel.add(verifTypeTask, gbc_verifTypeTask);

		JLabel lblTaskStart = new JLabel("D\u00E9but de la t\u00E2che :");
		GridBagConstraints gbc_lblTaskStart = new GridBagConstraints();
		gbc_lblTaskStart.anchor = GridBagConstraints.WEST;
		gbc_lblTaskStart.insets = new Insets(0, 0, 5, 5);
		gbc_lblTaskStart.gridx = 0;
		gbc_lblTaskStart.gridy = 5;
		contentPanel.add(lblTaskStart, gbc_lblTaskStart);
		

		taskStart = new PlaceholderJTextField("13/01/2015");
		taskStart.setColumns(10);
		GridBagConstraints gbc_taskStart = new GridBagConstraints();
		gbc_taskStart.gridwidth = 2;
		gbc_taskStart.insets = new Insets(0, 0, 5, 5);
		gbc_taskStart.fill = GridBagConstraints.HORIZONTAL;
		gbc_taskStart.gridx = 1;
		gbc_taskStart.gridy = 5;
		contentPanel.add(taskStart, gbc_taskStart);

		verifTaskStart = new JLabel("");
		taskStart.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				// On vérifie la bonne forme de la date
				Boolean r = Pattern.matches("(\\d{2}/){2}\\d{4}", taskStart.getText());
				verifTaskStart.setIcon(r ? correct : incorrect);
				refreshOkState();
			}
		});
		GridBagConstraints gbc_verifTaskStart = new GridBagConstraints();
		gbc_verifTaskStart.insets = new Insets(0, 0, 5, 0);
		gbc_verifTaskStart.gridx = 3;
		gbc_verifTaskStart.gridy = 5;
		contentPanel.add(verifTaskStart, gbc_verifTaskStart);
		
		lblTaskStart.setVisible(false);
		verifTaskStart.setVisible(false);
		taskStart.setVisible(false);
		shortTaskRadio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//On affiche ou non certaines zones de texte selon le type de la task
				ButtonModel found = TaskType.getSelection();
				if(found!=null){
					boolean isLong = !(isShort=found.getActionCommand()=="short");
					lblTaskStart.setVisible(isLong);
					verifTaskStart.setVisible(isLong);
					taskStart.setVisible(isLong);
				}
				refreshOkState();
			}
		});

		JLabel lblDueDate = new JLabel("Fin de la t\u00E2che :");
		lblDueDate.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblDueDate = new GridBagConstraints();
		gbc_lblDueDate.anchor = GridBagConstraints.WEST;
		gbc_lblDueDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblDueDate.gridx = 0;
		gbc_lblDueDate.gridy = 6;
		contentPanel.add(lblDueDate, gbc_lblDueDate);

		dueDate = new PlaceholderJTextField("13/01/2015");
		GridBagConstraints gbc_dueDate = new GridBagConstraints();
		gbc_dueDate.gridwidth = 2;
		gbc_dueDate.insets = new Insets(0, 0, 5, 5);
		gbc_dueDate.fill = GridBagConstraints.HORIZONTAL;
		gbc_dueDate.gridx = 1;
		gbc_dueDate.gridy = 6;
		contentPanel.add(dueDate, gbc_dueDate);
		dueDate.setColumns(10);

		verifDueDate = new JLabel("");
		dueDate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				// On vérifie la bonne forme de la date
				Boolean r = Pattern.matches("(\\d{2}/){2}\\d{4}", dueDate.getText());
				verifDueDate.setIcon(r ? correct : incorrect);
				refreshOkState();
			}
		});
		GridBagConstraints gbc_verifDueDate = new GridBagConstraints();
		gbc_verifDueDate.insets = new Insets(0, 0, 5, 0);
		gbc_verifDueDate.gridx = 3;
		gbc_verifDueDate.gridy = 6;
		contentPanel.add(verifDueDate, gbc_verifDueDate);

		JLabel lblCat = new JLabel("Cat\u00E9gorie :");
		GridBagConstraints gbc_lblCat = new GridBagConstraints();
		gbc_lblCat.anchor = GridBagConstraints.WEST;
		gbc_lblCat.insets = new Insets(0, 0, 5, 5);
		gbc_lblCat.gridx = 0;
		gbc_lblCat.gridy = 7;
		contentPanel.add(lblCat, gbc_lblCat);
		

		DefaultListModel<Category> defaultListModel = new DefaultListModel<Category>();

		for(Category cat : controler.getCategories())
			defaultListModel.addElement(cat);
		
		JList<Category> CatList = new JList<Category>(defaultListModel);
		CatList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GridBagConstraints gbc_CatList_1 = new GridBagConstraints();
		gbc_CatList_1.gridwidth = 2;
		gbc_CatList_1.gridheight = 3;
		gbc_CatList_1.insets = new Insets(0, 0, 0, 5);
		gbc_CatList_1.gridx = 1;
		gbc_CatList_1.gridy = 7;
		
		CatList.setVisibleRowCount(10);
		CatList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override //Permet d'afficher des Category dans la liste
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Category) {
                    ((JLabel) renderer).setText(((Category) value).getName());
                }
                return renderer;
            }
        });
		
		//Par défaut
		CatList.setModel(defaultListModel);
		if(defaultCat!=null)
			CatList.setSelectedValue(defaultCat, true);
		contentPanel.add(CatList, gbc_CatList_1);

		JLabel verifCat = new JLabel("");
		GridBagConstraints gbc_verifCat = new GridBagConstraints();
		gbc_verifCat.insets = new Insets(0, 0, 5, 0);
		gbc_verifCat.gridx = 3;
		gbc_verifCat.gridy = 7;
		contentPanel.add(verifCat, gbc_verifCat);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JDialog This = this;
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			okButton = new JButton("Ajouter");
			okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");
					String title = taskName.getText();
					String desc = description.getText();
					Date due = null;
					try {
						due = d.parse(dueDate.getText());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if(isShort){
						controler.addTask(title, desc, due, CatList.getSelectedValue());
					}else{
						Date start = null;
						try {
							start = d.parse(taskStart.getText());
						} catch (ParseException e) {
							e.printStackTrace();
						}
						controler.addTask(title, desc, start, due, CatList.getSelectedValue());
					}
					This.dispose();
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Annuler");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					This.dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
		refreshOkState();
	}

}
