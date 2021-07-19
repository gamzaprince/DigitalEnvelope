package service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Tester {

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		// 비교 파일 생성
		TestCase tc = new TestCase();
		tc.createTestFiles();

		StringBuilder dataFname = new StringBuilder("test1.txt");
		StringBuilder publicKeyFname = new StringBuilder("publicKeyA");
		StringBuilder privateKeyFname = new StringBuilder("privateKeyA");
		StringBuilder publicKeyRecFname = new StringBuilder("publicKeyB");
		StringBuilder privateKeyRecFname = new StringBuilder("privateKeyB");
		StringBuilder secretKeyFname = new StringBuilder("secretKey");
		StringBuilder sigFname = new StringBuilder("testsign");
		StringBuilder envelopeFname = new StringBuilder("testenvelope");
		StringBuilder encryptedDataFname = new StringBuilder("encryptedData");
		StringBuilder decryptedDataFname = new StringBuilder("decryptedData");
		StringBuilder decryptedSigFname = new StringBuilder("decryptedSig");
		StringBuilder decryptedPublicKeyFname = new StringBuilder("decryptedPublicKey");

		DigitSign digit = new DigitSign();
		
		// 전자봉투 생성 및 파일 저장
		digit.signWithEnvelope(dataFname, publicKeyFname, publicKeyRecFname, secretKeyFname, sigFname,
				encryptedDataFname, envelopeFname);

		// 전자봉투 복호화 시도 결과 출력

		for (int i = 0; i < tc.getTestSize(); i++) {
			StringBuilder title = tc.getKeyfileNames()[i];

			System.out.println("----------------------");
			System.out.println("기준 개인키: " + tc.getKeyfileNames()[0]);
			System.out.println("비교 개인키: " + title);
			System.out.println("<전자봉투 복호화 시도 결과>");

			String result = digit.verifyEnvelope(encryptedDataFname, envelopeFname, title, decryptedDataFname,
					decryptedSigFname, decryptedPublicKeyFname);
			System.out.println(result);

			System.out.println("----------------------");
		}
		dataFname.delete(0, dataFname.length());
		publicKeyFname.delete(0, publicKeyFname.length());
		privateKeyFname.delete(0, privateKeyFname.length());
		publicKeyRecFname.delete(0, publicKeyRecFname.length());
		privateKeyRecFname.delete(0, privateKeyRecFname.length());
		secretKeyFname.delete(0, secretKeyFname.length());
		sigFname.delete(0, sigFname.length());
		envelopeFname.delete(0, envelopeFname.length());
		encryptedDataFname.delete(0, encryptedDataFname.length());
	}
}