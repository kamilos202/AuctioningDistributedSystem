import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
/**
 * Storage class takes care of txt files interaction
 */
public class Storage implements IStorage {

	public static ArrayList<User> users = new ArrayList<User>();
	/**
	 * This method loads users from txt file
	 * @throws NumberFormatException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static void populateUsers()
			throws NumberFormatException, MalformedURLException, RemoteException, NotBoundException
	{
		List<String> usersInDataBase = readFile("users.txt");
		for(int i =1;i<usersInDataBase.size();i++)
		{
			users.add(UserControl.getUserFromString(usersInDataBase.get(i)));
		}
		
	}
	
	public static void writeToTxt(String[] lines, String path)
	{
		try {IStorage.writeToTxt(lines, path);} 
		catch (IOException e) {	e.printStackTrace();}
	}
	
	public static void appendToTxt(String[] lines, String path) throws IOException
	{
		IStorage.appendToTxt(lines, path);
	}
	
	public static Boolean ifExists(String file){
		return IStorage.ifExists(file);
	}

	public static List<String> readFile(String path)
	{
		return IStorage.readFromTxt(path);
	}
	/**
	 * This method retrieves users' data from file
	 * @param username
	 * @return
	 */
	public static String getUserDetails(String username)
	{
		List<String> users = readFile("users.txt");
		for(int i = 0;i<users.size();i++) 
		{
			System.out.println("getUsersDetails in Storage:   "+users.get(i).split(";")[0]);
			if(users.get(i).split(";")[0].equals(username)) return users.get(i);
		}
		return null;
	}

	public static void createEmptyTxtFile(String fileName) throws IOException {
		IStorage.createEmptyTxtFile(fileName);
	}

}
