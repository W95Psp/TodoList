package core;

import java.util.Date;

public class ShortTask extends Task {
	public ShortTask(String name, String description, Date dueDate) {
		super(name, description, dueDate);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -1036270876973695371L;
	
	@Override
	public Types getType() {
		return Task.Types.SHORT_TASK;
	}

	@Override
	public boolean isDone() {
		return actuallyFinishedDate!=null;
	}

	@Override
	public boolean isLate() {
		return (isDone() ? actuallyFinishedDate : today()).after(dueDate);// Si identique, tout marche bien, renvoie false
	}
	
	@Override
	public void markAsDone(){
		actuallyFinishedDate = today();
	}
}
