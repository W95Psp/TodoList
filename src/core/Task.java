package core;
import exceptions.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observable;

public abstract class Task extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected String description;
	protected Date creation;
	protected Date dueDate;
	protected Category category;
	
	protected Date actuallyFinishedDate;
	
	/*
	 * Crée une tâche
	 */
	public Task(String name, String description, Date dueDate) throws BadDateInput, BadName{
		if(false && dueDate.before(Task.today()))//On vérifie que la date est après aujourd'hui
			throw new exceptions.BadDateInput();
		if(name.equals(""))//Et que le nom n'est pas nul
			throw new exceptions.BadName();
		this.name 			= name;
		this.description	= description;
		this.dueDate		= dueDate;
	}
	
	/*
	 * Retourne la date d'aujoud'hui à minuit
	 */
	public static Date today(){
		Calendar c = new GregorianCalendar();
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    return c.getTime();
	}
	
	/*
	 * Différents types de tâches
	 */
	public enum Types{
		LONG_TASK,
		SHORT_TASK
	}
	
	/*
	 *Obtient le nom de la tâche
	 */
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	/*
	 * Notifie les observateurs
	 */
	public void notifyChange(Object something){
		setChanged();
		notifyObservers(something);
	}
	public void rename(String newName) throws BadName {
		if(newName.equals(""))
			throw new exceptions.BadName();
		this.name = newName;
		notifyChange(this.name);
	}
	public void setDescription(String description) {
		this.description = description;
		notifyChange(this.description);
	}
	
	public Category getCategory() {
		return category;
	}
	public String getCategoryName() {
		if(category==null)
			return "Pas de catégorie";
		else
			return category.getName();
	}
	public void setCategory(Category cat) {
		category = cat;
		notifyChange(this.category);
	}
	public void removeCategory() {
		category = null;
		notifyChange(this.category);
	}
	public Date getCreationDate() {
		return creation;
	}
	public Date getEndDate() {
		return dueDate;
	}
	public void setEndDate(Date d) throws BadDateInput {
		if(d.before(Task.today()))
			throw new exceptions.BadDateInput();
		dueDate = d;
		notifyChange(this.dueDate);
	}

	abstract public Types getType();
	
	abstract public boolean isDone();
	abstract public boolean isLate();
	
	abstract public void markAsDone();

	public String StateToHTML(){
		String output = "";
		if(isDone())
			output = "Terminé";
		else
			output = "En cours";
		return output;
	}
	public String DateToHTML(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return "Fin le "+format.format(getEndDate());
	}
	public String toHTML(){
		String output = "<div class='task'>";
		output += "<div class='title'>" + getName() + "</div>";
		output += "<div class='end-date'>" + DateToHTML() + "</div>";
		output += "<div class='state'>" + StateToHTML() + "</div>";
		output += "<div class='description'>" + getDescription() + "</div>";
		output += "<div class='category'>" + getCategoryName() + "</div>";
		output += "</div>";
		return output;
	}
}
