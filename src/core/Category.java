package core;

import java.io.Serializable;
import java.util.Observable;

import exceptions.BadName;

public class Category extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	
	/*
	 * Repr�sente une cat�gorie
	 * 
	 * @param	name	le nom (non nul) de la cat�gorie
	 */
	public Category(String name) throws BadName{
		rename(name);
	}
	
	/*
	 * @return	le nom de la cat�gorie
	 */
	public String getName(){
		return name;
	}
	/*
	 * Renomme une cat�gorie
	 * @param	value	le nouveau nom pour la cat�gorie
	 */
	public void rename(String value) throws BadName{
		if(value.equals(""))//Nom vides interdis
			throw new exceptions.BadName();
		name = value;
		//On informe les observeurs (= les JPanels concern�s)
		setChanged();
		notifyObservers(this.name);
	}

	/*
	 * Equivalent � o.getName()
	 */
	public String toString(){
		return getName();
	}
}
