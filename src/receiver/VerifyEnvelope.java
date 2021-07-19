package receiver;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import service.DigitSign;

public class VerifyEnvelope {


	public static void main(String[] args) throws InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, SignatureException, IOException {
		// TODO Auto-generated method stub

		StringBuilder privateKeyFname = new StringBuilder("privateKeyB");
		StringBuilder envelopeFname = new StringBuilder("testenvelope");
		StringBuilder encryptedDataFname = new StringBuilder("encryptedData");
		StringBuilder decryptedDataFname = new StringBuilder("decryptedData");
		StringBuilder decryptedSigFname = new StringBuilder("decryptedSig");
		StringBuilder decryptedPublicKeyFname = new StringBuilder("decryptedPublicKey");

		DigitSign digit = new DigitSign();

		// 전자봉투 검증
		String result = digit.verifyEnvelope(encryptedDataFname, envelopeFname, privateKeyFname, decryptedDataFname,
				decryptedSigFname, decryptedPublicKeyFname);

		if (result.equals("NotExist")) {
			System.out.println("존재하지 않는 파일입니다.");
		} else if (result.equals("fail")) {
			System.out.println("변경된 파일입니다.");
		} else {
			System.out.println("원본 파일입니다.");
		}
		
		privateKeyFname.delete(0, privateKeyFname.length());
		envelopeFname.delete(0, envelopeFname.length());
		encryptedDataFname.delete(0, encryptedDataFname.length());
		decryptedDataFname.delete(0, decryptedDataFname.length());
		decryptedSigFname.delete(0, decryptedSigFname.length());
		decryptedPublicKeyFname.delete(0, decryptedPublicKeyFname.length());
	}

}
