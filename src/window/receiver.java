package window;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextPane;

import service.DigitSign;
import service.KeyManage;
import service.MyKeyPair;
import java.awt.Color;

public class receiver {

	private JFrame frame;
	private static final Logger logger = Logger.getLogger(receiver.class.getName());
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					receiver window = new receiver();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public receiver() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 248, 220));
		frame.setForeground(new Color(255, 255, 240));
		frame.getContentPane().setForeground(new Color(255, 250, 240));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		JTextPane textPane = new JTextPane();
		textPane.setText("결과");
		textPane.setBounds(38, 145, 248, 29);
		frame.getContentPane().add(textPane);
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setText("결과");
		textPane_1.setBounds(38, 223, 248, 29);
		
		JButton button_1 = new JButton("파일 검증하기");
		button_1.setIcon(new ImageIcon("/Users/gamza/Downloads/네트워크보안/Practice10/imgs/icon4.png"));
		button_1.setSelectedIcon(new ImageIcon("/gamza/kwonheewon/Downloads/네트워크보안/Practice10/imgs/icon4.png"));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
					textPane_1.setText("존재하지 않는 파일입니다.");
				} else if (result.equals("fail")) {
					textPane_1.setText("변경된 파일입니다.");
				} else {
					textPane_1.setText("원본 파일입니다.");
				}
				
				privateKeyFname.delete(0, privateKeyFname.length());
				envelopeFname.delete(0, envelopeFname.length());
				encryptedDataFname.delete(0, encryptedDataFname.length());
				decryptedDataFname.delete(0, decryptedDataFname.length());
				decryptedSigFname.delete(0, decryptedSigFname.length());
				decryptedPublicKeyFname.delete(0, decryptedPublicKeyFname.length());
			}
		});
		button_1.setBounds(307, 223, 108, 29);
		frame.getContentPane().add(button_1);
		
		JButton button = new JButton("키 생성하기");
		button.setIcon(new ImageIcon("/Users/gamza/Downloads/네트워크보안/Practice10/imgs/icon1.png"));
		button.setBounds(307, 145, 108, 29);
		frame.getContentPane().add(button);
		

		frame.getContentPane().add(textPane_1);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("/Users/gamza/Downloads/네트워크보안/Practice10/imgs/alice.png"));
		lblNewLabel.setBounds(38, 6, 126, 134);
		frame.getContentPane().add(lblNewLabel);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				StringBuilder publicKeyFname = new StringBuilder("publicKeyB");		
				StringBuilder privateKeyFname = new StringBuilder("privateKeyB");

				try {
					// 수신자 keyPair 생성
					MyKeyPair myKeyPairA = MyKeyPair.getInstance(2048);
					myKeyPairA.createKeys();
					
					// 수신자 RSA키 파일에 저장
					KeyManage.saveKeyFile(publicKeyFname, myKeyPairA.getPublicKey());
					KeyManage.saveKeyFile(privateKeyFname, myKeyPairA.getPrivateKey());

					textPane.setText("keyPair을 생성하고 저장했습니다.");
				} catch (NoSuchAlgorithmException e1) {
					logger.log(Level.INFO, "[ERROR] [button] [receiver] - NoSuchAlgorithmException");
				}
				publicKeyFname.delete(0, publicKeyFname.length());
				privateKeyFname.delete(0, privateKeyFname.length());
			}
		});
		
	}
}
