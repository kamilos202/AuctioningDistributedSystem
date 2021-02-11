import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import org.jgroups.JChannel;

/**
 * Server class
 */
public class Server {

	private static final int PORT = 8080;

	private static JChannel jChannel;

	public static void main(String args[]) throws Exception{

			jChannel = new JChannel();
			jChannel.connect("Cluster");
			FrontEndNode frontEndNode = new FrontEndNode(jChannel);
			jChannel.setDiscardOwnMessages(true);

			System.out.println("--Server is running");
			AuctionInterface stub = (AuctionInterface) UnicastRemoteObject.exportObject(frontEndNode, PORT);
			LocateRegistry.createRegistry(PORT);
			Naming.rebind("rmi://localhost:"+PORT+"/AuctionService", stub);


		// Generating unique session Id
		try {
			System.out.println("-->>Generating Session Id");
			Storage.appendToTxt(new String[] { "\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\nSession ID: ",
					generateUUID().toString() }, "history.txt");
		} catch (Exception esc) {
			System.out.println(esc);
		}

	}

	private static long getFirstBits() {
		Random random = new Random();
		return random.nextLong() & 0x3FFFFFFFFFFFFFFFL + 0x8000000000000000L;
	}

	private static long getLastBits() {
		LocalDateTime start = LocalDateTime.of(1582, 10, 15, 0, 0, 0);
		Duration dur = Duration.between(start, LocalDateTime.now());
		long timeUUID = (dur.getSeconds() * 10000000 + dur.getNano() * 100);
		long leastBitTime = (timeUUID & 0x000000000000FFFFL) >> 4;
		return (timeUUID & 0xFFFFFFFFFFFF0000L) + (1 << 12) + leastBitTime;
	}

	/**
	 * Generates session ID
	 * 
	 * @return UUID
	 */
	private static UUID generateUUID() {
		return new UUID(getFirstBits(), getLastBits());
	}

}
