package service;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyManage {
	private static final Logger logger = Logger.getLogger(KeyManage.class.getName());
	
	// key 파일 저장
	public static void saveKeyFile(StringBuilder publicKeyFname, Key key) {
		String pkn = new String(publicKeyFname);
	    try (FileOutputStream fstream = new FileOutputStream(pkn);){
	    	try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
				ostream.writeObject(key);
			}
	    } catch (FileNotFoundException e) {
	    	logger.log(Level.INFO, "[ERROR] [saveKeyFile] [KeyManage] - FileNotFoundException");
	    } catch (IOException e) {
	    	logger.log(Level.INFO, "[ERROR] [saveKeyFile] [KeyManage] - IOException");
	    }
	}
	
	// key 불러오기
	public static Key restoreKey(StringBuilder keyFilename) {
		String kn = new String(keyFilename);
		Key key = null;
		try (FileInputStream fis = new FileInputStream(kn)) {
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				Key obj = (Key)ois.readObject();
				key = obj;
				return key;
			}
		} catch (ClassNotFoundException e) {
			logger.log(Level.INFO, "[ERROR] [restoreKey] [KeyManage] - ClassNotFoundException");
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "[ERROR] [restoreKey] [KeyManage] - FileNotFoundException");
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [restoreKey] [KeyManage] - IOException");
		}
		return key;
	}
}
