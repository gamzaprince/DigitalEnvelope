package receiver;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.KeyManage;
import service.MyKeyPair;

public class CreateKeyReceiver {
	
	private static final Logger logger = Logger.getLogger(CreateKeyReceiver.class.getName());
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringBuilder publicKeyFname = new StringBuilder("publicKeyB");		
		StringBuilder privateKeyFname = new StringBuilder("privateKeyB");

		try {
			// 수신자 keyPair 생성
			MyKeyPair myKeyPairA = MyKeyPair.getInstance(2048);
			myKeyPairA.createKeys();
			
			// 수신자 RSA키 파일에 저장
			KeyManage.saveKeyFile(publicKeyFname, myKeyPairA.getPublicKey());
			KeyManage.saveKeyFile(privateKeyFname, myKeyPairA.getPrivateKey());

			System.out.println("keyPair을 생성하고 저장했습니다.");
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.INFO, "[ERROR] [main] [CreateKeyReceiver] - NoSuchAlgorithmException");
		}
		publicKeyFname.delete(0, publicKeyFname.length());
		privateKeyFname.delete(0, privateKeyFname.length());
	}
}
