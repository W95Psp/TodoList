package gui;
import core.Manager;
import core.config;

import javax.swing.*;

import controlers.Controler;

import java.awt.*;
public class Main extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String [] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				//On charge les polices de caractères
				config.setUpFonts();
				
				//On charge le fichier "data"
				Manager global = Manager.createFromFile("data");
				
				//On essaie de lancer l'application
				try {
					Controler ctrl = new Controler(global);
					MainWindow window = new MainWindow(ctrl);
					ctrl.bindInterface(window);//On bind
					window.frmTodoList.setVisible(true);//On affiche la fenêtre
				} catch (Exception e) {
					// Ne devrait pas se produire
					e.printStackTrace();
				}
			}
		});
	}
}
