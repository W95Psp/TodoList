package gui;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.FlowLayout;

import java.awt.CardLayout;

import javax.swing.JTextField;

import controlers.Controler;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import core.Category;
import core.config;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Color;

/*
 * Contrôle pour une catégorie
 */
public class CategoryControl extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JLabel label;
	private Category category;
	
	public boolean isCategoryEqualsTo(Category c){
		if(c==null)
			return false;
		return c.equals(category);
	}
	
	/**
	 * Create the panel.
	 * @return 
	 */
	
	private void resetLabelsBackground(){
		//TODO: parent.resetLabelsBackground();
	}
	
	public void setOpaque(boolean v){
		if(label!=null)
			label.setOpaque(v);
	}
	
	public CategoryControl(Category category, Controler controler) {
		JPanel This = this;
		
		setLayout(new CardLayout(0, 0));
		
		JPanel view = new JPanel();
		add(view, "view");
		view.setLayout(new BorderLayout(0, 0));
		
		//Nom de la catégorie
		label = new JLabel(category.getName());
		label.setBackground(Color.WHITE);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				resetLabelsBackground();
				controler.setCurrentViewedCategory(category);
				controler.getMainView().openMenu();
				label.setOpaque(true);
			}
		});
		view.add(label, BorderLayout.WEST);
		
		label.setFont(config.lightFontName.deriveFont(19f));
		
		JPanel right = new JPanel();
		view.add(right, BorderLayout.EAST);
		
		//edition
		JLabel buttonEdit = new JLabel("");
		right.add(buttonEdit);
		buttonEdit.setIcon(config.editIcon);
		
		//suppression
		JLabel buttonDelete = new JLabel("");
		right.add(buttonDelete);
		buttonDelete.setIcon(config.cancelIcon);
		buttonDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				resetLabelsBackground();
				controler.removeCategory(category);
				controler.setCurrentViewedCategory(null);
			}
		});

		JPanel edit = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) edit.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		add(edit, "edit");

		//CardLayout pour switcher entre vue visualisation et vue édition
		CardLayout cardLayout = (CardLayout) getLayout();
		cardLayout.show(this, "view");

		buttonEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				textField.setText(label.getText());
				//switch
				cardLayout.show(This, "edit");
			}
		});
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					label.setText(textField.getText());
					controler.setCategoryName(category, textField.getText());
					//switch
					cardLayout.show(This, "view");
				}
			}
		});
		edit.add(textField);
		textField.setColumns(10);
	}

}
