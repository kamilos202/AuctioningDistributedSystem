
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
/**
 * Utility class
 * Class needed for al sort of validation and things like encryption/decryption.
 */
public class Util {
	static public String format = "%-40s%s%n";
	static Scanner sc = new Scanner(System.in);

	public static String getValidStringInput() {
		return sc.next();
	}

	public static String getValidLineInput() {

		String sth = getValidStringInput();
		return sth + sc.nextLine();
	}

	public static int getValidIntegerInput() {
		while (!sc.hasNextInt()) {
			sc.next();
			System.out.println("Please enter a valid number");
		}
		return sc.nextInt();
	}

	public static int getValidIntegerInput(int min, int max) {
		boolean inLimits = false;
		int output = 0;
		while (!inLimits) {
			while (!sc.hasNextInt()) {
				sc.next();
				System.out.println("Please enter a valid number");
			}
			output = sc.nextInt();
			if (output > min && output < max) {
				inLimits = true;
			} else
				System.out.println("Your input is not within the given limits");

		}

		return output;

	}

	public static char gatValidCharInputForAnswer() {
		char ch;
		while (true) {
			ch = sc.next().charAt(0);
			if (ch == 'y' || ch == 'n')
				break;
			System.out.println("Please enter a valid answer");
		}
		return ch;
	}

	/**
	 * Checks for valid user's imput
	 * 
	 * @return valid double variable
	 */
	public static double getValidDoubleInput() {
		while (!sc.hasNextDouble()) {
			String input = sc.next();
			try {
				return Double.parseDouble(input);
			} catch (Exception e) {
				System.out.println("Please enter a valid decimal number");
			}
		}
		return sc.nextDouble();
	}

	public static AuctionItem decryptItem(SealedObject obj) throws ClassNotFoundException, IllegalBlockSizeException,
			BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		String keyFile = "key";

		// Load key
		System.out.println("--Loading the key");
		byte[] keybytes = Files.readAllBytes(Paths.get(keyFile));
		SecretKey key = new SecretKeySpec(keybytes, "AES");

		// create cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);

		// SealedObject sealedObject = stub.getSpec(auctionItemId, sealedRequest);

		System.out.println("--Decoding requested item");
		return (AuctionItem) obj.getObject(cipher);

	}

	// public static SealedObject encryptItem(AuctionItem item) throws InvalidKeyException, IllegalBlockSizeException,
	// 		IOException, NoSuchAlgorithmException, NoSuchPaddingException {
	// 	String keyFile = "key";
	// 	// check if the file with the symmetric key already exists
	// 	if (new File(keyFile).exists()) {
	// 		System.out.println("--File with the key exists");
	// 	} else {
	// 		// if no file found generate the key
	// 		System.out.println("--No key found");
	// 		        //generate symmetric key
	// 				System.out.println("--Generating key");
	// 				KeyGenerator generator = KeyGenerator.getInstance("AES");
	// 				SecretKey key = generator.generateKey();
	// 				//save generated key to the file called "key"
	// 				System.out.println("--Saving key to the file");
	// 				keyFile = "key";
	// 				try (FileOutputStream out = new FileOutputStream(keyFile)) {
	// 					byte[] keybytes = key.getEncoded();
	// 					out.write(keybytes);
	// 				}catch(Exception e){
	// 					e.printStackTrace();
	// 				}
	// 	}
	// 	// Load key
	// 	System.out.println("--Loading the key");
	// 	byte[] keybytes = Files.readAllBytes(Paths.get(keyFile));
	// 	SecretKey key = new SecretKeySpec(keybytes, "AES");

	// 	// create cipher
	// 	Cipher cipher = Cipher.getInstance("AES");
	// 	cipher.init(Cipher.ENCRYPT_MODE, key);

	// 	// create sealed object
	// 	System.out.println("--Encoding AuctionItem");
	// 	return new SealedObject(item, cipher);

	// }

	public static boolean emailIsValid(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;		
		return pat.matcher(email).matches();
	}

	public static void closeScanner() {
		sc.close();
	}


	public static KeyPair generateKeys()
			throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator;
		KeyPair pair;
		keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		pair = keyPairGenerator.generateKeyPair();
		return pair;
	}

	public static void generateKeysForClient(String priv_file, String pub_file)
			throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator;
		KeyPair pair;
		PrivateKey privateKey;
		PublicKey publicKey;		
		byte[] keybytes_pub;
        byte[] keybytes_priv;
		keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		pair = keyPairGenerator.generateKeyPair();
		publicKey = pair.getPublic();
		privateKey = pair.getPrivate();
		try (FileOutputStream out1 = new FileOutputStream(pub_file)) {
			keybytes_pub = publicKey.getEncoded();
			out1.write(keybytes_pub);

		} catch (Exception e) {
			System.out.println(e);
		}
		try (FileOutputStream out2 = new FileOutputStream(priv_file)) {
			keybytes_priv = privateKey.getEncoded();
			out2.write(keybytes_priv);
		} catch (Exception e) {
			System.out.println(e);
		}
	
	}


	public static String generateRandomString() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
     
        return generatedString;
    }
}
