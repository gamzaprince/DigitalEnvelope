package sender;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.MyKeyPair;
import service.MySecretKey;
import service.KeyManage;

public class CreateKeySender {
	private static final Logger logger = Logger.getLogger(CreateKeySender.class.getName());

	public static void main(String[] args) throws Exception {
		//TODO Auto-generated method stub
		StringBuilder publicKeyFname = new StringBuilder("publicKeyA");
		StringBuilder privateKeyFname = new StringBuilder("privateKeyA");
		StringBuilder secretKeyFname = new StringBuilder("secretKey");
		try {
			//송신자 keyPair 생성
			MyKeyPair myKeyPairA = MyKeyPair.getInstance(2048);
			myKeyPairA.createKeys();
			//송신자 RSA키 파일에 저장
			KeyManage.saveKeyFile(publicKeyFname, myKeyPairA.getPublicKey());
			KeyManage.saveKeyFile(privateKeyFname, myKeyPairA.getPrivateKey());
			//비밀키 AES 생성
			MySecretKey mySecretKey = new MySecretKey();
			mySecretKey.createKey();
			//비밀키 AES 파일에 저장
			KeyManage.saveKeyFile(secretKeyFname, mySecretKey.getSecretKey());
			
			System.out.println("KeyPair와 비밀키를 생성 후 저장했습니다.");
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.INFO, "[ERROR] [main] [CreateKeySender] - NoSuchAlgorithmException");
		} finally {
			publicKeyFname.delete(0, publicKeyFname.length());
			privateKeyFname.delete(0, privateKeyFname.length());
		}
	}
}