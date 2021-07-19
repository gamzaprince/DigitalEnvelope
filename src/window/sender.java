package window;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import service.DigitSign;
import service.KeyManage;
import service.MyKeyPair;
import service.MySecretKey;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JLabel;

public class sender {

	private JFrame frame;
	private static final Logger logger = Logger.getLogger(sender.class.getName());
	StringBuilder fname;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sender window = new sender();
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
	public sender() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 248, 220));
		frame.getContentPane().setForeground(new Color(255, 248, 220));
		frame.setForeground(new Color(255, 248, 220));
		frame.setBackground(new Color(255, 248, 220));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextPane textPane = new JTextPane();
		textPane.setText("결과");
		textPane.setBounds(36, 146, 241, 29);
		frame.getContentPane().add(textPane);
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setText("파일 이름");
		textPane_1.setBounds(36, 189, 241, 29);
		frame.getContentPane().add(textPane_1);
		
		JButton button = new JButton("키 생성하기");
		button.setIcon(new ImageIcon("/Users/gamza/Downloads/네트워크보안/Practice10/imgs/icon1.png"));
		button.setSelectedIcon(new ImageIcon("/Users/gamza/Downloads/네트워크보안/Practice10/imgs/icon1.png"));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
					
					textPane.setText("KeyPair와 비밀키를 생성 후 저장했습니다.");
				} catch (NoSuchAlgorithmException e2) {
					logger.log(Level.INFO, "[ERROR] [main] [sender] - NoSuchAlgorithmException");
				} finally {
					publicKeyFname.delete(0, publicKeyFname.length());
					privateKeyFname.delete(0, privateKeyFname.length());
				}
			}
		});
		button.setBounds(299, 146, 107, 29);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("파일 선택");
		button_1.setIcon(new ImageIcon("/Users/kwonheewon/Downloads/네트워크보안/Practice10/imgs/icon2.png"));
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser jfc = new JFileChooser();
		        int returnVal = jfc.showOpenDialog(null);
		        if(returnVal == 0) {
		            System.out.println("파일 열기를 선택했습니다.");
		            File file = jfc.getSelectedFile();
		            fname = new StringBuilder(file.getName());
		            textPane_1.setText(fname.toString());
		        }
		        else
		        {
		            System.out.println("파일 열기를 취소하였습니다.");
		        }
			}
		});
		button_1.setBounds(298, 189, 107, 29);
		frame.getContentPane().add(button_1);
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setText("결과");
		textPane_2.setBounds(37, 232, 241, 29);
		frame.getContentPane().add(textPane_2);
		
		JButton button_2 = new JButton("전자봉투 생성하기");
		button_2.setIcon(new ImageIcon("/Users/gamza/Downloads/네트워크보안/Practice10/imgs/icon3.png"));
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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
				String result1 = digit.createAndSave(fname, privateKeyFname, sigFname);
				if (result1.equals("create/save")) {
					System.out.println("전자서명이 생성 후 저장되었습니다.");
				} else if (result1.equals("create")) {
					System.out.println("전자서명 저장에 실패했습니다.");
				} else {
					System.out.println("전자서명 생성과 저장에 실패했습니다.");
				}
				
				// 전자봉투 생성 및 파일 저장
				String result2 = digit.signWithEnvelope(fname, publicKeyFname, publicKeyRecFname, secretKeyFname, sigFname,
						encryptedDataFname, envelopeFname);

				textPane_2.setText(result2);
				dataFname.delete(0, dataFname.length());
				publicKeyFname.delete(0, publicKeyFname.length());
				privateKeyFname.delete(0, privateKeyFname.length());
				publicKeyRecFname.delete(0, publicKeyRecFname.length());
				secretKeyFname.delete(0, secretKeyFname.length());
				sigFname.delete(0, sigFname.length());
				envelopeFname.delete(0, envelopeFname.length());
				encryptedDataFname.delete(0, encryptedDataFname.length());
			}
		});
		button_2.setBounds(299, 234, 108, 29);
		frame.getContentPane().add(button_2);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("/Users/gamza/Downloads/네트워크보안/Practice10/imgs/bob.png"));
		lblNewLabel.setBounds(34, 1, 137, 143);
		frame.getContentPane().add(lblNewLabel);
	}
}
