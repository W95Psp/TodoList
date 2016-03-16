package gui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import controlers.Controler;
import core.Category;
import core.LongTask;
import core.Manager;
import core.Task;
import core.config;
import exceptions.BadName;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * Représente la liste des tâches et le header
 */
public class App extends JPanel implements Observer {
	private static final long serialVersionUID = 1L;
	Controler controler;
	JPanel contentPanel;
	JLabel categoryButton;
	JLabel currentFilter;
	Category all;
	public Category currentCatFilter;
	private int mode;
	
	/*
	 * Vide et remplis contentPanel, qui contient les TaskPanels, c'est-à-dire
	 * les composants swings qui correspondent visuellement à des tâches
	 */
	public void populate(Manager.TasksList tasks){
		//On organise les tâches [date échéance | ordre priorité] -> liste de tâches
		Map<Long, ArrayList<Task>> tasksByDay = new HashMap<Long, ArrayList<Task>>();
		
		contentPanel.removeAll();
		
		// deux comportements de tris différents
		if(mode==2){
			//Soient trois catégories de priorités
			tasksByDay.put((long) 0, new ArrayList<Task>());
			tasksByDay.put((long) 1, new ArrayList<Task>());
			tasksByDay.put((long) 2, new ArrayList<Task>());
			int count = 0;
			//On filtre les tâches pour éliminer les tâches non terminées
			//Ensuite on les tries par ordre de d'échéance croissante
			//Puis on les attribuent dans l'ordre
			for(Object o : tasks.stream().filter(
						(a) -> !((Task) a).isDone()
					).sorted(
						(a, b) -> (int) (((Task) a).getEndDate().getTime() - ((Task) b).getEndDate().getTime())
					).toArray()){
				//State est 0, 1 ou 2, selon le compteur.
				int state = (count>0 ? 1 : 0) + (count>3 ? 1 : 0);
				//On range la tâche
				tasksByDay.get((long) state).add((Task) o);
				
				count++;
				if(count==9)//Si on a déjà affiché 1 tahce importante, 3 moyuenne, et 5 petites (1+3+5 = 9)
					break;
			}
		}else
			//Ici, on veut regrouper les tâches par date d'écheances.
			for (Task t : tasks) {
				if(!t.isDone() && 
							(
								currentCatFilter.equals(all)
									||
								currentCatFilter.equals(t.getCategory())
							)
						){
					Long dueDate = t.getEndDate().getTime();
					//Si le mode est "Priorité", on choisit la prochaine échéance si c'est une tache longue
					if(mode==1 && t.getType()==Task.Types.LONG_TASK)
						dueDate = ((LongTask) t).getNextMilestone().getTime();
					
					//Si il n'y a pas de clé dans la map, on crée une liste
					if(!tasksByDay.containsKey(dueDate))
						tasksByDay.put(dueDate, new ArrayList<Task>());
					//On remplis la liste
					ArrayList<Task> listTasks = tasksByDay.get(dueDate);
					listTasks.add(t);
				}
			}
	
		int insertedTasks = 0;//Pour afficher un message s'il n'y a pas de tâches
		//On trie les clés : on trie donc les dates d'échéances
		for(Object indexO : tasksByDay.keySet().stream().sorted().toArray()){
			//On récupère les données intéressantess
			Long d = (long) indexO;
		    ArrayList<Task> listTasks = tasksByDay.get(d);
			
			if(mode==2){//par priorités
				String[] priorities = new String[]{"importante", "moyenne", "faible"};
				//On ajoute un header pour "ranger" les tâches par priorités visuellement
			    contentPanel.add(new TitleDay("Priorité "+priorities[(int) (d%3)], ""));
			}else{
				//Permet de formater les dates
				SimpleDateFormat titleFormat = new SimpleDateFormat("EEEE");
				SimpleDateFormat mFormat = new SimpleDateFormat("d MMM y");
				//On convertit l'échéance courante (Long) en Date pour les convertir en string
				Date dDate = new Date(d);
				//Le titre les un jour au format long (EEEE)
				String title = titleFormat.format(dDate);
				//Première lettre en majuscule
				title = title.substring(0, 1).toUpperCase() + title.substring(1);
				
				//On ajoute le TitleDay
			    contentPanel.add(new TitleDay(title, mFormat.format(dDate)));
			}
			//On itère sur les tâches dont l'échance est d
		    for(Task t : listTasks){
		    	insertedTasks++;
		    	TaskPanel tmp = new TaskPanel(t, controler, mode==2); //Si mode==2, on affiche la date d'échéance dans le corps (effectivement, les header ne montrent visuellement que l'odre de priorité dans ce cas)
		    	tmp.setFocusable(false);
		    	contentPanel.add(tmp);
		    	//Si la tâche est longue, on ajoute la progression et le contrôle du pourcentage
		    	if(t.getType()==Task.Types.LONG_TASK)
		    		contentPanel.add(new TaskProgress((core.LongTask) t/*cast sûr, on sait que c'est une ta^che longue*/, controler));
		    }
		}
		// Afiche un message
		if(insertedTasks==0){
			if(currentFilter==null)
				contentPanel.add(new JLabel("Pas de tâches"));
			else
				contentPanel.add(new JLabel("Pas de tâches dans cette catégorie"));
		}
		
		//On évite les problèmes d'affichages
		contentPanel.repaint();
		contentPanel.revalidate();
	}
	//Change de mode simple -> priorité -> résumé...
	public void switchMode(){
		mode = (mode+1)%3;
		populate(controler.getTasks());
	}
	
