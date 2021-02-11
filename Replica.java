import java.io.IOException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jgroups.JChannel;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;

public class Replica {

    private KeyPair keyPair = null;
    private PrivateKey privateKey;
    private PublicKey publicKey;


    private List<Auction> items = new ArrayList<>();
    private ArrayList<Auction> itemsWon = new ArrayList<>();
    private HashMap<PublicKey,byte[]> dataMap = new HashMap<>();
    private ArrayList<Integer> userIds = new ArrayList<>();

    // static itemId is assigned to just created AuctionItem
    private Integer itemId = 0;

    private JChannel jChannel;
    private RpcDispatcher rpcDispatcher;
    private RequestOptions requestOptions;

    public static void main(String[] args) throws Exception {
        new Replica();
    }
    /**
     * This is a constructor for replica 
     * It initiates the connection to the jGroup channel and gets data from other nodes
     * @throws Exception
     */
    public Replica() throws Exception {
        jChannel = new JChannel();
        jChannel.connect("Cluster");
        jChannel.setDiscardOwnMessages(true);
        rpcDispatcher = new RpcDispatcher(jChannel, this);
        requestOptions = new RequestOptions(ResponseMode.GET_ALL, 1000);
        RspList rspList = rpcDispatcher.callRemoteMethods(null, "getExistingData", null, null, requestOptions);

        List results = rspList.getResults();

        if(results.size() != 0){
            this.assignData(results);
       }
    }
    /**
     * Get data from other replicas
     * @return object with data
     */
    public HashMap<String, Object> getExistingData()
    {
        HashMap<String, Object> updateData = new HashMap<>();

        ArrayList<Auction> itemsArr = new ArrayList<>();
        itemsArr.addAll(items);
        updateData.put("auctions", itemsArr);

        ArrayList<Auction> itemsWonArr = new ArrayList<>();
        itemsWonArr.addAll(itemsWon);
        updateData.put("auctionsWon", itemsWonArr);

        HashMap<PublicKey,byte[]> dataKeyMap= new HashMap<>();
        dataKeyMap.putAll(dataMap);
        updateData.put("dataMap", dataKeyMap);

        KeyPair pair;
        pair = keyPair;
        updateData.put("keyPair", pair);

        Integer integer;
        integer = itemId;
        updateData.put("itemId", integer);

        ArrayList<Integer> ids = new ArrayList<>();
        ids.addAll(userIds);
        updateData.put("userIds", ids);


        return updateData;
    }

    private void assignData(List list){
        HashMap response = (HashMap) list.get(0);

        ArrayList itemsArr = (ArrayList) response.get("auctions");
        items.addAll(itemsArr);

        ArrayList itemsWonArr = (ArrayList) response.get("auctionsWon");
        itemsWon.addAll(itemsWonArr);

        HashMap dataKeyMap= (HashMap) response.get("dataMap");
        dataMap.putAll(dataKeyMap);

        KeyPair pair = (KeyPair) response.get("keyPair");
        keyPair = pair;

        Integer integer = (Integer) response.get("itemId");
        itemId = integer;

        ArrayList ids = (ArrayList) response.get("userIds");
        userIds.addAll(ids);

    }

