package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.FocusManager;
import javax.swing.JTextField;

/*
 * Affiche un placeholder pour indiquer à l'utilisateur que faire
 */
public class PlaceholderJTextField extends JTextField {
	private static final long serialVersionUID = 1L;

	private String placeholder;
	public PlaceholderJTextField(String placeholder){
		this.placeholder = placeholder;
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
	    super.paintComponent(g);
	    
	    //On dessine le texte si non vide
	    if(getText().isEmpty() && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)){
	        Graphics2D g2 = (Graphics2D) g.create();
	        g2.setColor(Color.gray);//On écris en gris
	        g2.drawString(placeholder, 5, 15);//On dessine la string
	        g2.dispose();
	    }
	}
}
