package core;
import java.util.Date;

public class LongTask extends Task {
	public LongTask(String name, String description, Date dueDate) {
		super(name, description, dueDate);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;
	
	private int percent = 0;
	private Date begin;

	@Override
	public Types getType() {
		return Task.Types.LONG_TASK;
	}

	@Override
	public boolean isDone() {
		return percent==100;
	}
	
	public void setPercent(int percent){
		if(percent <= this.percent)
			//TODO : Throw exception here
		this.percent = percent;
	}

	@Override
	public boolean isLate(){
		long sBegin = begin.getTime();
		long sEnd = dueDate.getTime();
		long sNow = (isDone() ? actuallyFinishedDate : today()).getTime() % (24*60*60*1000);
		long sQuarter = (sEnd - sBegin)/4;
		int percentQuarter = percent/25 + 1;
		
		long currentStop = sBegin + percentQuarter * sQuarter;
		
		return currentStop < sNow;
	}

	@Override
	public void markAsDone() {
		setPercent(100);
	}
}
