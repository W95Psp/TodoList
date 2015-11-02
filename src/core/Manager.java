package core;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;

@SuppressWarnings("unused")
public class Manager implements Serializable {
	private static final long serialVersionUID = 1L;
	private String storageLocation;

	public class TasksList extends java.util.ArrayList<Task>{
		private static final long serialVersionUID = 1L;
	}
	public class CategoryList extends java.util.ArrayList<Category>{
		private static final long serialVersionUID = 1L;
	}

	public TasksList tasks;
	public CategoryList categories;
	
	public Manager(String storageLocation){
		this.storageLocation = storageLocation;
		this.tasks = new TasksList();
		this.categories = new CategoryList();
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
	
	public static Manager createFromFile(String path){
		File f = new File(path);
        if(!f.exists())
        	return new Manager(path);
		
		try
        {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Manager o = (Manager) in.readObject();
            in.close();
            fileIn.close();
            return o;
        }
        catch(IOException i)
        {
            i.printStackTrace();
            System.exit(-1);
        }
		catch(ClassNotFoundException c)
        {
            c.printStackTrace();
            System.exit(-1);
        }
		return null;
	}
	
	private void sumup(){
		
	}
}
