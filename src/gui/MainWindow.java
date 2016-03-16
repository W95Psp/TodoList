package gui;

import core.*;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Dimension;
import java.awt.Toolkit;

import controlers.Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * Définit la fenêtre générale de l'interface
 */
public class MainWindow implements Observer {
	Controler controler;
	App app;
	JFrame frmTodoList;
	LeftSlidingMenu menu;
	float currentMenuSize = 0;
	float maxMenuSize = 260;
	/**
	 * Create the application.
	 */
	public MainWindow(Controler controler) {
		this.controler = controler;
		initialize();
	}
	
	/*
	 * Définit un filtre
	 */
	public void setFilter(Category cat){
		app.setFilter(cat);
	}
	
	/*
	 * Ouvre le menu de gauche
	 */
	public void openMenu(){
		setRoutine(currentMenuSize==0 ? 1 : -1);
		//menu.open();
	}
	
	/*
	 * Redimentionne les JPanels pour produire un effet d'animation
	 * @param way dans quel sens est fait l'annimation?
	 */
	private void setRoutine(int way){
		class Annim{
			Timer timer;
			
			public Annim(int speed){
				timer = new Timer(speed, new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						currentMenuSize += way * 13;
						if(currentMenuSize>maxMenuSize || currentMenuSize<0){
							currentMenuSize = (currentMenuSize<0) ? 0 : maxMenuSize;
							timer.stop();
						}
						redrawContent();
					}
				});
				timer.start();
			}
		}
		new Annim(10);
	}
	
	/*
	 * Dessine l'app et le menu (sliding menu) selon currentMenuSize
	 */
	private void redrawContent(){
		app.setSize(new Dimension(100,100));
		Dimension size = frmTodoList.getContentPane().getSize();
		app.setSize(size);

		app.setLocation((int) currentMenuSize, 0);
		menu.setLocation((int) (currentMenuSize - maxMenuSize), 0);
		menu.setSize(new Dimension((int) currentMenuSize, size.height));
	}
	
	/*
	 * Initialise les composants
	 */
	private void initialize() {
		
		//On colle au look and feel du système actuel
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
		
		frmTodoList = new JFrame();
		//fermeture de fenêtre = sauvegarde
		frmTodoList.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				controler.saveData();
			}
		});
		frmTodoList.setTitle("Todo List");
		frmTodoList.setBounds(100, 100, 599, 572);
		frmTodoList.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTodoList.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				redrawContent();
			}
		});
		//Menu et app sont les deux composantes pricipales
		menu = new LeftSlidingMenu(controler);
		app = new App(controler);
		frmTodoList.getContentPane().add(app);
		frmTodoList.getContentPane().add(menu);
		//Layout absolut
		frmTodoList.getContentPane().setLayout(null);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		//reçoit des notifications de Manager lorsque les listes des tâches ou des catégories sont modifiées (ajout|suppr)
		if(o instanceof Manager){
			String type = (String)arg;
			if(type=="cat")
				menu.update(o, arg);//transmet
			else if(type=="tasks")
				app.update(o, arg);//transmet
		}
	}

	public void setSelectedCategory(Category category) {
		//Set filter
		menu.setSelectedCategory(category);
		app.setSelectedCategory(category);
	}

	public void soundIncorrectInput() {
		//Alerte l'utilisateur
		Toolkit.getDefaultToolkit().beep();
	}
}
