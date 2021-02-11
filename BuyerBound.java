import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/**
 * Boundary class of the buyer
 */
public class BuyerBound extends UserBound {

    User userIn;

    public BuyerBound(Buyer buyer) {
        userIn = buyer;
    }
    /**
     * Buyer actions are performed here
     */
    @Override
    public void doThings() throws IOException, ClassNotFoundException {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n=========== Buyer MENU ===========");
            System.out.println("1: See active auctions");
            System.out.println("2: Bid");
            System.out.println("3: See my won items");
            System.out.println("4: Logout");
            System.out.println("\n=============================================");

            int actionChoice = Util.getValidIntegerInput();

            switch (actionChoice) {
                case 1:
                    try {
                        seeActiveAuctions();
                    } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                            | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    bid();
                    break;
                case 3:
                    try {
                        seeSoldAuctions(userIn.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
					break;
                case 4:
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Please enter one of the valid options");
                    break;
            }
        }
    }
    /**
     * Buyer can bid in this method
     */
    private void bid() {
        String email = userIn.getEmail();
        String fullName = userIn.getFullName();
        if (email == "") {
            System.out.println("Hey! Looks like You are the first time in the auction session.\nProvide your email address:");
            email = Util.getValidStringInput();
            while (!Util.emailIsValid(email)) {
                System.out.println("ERROR! Enter valid email address:");
                email = Util.getValidStringInput();
            }
            userIn.setEmail(email);
            System.out.println("Enter your full name:");
            fullName = Util.getValidStringInput();
            userIn.setFullName(fullName);

        }
        System.out.println("You are ready to go! \nWhat is the Id of the item you want to bid:");
        int itemIdd = Util.getValidIntegerInput();
        System.out.println("Input your price (must be higher than current one):");
        double price = Util.getValidDoubleInput();

        String message = "";
        try {
            message = Client.stub.bid(email, fullName, userIn.getId(), itemIdd, price);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | InvalidAlgorithmParameterException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    
        System.out.println(message);
    }
}