    /**
     * This method creates the auction for the item or to be more presise it
     * initiates the item on the auction.
     * 
     * @param auctionItem - encrypted AuctionItem is created at client side and
     *                    passed here to be launched at auction
     */
    public synchronized int createAuctionRequest(String itemTitle, String itemDescription, int ownerId, double reservePrice, double startingPrice) {
        if(userIds.contains((Integer) ownerId)){
            try {
                //AuctionItem itt = Util.decryptItem(auctionItem);
                Auction auction = new Auction(ownerId, itemTitle, itemDescription, reservePrice, startingPrice);
                auction.setAuctionId(++itemId);
                items.add(auction);
                System.out.println("Auction added");
            } catch (Exception e) {
                System.out.println(e);
            }
    
            return itemId;
        }
        System.out.println("Unauthorised action! User not logged in!");
        return 0;
    }
    /**
     * Shows active auctions.
     * 
     * @return activeAuctions is the array of the Strings that contain info about
     *         active auctions
     */
    public ArrayList<String> showActiveAuctionsRequest() {
        ArrayList<String> activeAuctions = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            activeAuctions.add(items.get(i).getAuctionInfo());
        }
        return activeAuctions;
    }
    /**
     * This method shows owner's auctions
     * 
     * @param ownerId
     * @return activeAuctions is the array that contains info about active auctions
     */
    public ArrayList<String> showAuctionsByIdRequest(int ownerId) {
        ArrayList<String> activeAuctions = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getOwnerId() == ownerId)
                activeAuctions.add(items.get(i).getAuctionInfo() + "Reserve price:" + items.get(i).getReservePrice()
                        + "\n---------------------");
        }
        return activeAuctions;
    }
    /**
     * @param ownerId
     * @return owner's auctions that have been already sold
     */
    public ArrayList<String> showSoldAuctionsByIdRequest(int ownerId) {
        ArrayList<String> soldAuctions = new ArrayList<>();
        for (int i = 0; i < itemsWon.size(); i++) {
            if (itemsWon.get(i).getOwnerId() == ownerId || itemsWon.get(i).getHighestBidderId() == ownerId)
                soldAuctions.add(itemsWon.get(i).getAuctionInfo()
                        + "\nWinner details: \n" + itemsWon.get(i).getHighestBidderName() + "\n"
                        + itemsWon.get(i).getHighestBidderEmail() + "\n---------------------");
        }
        return soldAuctions;
    }
    /**
     * This method is closing auction of the single item
     * 
     * @param ownerId the auction's owner id
     * @param itemId  id of the item that owner wants to be closed
     */
    public String closeAuctionRequest(int ownerId, int itemId)
            throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException {
        System.out.println("--Closing auction: " + itemId);
        String result = "ERROR! The item you want to remove does not exist!";
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getAuctionId() == itemId) {
                result = "ERROR! You cannot close an auction which is not yours!";
                if (items.get(i).getOwnerId() == ownerId) {
                    if (items.get(i).getCurrentBid() > 0
                            && items.get(i).getCurrentBid() >= items.get(i).getReservePrice() && items.get(i).getHighestBidderEmail() != "") {
                        System.out.println("--Item sold");
                        result = "Item sold!\nBuyer contact: " + items.get(i).getHighestBidderEmail() + "\n";
                        Storage.appendToTxt(
                                new String[] { items.get(i).getAuctionInfo(), "\nAuction closed", "\nItem sold!",
                                        "\n" + items.get(i).getHighestBidderName() + " won the item, congrats!    ",
                                        java.time.LocalDate.now().toString() },
                                "history.txt");
                        itemsWon.add(items.get(i));
                    } else {
                        System.out.println("--Item not sold");
                        result = "Item not sold!\n";
                        if(items.get(i).getCurrentBid() < items.get(i).getReservePrice()){
                            System.out.println("--Reserve has not been reached");
                            result+="--Reserve has not been reached\n";
                        }
                        Storage.appendToTxt(new String[] { items.get(i).getAuctionInfo(), "\nAuction closed",
                                "\nItem not sold!", java.time.LocalDate.now().toString() }, "history.txt");
                    }

                    items.remove(i);
                    System.out.println("->Auction closed successfully");
                    return result + "Auction closed successfully!";
                }
            }
        }
        System.out.println("--ERROR");
        return result;
    }
    /**
     * This method allows to bid active auctions
     * 
     * @param fullName
     * @param bidderId
     * @param itemId
     * @param email
     * @param price
     */
    public String bidRequest(String email, String fullName, int bidderId, int itemId, double price) {
        String result = "ERROR! Item that you want to bid is not available!";
        for (Auction x : items) {
            if (x.getAuctionId() == itemId) {
                synchronized(x){
                    result = "ERROR! Current bid is not smaller than your price!";
                    if (price > x.getCurrentBid()) {
                        x.setCurrentBid(price);
                        x.setHighestBidderEmail(email);
                        x.setHighestBidderName(fullName);
                        x.setHighestBidderId(bidderId);
                        return "Your price is the highest! Congrats! :)";
                    }
                }
            }
        }
        return result;
    }
    /**
     * This method is used for server verification
     * @throws IOException,NoSuchAlgorithmException,SignatureException
     * @param msg message from user - to be signed by server
     * @return signature - data signed with the private key
     */
    public byte[] challengeRequest(byte[] msg) throws IOException, NoSuchAlgorithmException, SignatureException {

        if (keyPair == null) {
            System.out.println("--Generating keys");
            keyPair = Util.generateKeys();
        }
        // Load key
        System.out.println("--Loading the keys");

        try {
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // Creating a Signature object
        Signature sign = Signature.getInstance("SHA256withRSA");
        try {
            sign.initSign(privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        // Adding data to the signature
        sign.update(msg);
        // Signing data with private key
        byte[] signature = sign.sign();
        System.out.println("--Returning signed data");
        return signature;
    }
    /**
     * This method is invoked twice in order to auth the user.
     * @throws SignatureException,NoSuchAlgorithmException
     * @param msg data from user
     * @param pubKey the user's public key
     * @return data to be encrypted by user or null in case that werification went alright
     */
    public byte [] loginToServerRequest(byte [] msg, PublicKey pubKey) throws SignatureException, NoSuchAlgorithmException
    {
        // challenge is randomly generated 
        byte [] challenged = null;
        // null means that users wants to log in and waits for the challenge
        if(msg==null) {
            System.out.println("--Login attempt\n--Generating challenge ");
            challenged = Util.generateRandomString().getBytes();
            dataMap.put(pubKey, challenged);
            return challenged;
        }else{
            // get message from server signed by its private key
            Signature sig = Signature.getInstance("SHA256withRSA");
            try {
                sig.initVerify(pubKey);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
            sig.update(dataMap.get(pubKey));
            // if verification went ok then null is returned
            if(sig.verify(msg)){
                System.out.println("-> User verified!");
                return null;
            }
            
        }
        return challenged;
    }
    public void saveUserRequest(int userId){
        if(!userIds.contains((Integer) userId)){
            userIds.add((Integer) userId);
        }
        System.out.println("User with id: "+userId+" has been logged in!");
    }
    /**
     * This method retrieves public key from previously created file.
     * @return public key
     * @throws Exception
     */
    public PublicKey getPublicRequest() throws Exception {
        // byte[] keyBytes = Files.readAllBytes(new File("publicKey_server").toPath());
        // X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        // KeyFactory kf = KeyFactory.getInstance("RSA");
        return keyPair.getPublic();
    }

}
