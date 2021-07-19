package sender;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import service.DigitSign;

public class SignWithEnvelope {

	public static void main(String[] args) throws InvalidKeyException, SignatureException, IOException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		StringBuilder dataFname = new StringBuilder("test1.txt");
		
		StringBuilder privateKeyFname = new StringBuilder("privateKeyA");
		
		StringBuilder publicKeyFname = new StringBuilder("publicKeyA");
		StringBuilder publicKeyRecFname = new StringBuilder("publicKeyB");
		StringBuilder secretKeyFname = new StringBuilder("secretKey");
		
		StringBuilder sigFname = new StringBuilder("testsign");
		StringBuilder envelopeFname = new StringBuilder("testenvelope");
		StringBuilder encryptedDataFname = new StringBuilder("encryptedData");

		DigitSign digit = new DigitSign();
		
		// 전자서명 생성 및 파일 저장
		String result1 = digit.createAndSave(dataFname, privateKeyFname, sigFname);
		if (result1.equals("create/save")) {
			System.out.println("전자서명이 생성 후 저장되었습니다.");
		} else if (result1.equals("create")) {
			System.out.println("전자서명 저장에 실패했습니다.");
		} else {
			System.out.println("전자서명 생성과 저장에 실패했습니다.");
		}
		
		// 전자봉투 생성 및 파일 저장
		String result2 = digit.signWithEnvelope(dataFname, publicKeyFname, publicKeyRecFname, secretKeyFname, sigFname,
				encryptedDataFname, envelopeFname);

		System.out.println(result2);
		dataFname.delete(0, dataFname.length());
		publicKeyFname.delete(0, publicKeyFname.length());
		privateKeyFname.delete(0, privateKeyFname.length());
		publicKeyRecFname.delete(0, publicKeyRecFname.length());
		secretKeyFname.delete(0, secretKeyFname.length());
		sigFname.delete(0, sigFname.length());
		envelopeFname.delete(0, envelopeFname.length());
		encryptedDataFname.delete(0, encryptedDataFname.length());
	}
}
