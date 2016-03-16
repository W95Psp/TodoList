package core;
import java.io.Serializable;
import java.util.Observable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;

import exceptions.BadName;

/*
 * Cette classe g�re toutes les donn�es
 */
public class Manager extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;
	private String storageLocation;

	/*
	 * On d�finit deux classes TasksList et CategoryList pour repr�senter des listes sp�cifiques
	 */
	public class TasksList extends java.util.ArrayList<Task>{
		private static final long serialVersionUID = 1L;
	}
	public class CategoryList extends java.util.ArrayList<Category>{
		private static final long serialVersionUID = 1L;
	}

	public TasksList tasks;
	public CategoryList categories;
	
	//When you add/remove task/cat
	//Au lieu de m'emb�ter � impl�menter un r�le observable pour une arraylist, j'introduis un m�canisme d'observation manuel
	//Etant donn� qu'on ne modifie ces listes que � de rares endroits, �a ne m'a pas paru assez pour faire quelque chose de plus joli
	public void NotifyTaskListEdition(){
		setChanged();
		notifyObservers("tasks");
	}
	public void NotifyCatListEdition(){
		setChanged();
		notifyObservers("cat");
	}
	
	public Manager(String storageLocation){
		this.storageLocation = storageLocation;
		this.tasks = new TasksList();
		this.categories = new CategoryList();
		//On cr�e des cat�gories par d�faut :
		try {
			this.categories.add(new Category("Travail"));
			this.categories.add(new Category("Personnel"));
		} catch (BadName e) {}
		//On actualise l'interface en le notifiant
		NotifyTaskListEdition();
		NotifyCatListEdition();
	}
	
	public void save(){
		try
		{
			FileOutputStream fileOut = new FileOutputStream(storageLocation);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		}catch(IOException i)
		{
			i.printStackTrace();
		}
	}
	/*
	 * Permet de lire un fichier existant, le cas �ch�ant d'en cr�er un vierge
	 * TODO: Cat�gories par d�faut ?
	 */
	public static Manager createFromFile(String path){
		File f = new File(path);
		
		//Si le fichier n'est pas trouv�, on le cr�e
        if(!f.exists())
        	return new Manager(path);
		
		try
        {
			//On charge le fichier
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Manager o = (Manager) in.readObject();
            in.close();
            fileIn.close();
            return o;
        }
        catch(ClassNotFoundException|IOException i)
        {
        	//Erreur : le fichier est corrompu !
        	System.out.println("Fichier corrompu ! Supprimez le fichier \""+path+"\".");
        	//On quite le programme
        	System.exit(-1);
        }
		return null;
	}
}
