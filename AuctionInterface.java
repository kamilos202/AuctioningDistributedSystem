import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/**
 * Remote interface
 */
public interface AuctionInterface extends Remote{
    //public SealedObject getSpec(int itemId, SealedObject clientRequest) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException ;
    public int createAuction(String itemTitle, String itemDescription, int ownerId, double reservePrice, double startingPrice) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException ;
    public ArrayList<String> showActiveAuctions() throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException ;
    public ArrayList<String> showAuctionsById(int ownerId) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException ;
    public String closeAuction(int ownerId, int itemId) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException ;
    public String bid(String email, String fullName, int bidderId, int itemId, double price) throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException ;  
    public ArrayList<String> showSoldAuctionsById(int id) throws Exception;
    public void saveUser(int userId) throws Exception;

}
