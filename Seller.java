import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Seller extends User {

	private UserBound boundary;
	/**
	 * Seller class constructor
	 * @param username
	 * @param password
	 * @param id
	 * @throws MalformedURLException
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public Seller(String username, String password, int id)
			throws MalformedURLException, RemoteException, NotBoundException {
		super(username,password,id);
		boundary = new SellerBound(this);
	}

	@Override
	public UserBound getBoundary()
	{
		return boundary;
	}


}
