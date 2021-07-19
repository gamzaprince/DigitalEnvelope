package service;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestCase {
	private static final Logger logger = Logger.getLogger(KeyManage.class.getName());
   private StringBuilder[] KeyfileNames = new StringBuilder[3];

   public TestCase() {
      KeyfileNames[0] = new StringBuilder();
      KeyfileNames[0].append("privateKeyB");
      for (int i = 1; i < KeyfileNames.length; i++) {
         KeyfileNames[i] = new StringBuilder();
         KeyfileNames[i].append("privateKeyB" + (i + 1));
      }
   }

   public boolean createTestFiles() {
      boolean result = true;
      try {
         //복제한 KeyFile 생성
         KeyManage.saveKeyFile(KeyfileNames[1], KeyManage.restoreKey(KeyfileNames[0]));
         
         // 새로운 Key 생성
         MyKeyPair myKeyPairA;
         myKeyPairA = MyKeyPair.getInstance(2048);
         myKeyPairA.createKeys();
         KeyManage.saveKeyFile(KeyfileNames[2], myKeyPairA.getPrivateKey());
         
      } catch (NoSuchAlgorithmException e) {
    	  logger.log(Level.INFO, "[ERROR] [saveKeyFile] [KeyManage] - NoSuchAlgorithmException");
      }
      return result;
   }

   public int getTestSize() {
      return KeyfileNames.length;
   }

   public StringBuilder[] getKeyfileNames() {
      return KeyfileNames;
   }
}