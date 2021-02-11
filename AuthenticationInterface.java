import java.io.IOException;
import java.rmi.Remote;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

public interface AuthenticationInterface extends Remote{
    public byte [] challenge(byte [] msg) throws NoSuchAlgorithmException, IOException, SignatureException;
    public byte [] loginToServer(byte [] msg, PublicKey pubKey) throws NoSuchAlgorithmException, IOException, SignatureException;
    public PublicKey getPublic() throws Exception;
}
