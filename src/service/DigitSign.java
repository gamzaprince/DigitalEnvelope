package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DigitSign {
	private static final String signAlgorithm = "SHA1withRSA";
	private static final String ALGO = "AES";
	private static final Logger logger = Logger.getLogger(DigitSign.class.getName());
	private static final byte init = 0;

	public String createAndSave(StringBuilder dataFilename, StringBuilder keyFilename, StringBuilder saveSigFilename) {
		if (dataFilename == null || saveSigFilename == null || keyFilename == null) {
			return "fail";
		}

		byte[] sigResult = sign(dataFilename, keyFilename);
		if (!java.util.Arrays.equals(sigResult, "fail".getBytes())) {
			boolean result = MyFile.saveFile(saveSigFilename, sigResult);
			if (sigResult != null) {
				Arrays.fill(sigResult, init);
			}
			if (result == true) {
				return "create/save";
			} else {
				return "create";
			}
		}
		return "fail";
	}

	public byte[] sign(StringBuilder dataFilename, StringBuilder keyFilename) {
		byte[] data = null;
		try {
			Signature sig = Signature.getInstance(signAlgorithm);
			PrivateKey privateKey = (PrivateKey) KeyManage.restoreKey(keyFilename);
			if (privateKey == null) {
				return "fail".getBytes();
			}
			sig.initSign(privateKey);
			data = MyFile.readFile(dataFilename);
			if (data == null) {
				return "fail".getBytes();
			}
			sig.update(data);
			return sig.sign();
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [sign] [DigitSign] - IOException");
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.INFO, "[ERROR] [sign] [DigitSign] - NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			logger.log(Level.INFO, "[ERROR] [sign] [DigitSign] - InvalidKeyException");
		} catch (SignatureException e) {
			logger.log(Level.INFO, "[ERROR] [sign] [DigitSign] - SignatureException");
		} finally {
			if (data != null) {
				Arrays.fill(data, init);
			}
		}
		return data;
	}

	public String verify(StringBuilder dataFilename, StringBuilder sigFilename, StringBuilder keyFilename) {
		byte[] data = null;
		byte[] sigg = getByteData(sigFilename);
		byte[] key = getByteData(keyFilename);
		try {
			Signature sig = Signature.getInstance(signAlgorithm);
			if (key == null) {
				return "NotExist";
			}
			PublicKey publicKey;
			try {
				publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
			} catch (InvalidKeySpecException e1) {
				return "InvalidKey";
			}
			sig.initVerify(publicKey);
			try {
				data = MyFile.readFile(dataFilename);
			} catch (IOException e) {
				return "DataNotExist";
			}
			if (data == null) {
				return "DataNotExist";
			}
			sig.update(data);
			if (sigg == null) {
				return "SigNotExist";
			}
			boolean rslt = sig.verify(sigg);
			if (rslt) {
				return "success";
			} else {
				return "fail";
			}
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - NoSuchAlgorithmException");
		} catch (InvalidKeyException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - InvalidKeyException");
		} catch (SignatureException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - SignatureException");
		} finally {
			if (sigg != null) {
				Arrays.fill(sigg, init);
			}
			if (data != null) {
				Arrays.fill(data, init);
			}
			if (key != null) {
				Arrays.fill(key, init);
			}
		}
		return "fail";
	}

	public static byte[] readFile(StringBuilder dataFilename) {
		String filename = new String(Paths.get("").toAbsolutePath() + "\\src\\" + dataFilename);
		Path path = (new File(filename)).toPath();
		byte[] fileRslt = null;
		try {
			fileRslt = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileRslt;
	}

	public void saveSignature(StringBuilder filename, byte[] result) {
		String fn = new String(filename);
		try (FileOutputStream fstream = new FileOutputStream(fn)) {
			try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
				ostream.writeObject(result);
			}
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [saveSignature] [DigitSign] - IOException");
		}
	}

	public String signWithEnvelope(StringBuilder dataFilename, StringBuilder publicKeyAfilename,
			StringBuilder publicKeyBfilename, StringBuilder secretKeyFilename,
			StringBuilder sigFileName, StringBuilder encryptedDataFilename, StringBuilder envelopeFilename) {

		// 송신자 공개키 불러오기
		PublicKey publicKeyA = (PublicKey) KeyManage.restoreKey(publicKeyAfilename);
		// 수신자 공개키 불러오기
		PublicKey publicKeyB = (PublicKey) KeyManage.restoreKey(publicKeyBfilename);
		// 비밀키 불러오기
		SecretKey secretKey = (SecretKey) KeyManage.restoreKey(secretKeyFilename);
		// 암호화할 데이터 불러오기
		byte[] data = readFile(dataFilename);
		// 전자서명 불러오기
		byte[] sig = readFile(sigFileName);
		boolean rslt = false;

		try {
			// 비밀키로 데이터, 전자서명, 송신자 공개키 암호화하기
			Cipher cSecret = Cipher.getInstance(ALGO);
			cSecret.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encData = cSecret.doFinal(data);
			byte[] encSig = cSecret.doFinal(sig);
			byte[] encOrigPublicKey = null;

			if (publicKeyA == null) {
				return "전자봉투 생성에 실패하였습니다.";
			} else {
				encOrigPublicKey = cSecret.doFinal(publicKeyA.getEncoded());
			}
			
			// 암호화한 데이터들 list에 저장 -> 하나의 파일로 저장하기
			List<byte[]> list = new ArrayList<byte[]>();
			list.add(encData);
			list.add(encSig);
			list.add(encOrigPublicKey);
			saveEncryptedFile(encryptedDataFilename, list);

			// 비밀키 수신자의 공개키로 암호화 -> 전자봉투 생성
			Cipher cPublic = Cipher.getInstance("RSA");
			cPublic.init(Cipher.ENCRYPT_MODE, publicKeyB);
			byte[] encSecretKey;
			encSecretKey = cPublic.doFinal(secretKey.getEncoded());

			rslt = saveEncSecretKey(envelopeFilename, encSecretKey);
			if (rslt) {
				return ("전자봉투가 생성되었습니다.");
			}
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.INFO, "[ERROR] [signWithEnvelope] [DigitSign] - NoSuchAlgorithmException");
		} catch (NoSuchPaddingException e) {
			logger.log(Level.INFO, "[ERROR] [signWithEnvelope] [DigitSign] - NoSuchPaddingException");
		} catch (InvalidKeyException e) {
			logger.log(Level.INFO, "[ERROR] [signWithEnvelope] [DigitSign] - InvalidKeyException");
		} catch (IllegalBlockSizeException e) {
			logger.log(Level.INFO, "[ERROR] [signWithEnvelope] [DigitSign] - IllegalBlockSizeException");
		} catch (BadPaddingException e) {
			logger.log(Level.INFO, "[ERROR] [signWithEnvelope] [DigitSign] - BadPaddingException");
		}
		return ("전자봉투 생성에 실패하였습니다.");
	}

	public void saveEncryptedFile(StringBuilder filename, List<byte[]> list) {
		String fn = new String(filename);
		try (FileOutputStream fstream = new FileOutputStream(fn)) {
			try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
				ostream.writeObject(list);
			}
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [saveEncryptedFile] [DigitSign] - IOException");
		}
	}

	public boolean saveEncSecretKey(StringBuilder filename, byte[] key) {
		String fn = new String(filename);
		if (key == null) {
			System.out.println("데이터가 비어 있습니다.");
			return false;
		}
		try (FileOutputStream fstream = new FileOutputStream(fn)) {
			try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
				ostream.writeObject(key);
				return true;
			}
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [saveEncSecretKey] [DigitSign] - IOException");
		}
		return false;
	}

	public String verifyEnvelope(StringBuilder encryptedDataFname, StringBuilder envelopeFname,
			StringBuilder privateKeyFname, StringBuilder decryptedDataFname, StringBuilder decryptedSigFname,
			StringBuilder decryptedPublicKeyFname) {

		// 수신자 사설키 불러오기
		PrivateKey privateKey = (PrivateKey) KeyManage.restoreKey(privateKeyFname);
		if (privateKey == null) {
			return ("NotExist");
		}

		// 수신자 사설키로 전자봉투 해독 -> 비밀키 획득
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] envelope = null;
			envelope = getByteData(envelopeFname);
			if (envelope == null) {
				return ("fail");
			}
			byte[] secretKeyData = null;
			secretKeyData = cipher.doFinal(envelope);

			if (secretKeyData == null) {
				return ("NotExist");
			} else {
				SecretKey secretKey = new SecretKeySpec(secretKeyData, ALGO);
				Cipher c = Cipher.getInstance(ALGO);
				c.init(Cipher.DECRYPT_MODE, secretKey);

				List<byte[]> list = getDecryptedFile(encryptedDataFname);
				if (list.size() == 0) {
					return ("fail");
				}

				byte[] data = c.doFinal(list.get(0));
				byte[] sig = c.doFinal(list.get(1));
				byte[] key = c.doFinal(list.get(2));

				if (data == null || sig == null || key == null) {
					return ("fail");
				}
				saveDecryptedDataFile(decryptedDataFname, data);
				saveDecryptedDataFile(decryptedSigFname, sig);
				saveDecryptedDataFile(decryptedPublicKeyFname, key);

				return ("success");
			}
		} catch (NoSuchAlgorithmException e1) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - NoSuchAlgorithmException");
		} catch (NoSuchPaddingException e1) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - NoSuchPaddingException");
		} catch (InvalidKeyException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - InvalidKeyException");
		} catch (IllegalBlockSizeException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - IllegalBlockSizeException");
		} catch (BadPaddingException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - BadPaddingException");
		}
		return "fail";
	}

	@SuppressWarnings("unchecked")
	public List<byte[]> getDecryptedFile(StringBuilder filename) {
		String fn = new String(filename);

		List<byte[]> list = new ArrayList<byte[]>();
		try (FileInputStream fis = new FileInputStream(fn)) {
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				Object obj = ois.readObject();
				list = (List<byte[]>) obj;
				return list;
			}
		} catch (ClassNotFoundException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - ClassNotFoundException");
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - FileNotFoundException");
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [verify] [DigitSign] - IOException");
		}
		return list;
	}

	public byte[] getByteData(StringBuilder filename) {
		byte[] data = null;
		String fn = new String(filename);
		try (FileInputStream fis = new FileInputStream(fn)) {
			try (ObjectInputStream ois = new ObjectInputStream(fis)) {
				Object obj = ois.readObject();
				data = (byte[]) obj;
				return data;
			}
		} catch (ClassNotFoundException e) {
			logger.log(Level.INFO, "[ERROR] [getByteData] [DigitSign] - ClassNotFoundException");
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "[ERROR] [getByteData] [DigitSign] - FileNotFoundException");
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [getByteData] [DigitSign] - IOException");
		}
		return data;
	}

	public void saveDecryptedDataFile(StringBuilder filename, List<byte[]> data) {
		String fn = new String(filename);
		try (FileOutputStream fstream = new FileOutputStream(fn)) {
			try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
				ostream.writeObject(data);
			}
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [saveDecryptedDataFile] [DigitSign] - IOException");
		}
	}

	public void saveDecryptedDataFile(StringBuilder filename, byte[] data) {
		String fn = new String(filename);
		try (FileOutputStream fstream = new FileOutputStream(fn)) {
			try (ObjectOutputStream ostream = new ObjectOutputStream(fstream)) {
				ostream.writeObject(data);
			}
		} catch (IOException e) {
			logger.log(Level.INFO, "[ERROR] [saveDecryptedDataFile] [DigitSign] - IOException");
		}
	}
}