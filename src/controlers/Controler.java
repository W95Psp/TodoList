package controlers;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import gui.MainWindow;
import core.Category;
import core.LongTask;
import core.Manager;
import core.Manager.CategoryList;
import core.Manager.TasksList;
import core.ShortTask;
import core.Task;
import exceptions.BadDateInput;
import exceptions.BadName;
import exceptions.IncorrectDateRange;
import exceptions.IncorrectPercent;

/*
 * Link model and view
 * J'ai fait qu'un seul contr�leur, pusique l'application n'est pas trop complexe
 */
public class Controler {
	MainWindow view;
	Manager model;
	//On link le mod�le
	public Controler(Manager model){
		this.model = model;
	}
	/*
	 * Il faut appeler cette m�thode avant d'utiliser la classe.
	 * Permet de lier la vue.
	 */
	public void bindInterface(MainWindow window) {
		this.view = window;
		model.addObserver(window);
		model.NotifyCatListEdition();
		model.NotifyTaskListEdition();
	}
	/*
	 * Force save
	 */
	public void saveData() {
		model.save();
	}
	/*
	 * Ouvre une fen�tre pour demander � l'utilisateur de cr�er une t�che
	 */
	public void askUserANewTask() {
		askUserANewTask(null);
	}
	/*
	 * Ouvre une fen�tre pour demander � l'utilisateur de cr�er une t�che avec une pr�selection de cat�gorie
	 * @param defaultCategory la cat�gorie par d�faut � afficher
	 */
	public void askUserANewTask(Category defaultCategory) {
		gui.NewTask form = new gui.NewTask(this, defaultCategory);
		form.show();
	}
	/*
	 * Supprime une cat�gorie
	 */
	public void removeCategory(Category cat) {
		for (Task task : model.tasks)
			if(task.getCategory()!=null && task.getCategory().equals(cat))
				task.removeCategory();
		model.categories.remove(cat);
		model.NotifyCatListEdition();
	}
	/*
	 * Filtre les t�ches dans la vue principale
	 * @param la cat�gorie � afficher (null = pas de filtre)
	 */
	public void setCurrentViewedCategory(Category category) {
		view.setSelectedCategory(category);
	}
	/*
	 * Ajoute une cat�gorie
	 */
	public void addCategory(String catName) {
		try {
			model.categories.add(new Category(catName));
			model.NotifyCatListEdition();
		} catch (BadName e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Permet une actualisation manuelle des taches/cat�gories
	 */
	public void NotifyTaskListEdition(){
		model.NotifyTaskListEdition();
	}
	public void NotifyCatListEdition(){
		model.NotifyCatListEdition();
	}
	
	/*
	 * Supprime une t�che et transmet le message (= actualise la vue)
	 * @param task la t�che � supprimer
	 */
	public void removeTask(Task task) {
		model.tasks.remove(task);
		model.NotifyTaskListEdition();
	}
	
	/*
	 * Retourne la liste des cat�gories (� utiliser - de pr�f�rence - en lecture seule)
	 */
	public CategoryList getCategories() {
		return model.categories;
	}
	/*
	 * Retourne la liste des t�ches (� utiliser - de pr�f�rence - en lecture seule)
	 */
	public TasksList getTasks() {
		return model.tasks;
	}
	
	/*
	 * Renomme une cat�gorie
	 */
	public void setCategoryName(Category category, String text) {
		try {
			category.rename(text);
			for (Task task : model.tasks) {
				if(category.equals(task.getCategory())){
					task.notifyChange(null);
				}
			}
		} catch (BadName e) {
			//Si le nom de la cat�gorie est incorrect
			view.soundIncorrectInput();
		}
	}
	/*
	 * Affecte un nouveau pourcentage
	 */
	public void setLongTaskPercent(LongTask task, int percent) {
		try {
			task.setPercent(percent);
		} catch (IncorrectPercent e) {
			view.soundIncorrectInput();
		}
	}
	/*
	 * Renomme une t�che
	 */
	public void setTaskName(Task task, String text) {
		try {
			task.rename(text);
		} catch (BadName e) {
			view.soundIncorrectInput();
		}
	}
	/*
	 * Ajoute une t�che
	 */
	public void addTask(String title, String desc, Date due, Category category) {
		try {
			addTask(new ShortTask(title, desc, due), category);
		} catch (BadDateInput | BadName e) {
			view.soundIncorrectInput();
		}
	}
	/*
	 * Ajoute une t�che
	 */
	public void addTask(String title, String desc, Date start, Date due, Category category) {
		try {
			addTask(new LongTask(title, desc, start, due), category);
		} catch (BadDateInput | BadName | IncorrectDateRange e) {
			view.soundIncorrectInput();
		}
	}
	/*
	 * Ajoute une t�che
	 */
	private void addTask(Task task, Category category){
		task.setCategory(category);
		model.tasks.add(task);
		model.NotifyTaskListEdition();
	}
	/*
	 * marque une t�che comme termin�e
	 */
	public void markTaskAsDone(Task task) {
		task.markAsDone();
		model.NotifyTaskListEdition();
	}
	/*
	 * Retourne la vue actuelle
	 */
	public MainWindow getMainView() {
		return view;
	}
	public void generateHTML(Date from, Date to) {
		Object[] tasks = model.tasks.stream().filter(
				(o) -> ((Task) o).getEndDate().after(from) && ((Task) o).getEndDate().before(to)
			).toArray();
		
		//On �cris le style
		String output = "<style>";
		output += "*{font-family: 'Segoe UI', 'Sans';}body{text-align: center;}";
		output += "div.title{font-size: 20px;font-weight: bold;}";
		output += "div.task{padding: 2px; margin: 8px; margin-bottom: 22px;width: 220px; display: inline-block;}";
		output += "div.description:before{content: 'Description : \"';font-weight: bold;}";
		output += "div.description:after{content: '\"';font-weight: bold;}";
		output += "div.description{margin-left: 10px;margin-right: 10px;font-size: 9px;color: gray;}";
		output += "div.category{float: right;font-size: 8px;color: gray;}";
		output += "div.state:before{content: 'Etat : ';}";
		output += "</style>";

		int numberFinshedInTime = 0;
		int numberFinshedInLate = 0;
		int numberNotFinishedButInTime = 0;
		int numberNotFinishedAndLate = 0;
		int total = 0;
		for (Object task : tasks) {
			Task t = ((Task)task);
			if(t.isDone()){
				if(t.isLate())
					numberFinshedInLate++;
				else
					numberFinshedInTime++;
			}else{
				if(t.isLate())
					numberNotFinishedAndLate++;
				else
					numberNotFinishedButInTime++;
			}
			total++;
		}

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		output += "<div class='sumup'>"
				+ "<div class='global'>"
				+ "<h1>Bilan des t�ches</h1>"
				+ "<h2>Du "+format.format(from)+" au "+format.format(to)+"</h2>"
				+ "</div>"
				+ "<div class='stats'>"
				+ "<div><b>Nombre de t�ches au total : </b>"+total+"</div>"
				+ "<div><b>Nombre de t�ches finies sans retard : </b>"+numberFinshedInTime+"</div>"
				+ "<div><b>Nombre de t�ches finies avec retard : </b>"+numberFinshedInLate+"</div>"
				+ "<div><b>Nombre de t�ches restantes avec retard : </b>"+numberNotFinishedButInTime+"</div>"
				+ "<div><b>Nombre de t�ches restantes sans retard : </b>"+numberNotFinishedAndLate+"</div>"
				+ "</div>"
				+ "</div><br/><hr><br/>";
		
		
		//On �cris les t�ches qui ont une date de fin incluse dans l'interval sp�cifi�
		for (Object task : tasks) {
			output += ((Task)task).toHTML();
		}
		PrintWriter out;
		try {
			out = new PrintWriter("bilan.html");
			out.println(output);
			out.close();
		} catch (FileNotFoundException e) {
			view.soundIncorrectInput();
			e.printStackTrace();
		}
	}
}