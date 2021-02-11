import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.List;
/**
 * This is control class of User.
 * Takes care of login and register
 */
public class UserControl {

	/**
	 * Gets user from String
	 * @param userString
	 * @return
	 * @throws NumberFormatException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public static User getUserFromString(String userString)
			throws NumberFormatException, MalformedURLException, RemoteException, NotBoundException
	{
		String[] userInfo = userString.split(";");
		if(Boolean.parseBoolean(userInfo[2]))
		{
			return(new Seller(userInfo[0],userInfo[1],Integer.parseInt(userInfo[3])));
		}
		else
		{
			try { 
				return(new Buyer(userInfo[0],userInfo[1],Integer.parseInt(userInfo[3])));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	/**
	 * This method registers the user.
	 * @param username
	 * @param password
	 * @param type
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws ParseException
	 */
	public static void registerUser(String username, String password,char type)
			throws MalformedURLException, RemoteException, NotBoundException, ParseException
	{
		int id = Storage.users.size()+1;
		String typ = type == 'y' ? "true" : "false";
		String[] record = {"\n"+username+";"+password+";"+typ+";"+id};
		if(typ=="true"){
			Storage.users.add(new Seller(username,password,id));
		}else{
			Storage.users.add(new Buyer(username,password,id));
		}
		try {
			Storage.appendToTxt(record, "users.txt");
			Storage.users.add(new Buyer(username,password,id));
		} catch (Exception e) {
			System.out.println("Registration failed :(");
		}
		
	}
	/**
	 * This method logs in user
	 * @param username
	 * @param password
	 * @return user if login is correct or null in case that error occured
	 */
	public static User login(String username, String password)
	{
		if(getUserFromTxt(username).split(";")[1].equals(password))
		{
			System.out.println("Login succesful\n");
			return getUserByName(username);
		}
		else
		{
			System.out.println("ERROR: Incorrect password ");
			return null;
		}
	}
	/**
	 * Retrieves users from TXT file
	 * @param username
	 * @return
	 */
	private static String getUserFromTxt(String username)
	{
		List<String> users = Storage.readFile("users.txt");
		for(int i =0;i<users.size();i++)
		{
			if(users.get(i).split(";")[0].equals(username)) return users.get(i);
		}
		return null;
	}
	/**
	 * 
	 * @param name
	 * @return user
	 */
	private static User getUserByName(String name)
	{
		for(int i =0;i<Storage.users.size();i++)
		{
			if(Storage.users.get(i).getUsername().equals(name))
			{
				return Storage.users.get(i);
			}
		}
		return null;
	}
	/**
	 * Checks if user exists (is already registered)
	 * @param name
	 * @return
	 */
	public static boolean ifUserExists(String name)
	{
		if(getUserByName(name) == null) return false;
		return true;
	}
}
