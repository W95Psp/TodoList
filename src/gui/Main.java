package gui;
import core.Manager;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Manager global;

	public static void main(String [] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Manager global = Manager.createFromFile("data");
				try {
					MainWindow window = new MainWindow(global);
					window.frmTodoList.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
