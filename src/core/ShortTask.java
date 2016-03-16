package core;

import java.util.Date;

import exceptions.BadDateInput;
import exceptions.BadName;

public class ShortTask extends Task {
	public ShortTask(String name, String description, Date dueDate) throws BadDateInput, BadName {
		super(name, description, dueDate);
	}

	private static final long serialVersionUID = -1036270876973695371L;
	
	/*
	 * (non-Javadoc)
	 * @see core.Task#getType()
	 */
	@Override
	public Types getType() {
		return Task.Types.SHORT_TASK;
	}

	/*
	 * (non-Javadoc)
	 * @see core.Task#isDone()
	 */
	@Override
	public boolean isDone() {
		return actuallyFinishedDate!=null;
	}

	/*
	 * (non-Javadoc)
	 * @see core.Task#isLate()
	 */
	@Override
	public boolean isLate() {
		//Une tâche courte en retard est :
		//   si elle est finie, est ce que la date de fin effective est après la date de fin planifiée
		//   sinon, est ce que aujourd'hui est après la date de fin planifiée
		return (isDone() ? actuallyFinishedDate : today()).after(dueDate);// Si identique, tout marche bien, renvoie false
	}
	
	/*
	 * (non-Javadoc)
	 * @see core.Task#markAsDone()
	 */
	@Override
	public void markAsDone(){
		actuallyFinishedDate = today();
		setChanged();
		notifyObservers(this.actuallyFinishedDate);
	}
}
