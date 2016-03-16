package gui;

import javax.swing.JPanel;

import java.awt.FlowLayout;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.Graphics2D;

import javax.swing.Box;
import javax.swing.JTextField;

import core.LongTask;
import core.Task;
import core.config;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import controlers.Controler;

import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 * Affiche l'état actuel d'une tache longue
 */
public class TaskProgress extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
    private LongTask task;
    DrawingArea drawingArea = new DrawingArea();
    class DrawingArea extends JPanel {

    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		DrawingArea() {
            setPreferredSize(getSize());
            setMinimumSize(new Dimension(140, 10));
        }

        @Override
        public void paintComponent(Graphics g) {
        	super.paintComponent(g);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM");
        	
        	Dimension dim = getSize();

        	//On définit où s'affichera les choses
            int ox = 28;
            int w = dim.width-ox*2;
            int oy = 3;
            int h = 20;
            
            int heightBars = 4;
            int margin = 10;
            
            //Quelque couleurs
            Color gray	= new Color(160, 160, 160);
            Color grayf	= new Color(60, 60, 60);
            Color red	= new Color(255, 60, 60);
            Color white	= new Color(255, 255, 255);

            //Pour mesurer le texte ultérieurement
            Graphics2D g2d = (Graphics2D) g;
            
            g.setColor(gray);
            g.drawLine(ox, oy+h-heightBars-margin, ox+w, oy+h-heightBars-margin);

            g.setFont(config.fontName.deriveFont(9f));
            //Outil mesure texte
            FontMetrics fm = g2d.getFontMetrics();
            float stepX = w/4;
            boolean last = false;
            //Pour toute étape
            for(int milestoneCount = 0; milestoneCount <= 4; milestoneCount++){
            	int currentX = (int) (stepX*milestoneCount+ox);
            	
            	Date milestone = task.getDateMilestone(milestoneCount);
            	String dateStr = format.format(milestone);

            	if(Task.today().before(milestone) && !last){//Si étape suivante
            		last = true;
            		g.setColor(grayf);
                	g.drawLine(currentX+1, oy+h-2*heightBars-margin, currentX+1, oy+h-margin);
            	}else
            		g.setColor(gray);
            	
            	//On trace la petite délimitation
            	g.drawLine(currentX, oy+h-2*heightBars-margin, currentX, oy+h-margin);
            	Rectangle2D rec = fm.getStringBounds(dateStr, g2d);
            	//On affiche la date juste avant la délimitation
            	g.drawString(dateStr, (int) (currentX - rec.getWidth() - 1), 8);
            }
            
            //Draw percent indicator
            g.setColor(task.isLate() ? red : grayf);
            int pX = (int) (((double) task.getPercent())/100.0 * w) + ox;
            //On remplit un rectangle
            g.drawRect(pX-15, oy+h-margin-2, 30, margin+2);
            g.fillRect(pX-15, oy+h-margin-2, 30, margin+2);
            
            //On affiche un cruseur
            g.drawLine(pX, oy+h-2*heightBars-margin, pX, oy+h-margin);
            
            g.setFont(config.fontName.deriveFont(11f));
            String strPercent = task.getPercent()+"%";
            
            fm = g2d.getFontMetrics();
            
            Rectangle2D r = fm.getStringBounds(strPercent, g2d);
            g.setColor(white);
            //On affiche la valeur numérique du pourcent
            g.drawString(strPercent, (int) (pX - r.getWidth()/2), oy+h-margin+8);
        }
    }

    
	/**
	 * Create the panel.
	 */
	public TaskProgress(core.LongTask t, Controler controler) {
		setMinimumSize(new Dimension(10, 30));
		setMaximumSize(new Dimension(32767, 30));
		task = t;
		task.addObserver(this);
		
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 45));
		panel.setMinimumSize(new Dimension(10, 45));
		panel.setMaximumSize(new Dimension(32767, 45));
		panel.setOpaque(false);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel right = new JPanel();
		right.setOpaque(false);
		panel.add(right, BorderLayout.EAST);
		FlowLayout flowLayout_1 = (FlowLayout) right.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(10);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(30);
		right.add(horizontalStrut_1);
		
		JLabel DueDate = new JLabel(" ");
		DueDate.setForeground(Color.GRAY);
		right.add(DueDate);
		DueDate.setFont(config.lightFontName.deriveFont(17f));
		
		Component horizontalStrut = Box.createHorizontalStrut(80);
		panel.add(horizontalStrut, BorderLayout.WEST);
		
		Component verticalStrut = Box.createVerticalStrut(4);
		add(verticalStrut, BorderLayout.SOUTH);


		CardLayout cardPanelCtrl = new CardLayout(0, 0);
		
		drawingArea = new DrawingArea();
		JPanel cardPanel = new JPanel();
		cardPanel.setOpaque(false);
		panel.add(cardPanel, BorderLayout.CENTER);
		cardPanel.setLayout(cardPanelCtrl);
		
		drawingArea.setBackground(new Color(0,0,0,0));
		
		JTextField spinner = new JTextField();
		
		//Permet de modifier le pourcentage
		drawingArea.addMouseListener(new MouseAdapter() {
			long lastClick = new Date().getTime();
			@Override
			public void mouseClicked(MouseEvent arg0) {
				long now = new Date().getTime();
				if((now - lastClick)<250){
					spinner.setText("" + task.getPercent());
					cardPanelCtrl.show(cardPanel, "edit");
					lastClick = 0;
				}else
					lastClick = now;
			}
		});
		
		
		JPanel editPercentage = new JPanel();
		editPercentage.setOpaque(false);
		cardPanel.add(editPercentage, "edit");
		FlowLayout fl_editPercentage = (FlowLayout) editPercentage.getLayout();
		fl_editPercentage.setVgap(0);
		fl_editPercentage.setHgap(0);
		fl_editPercentage.setAlignment(FlowLayout.LEFT);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		editPercentage.add(horizontalStrut_2);
		
		spinner.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent arg0) {
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					controler.setLongTaskPercent(task, Integer.parseInt(spinner.getText()));
					cardPanelCtrl.show(cardPanel, "visu");
				}
			}
		});
		spinner.setPreferredSize(new Dimension(65, 26));
		editPercentage.add(spinner);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(6);
		editPercentage.add(horizontalStrut_3);
		
		JLabel label = new JLabel("%");
		editPercentage.add(label);
		
		editPercentage.setVisible(false);
		
		cardPanel.add(drawingArea, "visu");
		
		cardPanelCtrl.show(cardPanel, "visu");
	}


	@Override
	public void update(Observable arg0, Object arg1) {
		drawingArea.revalidate();
		drawingArea.repaint();
		drawingArea.validate();
		drawingArea.repaint();
	}
}
