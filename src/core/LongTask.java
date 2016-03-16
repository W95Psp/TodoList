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
	 * Crée une tâche de type longue durée (début + fin)
	 * La date de fin doit être inférieure à la date de début
	 * @param name Titre de la tâche
	 * @param description Description de la tâche
	 * @param startDate la date de départ
	 * @param dueDate la date d'échéance (<startDate !)
	 */
	public LongTask(String name, String description, Date startDate, Date dueDate) throws BadDateInput, BadName, IncorrectDateRange {
		super(name, description, dueDate);
		if(false && startDate.before(Task.today()))//Pas possible de faire de tâche avant aujoud'hui
			throw new exceptions.BadDateInput();
		if(dueDate.before(startDate))//Pas possible qu'une tâche finisse avant qu'elle débute
			throw new exceptions.IncorrectDateRange();
		//On affecte la date
		begin = startDate;
		//Par défaut, la tâche n'est pas commencée
		percent = 0;
	}


	/*
	 * Affecte la date de départ
	 * @param start Date en question
	 */
	public void setStartDate(Date start) throws BadDateInput, IncorrectDateRange {
		if(false && start.before(Task.today()))//Pas possible de faire de tâche avant aujoud'hui
			throw new exceptions.BadDateInput();
		if(dueDate.before(start))//Pas possible qu'une tâche finisse avant qu'elle débute
			throw new exceptions.IncorrectDateRange();
		begin = start;
	}

	/*
	 * (non-Javadoc)
	 * @see core.Task#getType()
	 */
	@Override
	public Types getType() {
		//On retourne LONG_TASK (permet de différencier short/long)
		return Task.Types.LONG_TASK;
	}

	/*
	 * (non-Javadoc)
	 * @see core.Task#isDone()
	 */
	@Override
	public boolean isDone() {
		//Ici, une tâche et finie ssi sont taux d'accomplissement est de 100%
		return percent==100;
	}
	
	/*
	 * Obtient la date de début
	 */
	public Date getStartDate(){
		return begin;
	}
	
	/*
	 * Affecte le pourcentage d'accomplissement actuel de la tâche
	 */
	public void setPercent(int percent) throws IncorrectPercent{
		if((percent <= this.percent && false) || percent > 100)//on vérifie la cohérence du pourcentage
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
		//Si il y a du retard dans la tâche, alors la dernière étape validée est avant aujourd'hui
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
			// Si se produit, ça veut dire que le pourcentage est déjà à 100% donc markAsDone fait le boulot
		}
	}

	/*
	 * Retourne le taux d'accomplissement de la tâche, en pourcentage
	 */
	public int getPercent() {
		return percent;
	}
	/*
	 * Retourne la date de la prochaine échéance
	 */
	public Date getCurrentMilestoneUsingPercent(){
		//percent/25 est casté en int donc on a bien la date de la dernière étape validée
		return getDateMilestone(percent/25);
	}
	/*
	 * Retourne la date de la nième échéance
	 * @param n quelle échéance
	 */
	public Date getDateMilestone(int n){ //n \in [0, 4] \cap \mathbb{N}
		//Pour manipuler facilement les dates, on passe par des timestamps
		long sBegin = begin.getTime();
		long sEnd = dueDate.getTime();
		long sQuarter = (sEnd - sBegin)/4;//Incrément
		
		return new Date(sBegin + sQuarter*n);//Début + delta/4 * étape produit bien ce que l'on cherche
	}

	/*
	 * Retourne la prochaine échéance selon la date courante
	 */
	public Date getNextMilestone() {
		int n = 0;
		//On cherche à trouver une échéance après aujoud'hui
		while(n<=4 && getDateMilestone(n).before(today()))
			n++;
		//getDateMilestone(n) est bien la première étape à réaliser après la date d'aujoud'hui
		return getDateMilestone(n);
	}
}
