import java.text.ParseException;
/**
 * 
 */
public class Buyer extends User {
	
	UserBound boundary;
	Buyer thisOne;
	/**
	 * Constructor of the Buyer
	 * @param username
	 * @param password
	 * @param id
	 * @throws ParseException
	 */
	public Buyer(String username, String password, int id) throws ParseException {
		super(username,password,id);
		boundary = new BuyerBound(this);
	}
	/**
	 * @return user boundary class
	 */
	@Override
	public UserBound getBoundary()
	{
		return boundary;
	}
	
	@Override
	public String getUsername() {
		return this.username;
	}

}
