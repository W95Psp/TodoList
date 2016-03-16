package core;
import java.util.Date;

import exceptions.BadDateInput;
import exceptions.BadName;
import exceptions.IncorrectDateRange;
import exceptions.IncorrectPercent;

public class LongTask extends Task {
	private static final long serialVersionUID = 1L;
	
	private int percent = 0;
	private Date begin;

	/*
	 * Cr�e une t�che de type longue dur�e (d�but + fin)
	 * La date de fin doit �tre inf�rieure � la date de d�but
	 * @param name Titre de la t�che
	 * @param description Description de la t�che
	 * @param startDate la date de d�part
	 * @param dueDate la date d'�ch�ance (<startDate !)
	 */
	public LongTask(String name, String description, Date startDate, Date dueDate) throws BadDateInput, BadName, IncorrectDateRange {
		super(name, description, dueDate);
		if(false && startDate.before(Task.today()))//Pas possible de faire de t�che avant aujoud'hui
			throw new exceptions.BadDateInput();
		if(dueDate.before(startDate))//Pas possible qu'une t�che finisse avant qu'elle d�bute
			throw new exceptions.IncorrectDateRange();
		//On affecte la date
		begin = startDate;
		//Par d�faut, la t�che n'est pas commenc�e
		percent = 0;
	}


	/*
	 * Affecte la date de d�part
	 * @param start Date en question
	 */
	public void setStartDate(Date start) throws BadDateInput, IncorrectDateRange {
		if(false && start.before(Task.today()))//Pas possible de faire de t�che avant aujoud'hui
			throw new exceptions.BadDateInput();
		if(dueDate.before(start))//Pas possible qu'une t�che finisse avant qu'elle d�bute
			throw new exceptions.IncorrectDateRange();
		begin = start;
	}

	/*
	 * (non-Javadoc)
	 * @see core.Task#getType()
	 */
	@Override
	public Types getType() {
		//On retourne LONG_TASK (permet de diff�rencier short/long)
		return Task.Types.LONG_TASK;
	}

	/*
	 * (non-Javadoc)
	 * @see core.Task#isDone()
	 */
	@Override
	public boolean isDone() {
		//Ici, une t�che et finie ssi sont taux d'accomplissement est de 100%
		return percent==100;
	}
	
	/*
	 * Obtient la date de d�but
	 */
	public Date getStartDate(){
		return begin;
	}
	
	/*
	 * Affecte le pourcentage d'accomplissement actuel de la t�che
	 */
	public void setPercent(int percent) throws IncorrectPercent{
		if((percent <= this.percent && false) || percent > 100)//on v�rifie la coh�rence du pourcentage
			throw new IncorrectPercent();
		this.percent = percent;
		//On notifie les observers (certains JPanels donc)
		setChanged();
		notifyObservers(this.percent);
	}

	/*
	 * (non-Javadoc)
	 * @see core.Task#isLate()
	 */
	@Override
	public boolean isLate(){
		//Si il y a du retard dans la t�che, alors la derni�re �tape valid�e est avant aujourd'hui
		return getCurrentMilestoneUsingPercent().before(today());
	}

	/*
	 * (non-Javadoc)
	 * @see core.Task#markAsDone()
	 */
	@Override
	public void markAsDone(){
		try {
			setPercent(100);
		} catch (IncorrectPercent e){
			// Si se produit, �a veut dire que le pourcentage est d�j� � 100% donc markAsDone fait le boulot
		}
	}

	/*
	 * Retourne le taux d'accomplissement de la t�che, en pourcentage
	 */
	public int getPercent() {
		return percent;
	}
	/*
	 * Retourne la date de la prochaine �ch�ance
	 */
	public Date getCurrentMilestoneUsingPercent(){
		//percent/25 est cast� en int donc on a bien la date de la derni�re �tape valid�e
		return getDateMilestone(percent/25);
	}
	/*
	 * Retourne la date de la ni�me �ch�ance
	 * @param n quelle �ch�ance
	 */
	public Date getDateMilestone(int n){ //n \in [0, 4] \cap \mathbb{N}
		//Pour manipuler facilement les dates, on passe par des timestamps
		long sBegin = begin.getTime();
		long sEnd = dueDate.getTime();
		long sQuarter = (sEnd - sBegin)/4;//Incr�ment
		
		return new Date(sBegin + sQuarter*n);//D�but + delta/4 * �tape produit bien ce que l'on cherche
	}

	/*
	 * Retourne la prochaine �ch�ance selon la date courante
	 */
	public Date getNextMilestone() {
		int n = 0;
		//On cherche � trouver une �ch�ance apr�s aujoud'hui
		while(n<=4 && getDateMilestone(n).before(today()))
			n++;
		//getDateMilestone(n) est bien la premi�re �tape � r�aliser apr�s la date d'aujoud'hui
		return getDateMilestone(n);
	}
}
