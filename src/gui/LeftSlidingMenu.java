package gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.Component;

import javax.swing.Box;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;

import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingConstants;

import core.Category;
import core.config;

import javax.swing.JButton;

import controlers.Controler;

/*
 * Représente le menu coulissant de gauche
 */
public class LeftSlidingMenu extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	private JPanel Content;
	private int maxMenuSize = 260;
	public Controler controler;
	private JPanel inner;
	private Component horizontalStrut;
	private JButton btnAucune;
	private JPanel catList;
	
	/*
	 * Supprime une catégorie
	 */
	public void deleteCategory(Category cat, JPanel child){
		controler.removeCategory(cat);
		inner.remove(child);
	}
	/**
	 * Create the panel.
	 */
	public LeftSlidingMenu(Controler controler) {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
		});
		this.controler = controler;
		
		Content = new JPanel();
		FlowLayout flowLayout = (FlowLayout) Content.getLayout();
		flowLayout.setVgap(10);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		Content.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(Content);
		
		inner = new JPanel();

		inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
		inner.setOpaque(false);
		inner.setBackground(Color.YELLOW);
		Content.add(inner);
		
		horizontalStrut = Box.createHorizontalStrut(maxMenuSize - 30);
		inner.add(horizontalStrut);
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);
		inner.add(panel);
		
		JLabel lblCategories = new JLabel("Cat\u00E9gories");
		panel.add(lblCategories);
		lblCategories.setHorizontalTextPosition(SwingConstants.LEFT);
		lblCategories.setHorizontalAlignment(SwingConstants.LEFT);
		
		
		lblCategories.setFont(config.semiBoldFontName.deriveFont(22f));
		
		btnAucune = new JButton("D\u00E9s\u00E9lectionner");
		btnAucune.setFont(config.fontName.deriveFont(10f));
		btnAucune.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				controler.setCurrentViewedCategory(null);
				controler.getMainView().openMenu();
			}
		});
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		panel.add(horizontalStrut_1);
		panel.add(btnAucune);
		
		//CatList contient toutes les catégories
		catList = new JPanel();
		catList.setLayout(new BoxLayout(catList, BoxLayout.Y_AXIS));
		inner.add(catList);
		
		inner.add(Box.createVerticalStrut(10));
		
		PlaceholderJTextField addNew = new PlaceholderJTextField("Ajouter...");
		addNew.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					controler.addCategory(addNew.getText());
					addNew.setText("");
				}
			}
		});
		inner.add(addNew);
	}

	@Override
	public void update(Observable o, Object arg) {
		//C'est un ordre de Manger, la liste a été modifiée
		refresh();
	}
	public void setSelectedCategory(Category category){
		//On parcours tous les enfants de catList
		//On modifie le fond
		for (Component catComponent : catList.getComponents()) {
			CategoryControl catCtrl = (CategoryControl) catComponent;
			catCtrl.setOpaque(catCtrl.isCategoryEqualsTo(category));//si catCtrl est category, le fond doit être blanc
		}
	}
	private void refresh() {
		revalidate();
		repaint();
		//On vire tout
		catList.removeAll();
		//on ré-écrit
		for (Category cat : controler.getCategories())
			catList.add(new CategoryControl(cat, controler));
	}
}
