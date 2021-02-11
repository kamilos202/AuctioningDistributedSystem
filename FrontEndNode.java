import java.io.IOException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;

public class FrontEndNode extends ReceiverAdapter implements AuctionInterface, AuthenticationInterface {

    private RequestOptions requestOptions;
    private RpcDispatcher rpcDispatcher;
    private int numberOfServersRunning=0;

    public FrontEndNode(JChannel jChannel) {

        requestOptions = new RequestOptions(ResponseMode.GET_ALL, 2000);
        rpcDispatcher = new RpcDispatcher(jChannel, this);
        jChannel.setDiscardOwnMessages(true);
    }

    @Override
    public int createAuction(String itemTitle, String itemDescription, int ownerId, double reservePrice, double startingPrice)
            throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException {
        RspList rspList;
        List answer;

        try {
            rspList = rpcDispatcher.callRemoteMethods(null, "createAuctionRequest",
                    new Object[] { itemTitle, itemDescription, ownerId, reservePrice, startingPrice },
                    new Class[] { String.class, String.class, int.class, double.class, double.class }, requestOptions);
            answer = rspList.getResults();
            int replicasState = checkIfReplicaFailed(rspList.size());
            if(replicasState > 0){
                System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
                System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
            }
            return (int) answer.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return 0;
    }

    @Override
    public ArrayList<String> showActiveAuctions()
            throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException {
        RspList rspList;
        List answer;

        try {
            rspList = rpcDispatcher.callRemoteMethods(null, "showActiveAuctionsRequest",
                    new Object[] {},
                    new Class[] {}, requestOptions);
            answer = rspList.getResults();
            int replicasState = checkIfReplicaFailed(rspList.size());
            if(replicasState > 0){
                System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
                System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
            }
            return (ArrayList<String>) answer.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public ArrayList<String> showAuctionsById(int ownerId)
            throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException {
        RspList rspList;
        List answer;

        try {
            rspList = rpcDispatcher.callRemoteMethods(null, "showAuctionsByIdRequest",
                    new Object[] { ownerId },
                    new Class[] { int.class }, requestOptions);
            answer = rspList.getResults();
            int replicasState = checkIfReplicaFailed(rspList.size());
            if(replicasState > 0){
                System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
                System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
            }
            return (ArrayList<String>) answer.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public String closeAuction(int ownerId, int itemId)
            throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException {
        RspList rspList;
        List answer;

        try {
            rspList = rpcDispatcher.callRemoteMethods(null, "closeAuctionRequest",
                    new Object[] { ownerId, itemId },
                    new Class[] { int.class, int.class }, requestOptions);
            answer = rspList.getResults();
            int replicasState = checkIfReplicaFailed(rspList.size());
            if(replicasState > 0){
                System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
                System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
            }
            return (String) answer.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public String bid(String email, String fullName, int bidderId, int itemId, double price)
            throws RemoteException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, ClassNotFoundException {

        RspList rspList;
        List answer;

        try {
            rspList = rpcDispatcher.callRemoteMethods(null, "bidRequest",
                    new Object[] { email, fullName, bidderId, itemId, price },
                    new Class[] { String.class, String.class, int.class, int.class, double.class }, requestOptions);
            answer = rspList.getResults();
            int replicasState = checkIfReplicaFailed(rspList.size());
            if(replicasState > 0){
                System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
                System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
            }
            return (String) answer.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @Override
    public byte[] challenge(byte[] msg) throws NoSuchAlgorithmException, IOException, SignatureException {

        RspList rspList;
        List answer;
        try {
            rspList = rpcDispatcher.callRemoteMethods(null, "challengeRequest", new Object[] { msg },
                    new Class[] { byte[].class }, requestOptions);

            answer = rspList.getResults();
            int replicasState = checkIfReplicaFailed(rspList.size());
            if(replicasState > 0){
                System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
                System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
            }
            return (byte[]) answer.get(0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public byte[] loginToServer(byte[] msg, PublicKey pubKey)
            throws NoSuchAlgorithmException, IOException, SignatureException {
        RspList rspList;
        List answer;
        try {
            rspList = rpcDispatcher.callRemoteMethods(null, "loginToServerRequest", 
            new Object[] { msg, pubKey }, new Class[] { byte[].class, PublicKey.class }, requestOptions);

            answer = rspList.getResults();
            int replicasState = checkIfReplicaFailed(rspList.size());
            if(replicasState > 0){
                System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
                System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
            }
            if(answer.size() != 0){
                return (byte[]) answer.get(0);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public PublicKey getPublic() throws Exception {
        RspList rspList;
        List answer;
        
        rspList = rpcDispatcher.callRemoteMethods(null, "getPublicRequest", 
        new Object[] {}, new Class[] {}, requestOptions);

        answer = rspList.getResults();
        int replicasState = checkIfReplicaFailed(rspList.size());
        if(replicasState > 0){
            System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
            System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
        }
        return (PublicKey) answer.get(0);
    }

    @Override
    public ArrayList<String> showSoldAuctionsById(int id) throws Exception {
        RspList rspList;
        List answer;
        
        rspList = rpcDispatcher.callRemoteMethods(null, "showSoldAuctionsByIdRequest", 
        new Object[] {id}, new Class[] {int.class}, requestOptions);

        answer = rspList.getResults();

        int replicasState = checkIfReplicaFailed(rspList.size());
        if(replicasState > 0){
            System.out.println("REPLICAS FAILED - COUNT: "+replicasState);
            System.out.println("REPLICAS LEFT - COUNT: "+numberOfServersRunning);
        }
        return (ArrayList<String>) answer.get(0);
    }

    @Override
    public void saveUser(int userId) throws Exception {
        rpcDispatcher.callRemoteMethods(null, "saveUserRequest", 
        new Object[] {userId}, new Class[] {int.class}, requestOptions);
    }


    private int checkIfReplicaFailed(int requests){
        int result = 0;
        if(numberOfServersRunning>requests) result = numberOfServersRunning-requests;
        numberOfServersRunning = requests;
        return result;
    }
    
}
