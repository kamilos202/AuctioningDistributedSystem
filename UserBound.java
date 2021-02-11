import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * This is the boundary class for User class
 */
public class UserBound {


	public void doThings() throws IOException, ParseException, NotBoundException, ClassNotFoundException {
		 
	}

	protected void seeActiveAuctions()
			throws InvalidKeyException, RemoteException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException, IOException {
		System.out.println("The list of active auctions:\n");
		if(Client.stub.showActiveAuctions().isEmpty()){
			System.out.println("NO ACTIVE AUCTIONS");
		}
		for(String s : Client.stub.showActiveAuctions()){
			System.out.println(s);
		}

	}

	protected void seeSoldAuctions(int userId) throws Exception {
		if(Client.stub.showSoldAuctionsById(userId).isEmpty()){
			System.out.println("NO SOLD ITEMS");
		}else{
			for(String s : Client.stub.showSoldAuctionsById(userId)){
				System.out.println(s);
			}
		}

	}
}