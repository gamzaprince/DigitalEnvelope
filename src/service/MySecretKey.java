package service;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MySecretKey extends KeyManage {
	private static final Logger logger = Logger.getLogger(MySecretKey.class.getName());
	private static final String ALGO = "AES";
	private static final byte[] keyValue= {'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e',
										'c', 'r', 'e', 't', 'K', 'e', 'y'};
	
	private SecretKey key;
	
	public static SecretKey generateKey() throws Exception {
		return (new SecretKeySpec(keyValue, ALGO));
	}
	
	public void createKey() {
		try {
			this.key = generateKey();
		} catch (Exception e) {
			logger.log(Level.INFO, "[ERROR] [createKey] [KeyManage] - Exception");
		}
	}
	
	public SecretKey getSecretKey() {
		return key;
	}
	
}
