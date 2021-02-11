import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/**
 * This class starts client application.
 * @author Kamil Florowski
 */
public class Client {

    private static final int PORT = 8080;

    static AuctionInterface stub;
    private static AuthenticationInterface auctInterface;
    private static KeyPairGenerator keyPairGenerator;
    private static KeyPair pair;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    private static String challenge;
    /**
     * Client program driver
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws ClassNotFoundException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     */
    public static void main(String args[])
            throws NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException,
            IllegalBlockSizeException, ClassNotFoundException, BadPaddingException, InvalidAlgorithmParameterException {
        try {

            stub = (AuctionInterface) Naming.lookup("rmi://localhost:"+PORT+"/AuctionService");
            auctInterface = (AuthenticationInterface) Naming.lookup("rmi://localhost:"+PORT+"/AuctionService");
            // Here users are retrieved from the txt file
            Storage.populateUsers();
            Boolean programOn = true;

            // while program is running user can perform its actions
            while (programOn) {
                System.out.println("What do you want to do?");
                System.out.println("1: Login using an existing account");
                System.out.println("2: Register a new account");
                System.out.println("3: Quit application");

                int moduleEntering = Util.getValidIntegerInput();
                switch (moduleEntering) {
                    // user wants to login
                    case 1:
                        User user = login();
                        try {
                            if (!(user == null))
                                user.getBoundary().doThings();
                            else
                                System.out.println("Error during login, please try again");
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        break;
                    // register a new user to the system
                    case 2:
                        registerUser();
                        break;
                    // Exit
                    case 3:
                        System.out.println("Goodbye!");
                        Util.closeScanner(); // Closes the inputstream
                        System.exit(0);
                        programOn = false;
                        break;
                    default:
                        System.out.println("Please enter one of the aformentioned options");
                }

            }

        }

        // Catch the exceptions that may occur - rubbish URL, Remote exception
        // Not bound exception or the arithmetic exception that may occur in
        // one of the methods creates an arithmetic error (e.g. divide by zero)
        catch (MalformedURLException murle) {
            System.out.println();
            System.out.println("MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println();
            System.out.println("RemoteException");
            System.out.println(re);
        } catch (NotBoundException nbe) {
            System.out.println();
            System.out.println("NotBoundException");
            System.out.println(nbe);
        } catch (java.lang.ArithmeticException ae) {
            System.out.println();
            System.out.println("java.lang.ArithmeticException");
            System.out.println(ae);
        }

    }
    /**
     * This function logs user in the system
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static User login() throws NoSuchAlgorithmException, IOException {
        System.out.println("Enter username");
        String username = Util.getValidLineInput();
        if (UserControl.ifUserExists(username)) {
            System.out.println("Please enter your password: ");
            String password = Util.getValidLineInput();
            try {
                boolean ifVerified = verifyServer(username);
                if (ifVerified == true) {
                    System.out.println("Server verified");
                } else {
                    System.out.println("Server not verified");
                    return null;
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            // User is being verified by server
            try {
                System.out.println("--Want to log in to the server");
                byte[] req = auctInterface.loginToServer(null, publicKey);
                if (req != "true".getBytes())
                    System.out.println("--Request refused, Doing challenge..");
                String pub_file = "publicKey_" + username;
                String priv_file = "privateKey_" + username;
                // byte[] keybytes_pub;
                // byte[] keybytes_priv;
                if (new File(pub_file).exists() == false || new File(priv_file).exists() == false) {
                    System.out.println("--Generating keys");
                    Util.generateKeysForClient(priv_file, pub_file);
                }

                // System.out.println("--File with the key exists");
                // Load key
                System.out.println("--Loading the key");
                // keybytes_pub = Files.readAllBytes(Paths.get(pub_file));
                // keybytes_priv = Files.readAllBytes(Paths.get(priv_file));

                try{
                    privateKey = getPrivate(priv_file);
                    publicKey = getPublic(pub_file);
                }catch(Exception e){
                    e.printStackTrace();
                }

                // Creating a Signature object
                Signature sign = Signature.getInstance("SHA256withRSA");

                sign.initSign(privateKey);
                sign.update(req);

                // Adding data to the signature
                byte[] signature = sign.sign();

                System.out.println("--Sending response to the server");
                req = auctInterface.loginToServer(signature, publicKey);

                if (req == null) {
                    System.out.println("You are verified!");
                    stub.saveUser(UserControl.login(username, password).getId());
                } else {
                    return null;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
            // finally after 5-stage auth user can log in to the system
            return UserControl.login(username, password);

        } else
            System.out.println("Error: Username does not exist in the system");
        return null;
    }
    /**
     * This function registers the user and saves the data to txt file
     */
    public static void registerUser() {
        System.out.println("Please enter a username: ");
        String username = Util.getValidLineInput();
        if (!UserControl.ifUserExists(username)) {
            System.out.println("Please choose a password with at least 8 characters: ");
            String password = "";
            boolean valid = false;
            while (!valid) {
                password = Util.getValidLineInput();
                if (password.length() >= 8)
                    valid = true;
                else
                    System.out.println("Please enter a password with at least 8 characters: ");
            }
            System.out.println("Do you want to be a seller (y) or a buyer (n)? Answer y or n:");
            char ans = Util.gatValidCharInputForAnswer();

            try {
                UserControl.registerUser(username, password, ans);
            } catch (MalformedURLException | RemoteException | NotBoundException | ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Registration Succesful!");
        }
        else System.out.println("Sorry, that username is already taken");
    }
    /**
     * This method verifies the server in challenge-response way
     * @param usr
     * @return tru if server is verified or false if not
     * @throws InvalidKeyException
     * @throws Exception
     */
    private static boolean verifyServer(String usr) throws InvalidKeyException, Exception {
        String pub_file = "publicKey_"+usr;
        String priv_file = "privateKey_"+usr;
        // byte[] keybytes_pub;
        // byte[] keybytes_priv;
        if(new File(pub_file).exists() == false || new File(priv_file).exists() == false){
            System.out.println("--Generating keys");
            Util.generateKeysForClient(priv_file, pub_file);
        }
    
        System.out.println("--File with the key exists");
        // Load key
        System.out.println("--Loading the key");
        // keybytes_pub = Files.readAllBytes(Paths.get(pub_file));
        // keybytes_priv = Files.readAllBytes(Paths.get(priv_file));
        try {
            privateKey = getPrivate(priv_file);
            publicKey = getPublic(pub_file);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        // get message from server signed by its private key
        System.out.println("--Generating challenge");
        challenge = Util.generateRandomString();
        byte [] messageToBeSend = challenge.getBytes();
        byte [] sigMess = auctInterface.challenge(messageToBeSend) ;
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(auctInterface.getPublic());
        sig.update(messageToBeSend);
        return sig.verify(sigMess);
    }
    /**
     * Retrieves private key from the file.
     * @param filename
     * @return private key
     * @throws Exception
     */
    public static PrivateKey getPrivate(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
    /**
     * Retrieves publik key from the file.
     * @param filename
     * @return public key
     * @throws Exception
     */
    public static PublicKey getPublic(String filename) throws Exception {
            byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
    }


}