	/*
	 * ON construit l'interface
	 */
	public App(Controler controler) {
		this.controler = controler;
		
		Color cGreen = new Color(0x1abc9c);

		setLayout(new BorderLayout(0, 0));

		//Barre du hat
		JPanel topbar = new JPanel();
		add(topbar, BorderLayout.NORTH);
		topbar.setBackground(cGreen);
		topbar.setLayout(new BorderLayout(0, 0));
		 
			JPanel left = new JPanel();
			left.setOpaque(false);
			topbar.add(left, BorderLayout.WEST);

			Component horizontalStrut_2 = Box.createHorizontalStrut(10);
			left.add(horizontalStrut_2);

			categoryButton = new JLabel("");
			categoryButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent arg0) {
					controler.getMainView().openMenu();
				}
			});
			categoryButton.setIcon(config.menuIcon);
			left.add(categoryButton);

			Component horizontalStrut = Box.createHorizontalStrut(5);
			left.add(horizontalStrut);

		try {
			all = new Category("Pas de filtre");
		} catch (BadName e1) {
			e1.printStackTrace();
		}
		
		//Filtre actuel
		currentFilter = new JLabel(all.getName());
		setFilter(all);
		currentFilter.setFont(config.fontName.deriveFont(24f));
		currentFilter.setForeground(Color.WHITE);
		left.add(currentFilter);

		//partie droite de la barre top
		JPanel right = new JPanel();
		FlowLayout flowLayout = (FlowLayout) right.getLayout();
		flowLayout.setVgap(10);
		right.setOpaque(false);
		topbar.add(right, BorderLayout.EAST);

		//Bouton d'ajout de tâche
		JLabel label_2 = new JLabel("");
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				controler.askUserANewTask(currentCatFilter.equals(all) ? null : currentCatFilter);
			}
		});
		
		//Mode par défaut
		mode = 0;
		//Modes
		String[] modes = new String[]{"Simple", "Priorité", "Résumé"};
		
		//Bouton switch mdoe
		JButton btnMode = new JButton(modes[mode]);
		btnMode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				switchMode();
				btnMode.setText(modes[mode]);
			}
		});
		right.add(btnMode);
		
		//alignements
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		right.add(horizontalStrut_1);
		
		//Icone pour le bouton ajout de tâche
		label_2.setIcon(config.addIcon);
		right.add(label_2);

		Component horizontalStrut_3 = Box.createHorizontalStrut(10);
		right.add(horizontalStrut_3);
		
		JButton btnBilan = new JButton("Bilan");
		btnBilan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				(new BilanGenerate(controler)).show();
			}
		});
		right.add(btnBilan);

		Component horizontalStrut_4 = Box.createHorizontalStrut(10);
		right.add(horizontalStrut_4);

		// Contenu global
		JScrollPane content = new JScrollPane();
		add(content, BorderLayout.CENTER);
		content.setBorder(null);
		content.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		content.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE);
		content.setViewportView(contentPanel);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		JPanel bottom = new JPanel();
		add(bottom, BorderLayout.SOUTH);
		bottom.setBackground(cGreen);

		// populate(contentPanel);
	}

	public void setFilter(Category cat) {
		currentCatFilter = (cat==null) ? all : cat;
		currentFilter.setText(currentCatFilter.getName());
	}

	@Override
	public void update(Observable o, Object arg) {
		//Si on reçoit une notification, c'est que la liste des tâches a été modifiée, on repeuple les tâches
		if(o instanceof Manager)
			populate(((Manager) o).tasks);
	}

	public void setSelectedCategory(Category category) {
		setFilter(category);
		populate(controler.getTasks());
	}
}

