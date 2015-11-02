package core;
import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class Task implements Serializable {
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected String description;
	protected Date creation;
	protected Date dueDate;
	protected Category category;
	
	protected Date actuallyFinishedDate;
	
	public Task(String name, String description, Date dueDate){
		this.name 			= name;
		this.description	= description;
		this.dueDate		= dueDate;
	}
	
	public static Date today(){
		Calendar c = new GregorianCalendar();
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    return c.getTime();
	}
	
	public enum Types{
		LONG_TASK,
		SHORT_TASK
	}
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public void rename(String newName) {
		this.name = newName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Category getCategory() {
		return category;
	}

	
	public String getCategoryName() {
		return "Not there yet";
	}

	public void setCategory(Category cat) {
		category = cat;
	}

	public void removeCategory() {
		category = null;
	}
	

	public Date getCreationDate() {
		return creation;
	}

	public Date getEndDate() {
		return dueDate;
	}

	public void setEndDate(Date d) {
		dueDate = d;
	}

	abstract public Types getType();
	
	abstract public boolean isDone();
	abstract public boolean isLate();
	
	abstract public void markAsDone();
}
