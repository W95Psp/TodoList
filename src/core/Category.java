package core;

import java.io.Serializable;
import java.util.Observable;

import exceptions.BadName;

public class Category extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	
	/*
	 * Représente une catégorie
	 * 
	 * @param	name	le nom (non nul) de la catégorie
	 */
	public Category(String name) throws BadName{
		rename(name);
	}
	
	/*
	 * @return	le nom de la catégorie
	 */
	public String getName(){
		return name;
	}
	/*
	 * Renomme une catégorie
	 * @param	value	le nouveau nom pour la catégorie
	 */
	public void rename(String value) throws BadName{
		if(value.equals(""))//Nom vides interdis
			throw new exceptions.BadName();
		name = value;
		//On informe les observeurs (= les JPanels concernés)
		setChanged();
		notifyObservers(this.name);
	}

	/*
	 * Equivalent à o.getName()
	 */
	public String toString(){
		return getName();
	}
}
