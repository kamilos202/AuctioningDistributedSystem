import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Boundary class of Seller
 */
public class SellerBound extends UserBound {
	private User userIn;
	/**
	 * Constructor
	 * 
	 * @param seller
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * @throws RemoteException
	 */
	public SellerBound(Seller seller) throws MalformedURLException, NotBoundException, RemoteException {
		userIn = seller;
	}
	/**
	 * Here Seller performs its actions.
	 * 
	 * @throws IOException,ParseException,NotBoundException,ClassNotFoundException
	 */
	@Override
	public void doThings() throws IOException, ParseException, NotBoundException, ClassNotFoundException {
		boolean loggedIn = true;
		while (loggedIn) {
			System.out.println("\n=========== Seller Menu ===========");
			System.out.println("1: See the active auctions");
			System.out.println("2: See my active auction");
			System.out.println("3: Create the auction");
			System.out.println("4: Close the auction");
			System.out.println("5: Show my sold items");
			System.out.println("6: Logout");

			int actionChoice = Util.getValidIntegerInput();
			switch (actionChoice) {
				case 1:
					try {
						seeActiveAuctions();
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| IllegalBlockSizeException | InvalidAlgorithmParameterException
							| ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					break;
				case 2:
					try {
						seeMyActiveAuction();
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| IllegalBlockSizeException | InvalidAlgorithmParameterException e1) {
						e1.printStackTrace();
					}
					break;
				case 3:
					try {
						createAuction();
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| IllegalBlockSizeException | InvalidAlgorithmParameterException
							| ClassNotFoundException e) {
						e.printStackTrace();
					}
					break;
				case 4:
					try {
						closeAuction();
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
						e.printStackTrace();
					}
					break;
				case 5:
					try {
						seeSoldAuctions(userIn.getId());
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 6:
					loggedIn = false;
					break;
				default:
					System.out.println("Please enter one of the valid options");
					break;
			}
		}

	}
	/**
	 * This method creates Auction
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidAlgorithmParameterException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InvalidKeyException
	 */
	private void createAuction() throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			InvalidAlgorithmParameterException, ClassNotFoundException, IOException, InvalidKeyException {
		System.out.println(userIn.getUsername()+", you are creating the auction. Provide the item details");
		String title, description;
		double reservePrice, startingPrice;
		System.out.println("Give the title of your item:");
		title = Util.getValidStringInput();	
		System.out.println("Provide the describtion of the item (color, size, etc.):");		
		description = Util.getValidStringInput();
		System.out.println("What is the reserve price (if no reserve price input 0):");		
		reservePrice = Util.getValidDoubleInput();
		System.out.println("Provide the starting price of the item (color, size, etc.):");
		startingPrice = Util.getValidDoubleInput();
		
		//SealedObject object = Util.encryptItem(new AuctionItem(title, description));
		//AuctionItem item = new AuctionItem(title, description);
		int idOfAuction = Client.stub.createAuction(title, description, userIn.getId(), reservePrice, startingPrice);
		System.out.println("Auction has been created. Your unique Auction id is: "+idOfAuction);
	}
	/**
	 * This method enables the owner to see his auctions
	 * @throws InvalidKeyException
	 * @throws RemoteException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidAlgorithmParameterException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void seeMyActiveAuction()
			throws InvalidKeyException, RemoteException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException, IOException {
		for(String s : Client.stub.showAuctionsById(userIn.getId())){
			System.out.println(s);
		}
	}
	/**
	 * This method closes Auction
	 * @throws InvalidKeyException
	 * @throws RemoteException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidAlgorithmParameterException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void closeAuction()
			throws InvalidKeyException, RemoteException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException, IOException {
		System.out.println("Which auction do you want to close? (input ID of the item): ");
		int itemIDD = Util.getValidIntegerInput();
		String message = Client.stub.closeAuction(userIn.getId(),itemIDD);
		System.out.println(message);
	}
}