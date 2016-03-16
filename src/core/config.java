package core;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

public class config {
	public static Font fontName;
	public static Font lightFontName;
	public static Font semiBoldFontName;
	public static ImageIcon menuIcon = new ImageIcon("images/menu48 (2).png");
	public static ImageIcon addIcon = new ImageIcon("images/add64.png");
	public static ImageIcon editIcon = new ImageIcon("images/pencil41.png");
	public static ImageIcon cancelIcon = new ImageIcon("images/cancel30.png");
	public static ImageIcon checkIcon = new ImageIcon("images/check52.png");
	
	public static void setUpFonts(){
		GraphicsEnvironment ge = 
	    GraphicsEnvironment.getLocalGraphicsEnvironment();
	    
		try {
			ge.registerFont(semiBoldFontName = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/OpenSans-Semibold.ttf")));
			ge.registerFont(lightFontName = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/OpenSans-Light.ttf")));
			ge.registerFont(fontName = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/OpenSans-Regular.ttf")));
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
