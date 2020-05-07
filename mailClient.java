import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;

public class mailClient implements ActionListener {
	private static JLabel senderLabel;
	private static JTextField senderTextField;
	private static JLabel passwordLabel;
	private static JPasswordField passwordField;
	private static JLabel recipientLabel;
	private static JTextField recipientTextField;
	private static JLabel messageLabel;
	private static JTextField messageTextField;
	private static JLabel subjectLabel;
	private static JTextField subjectTextField;
	private static JButton sendButton;
	private static JLabel messageSendConfirmation;
	
	private static String sender;
	private static String recipient;
	private static String subject;
	private static String text;
	private static String password;
	
	public static void main(String[] args) {
		//setup GUI
		JPanel panel = new JPanel();
		JFrame frame = new JFrame("JMail");
		
		ImageIcon icon = new ImageIcon(mailClient.class.getResource("/img/jmail icon.png"));
		frame.setIconImage(icon.getImage());
		
		frame.setSize(265, 355);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		panel.setLayout(null);
		
		//sender field
		senderLabel = new JLabel("From:");
		senderLabel.setBounds(10, 5, 70, 25);
		panel.add(senderLabel);
		
		senderTextField = new JTextField(20);
		senderTextField.setBounds(75, 5, 165, 25);
		panel.add(senderTextField);
		
		//sender field
		passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(10, 35, 70, 25);
		panel.add(passwordLabel);
		
		passwordField = new JPasswordField(20);
		passwordField.setBounds(75, 35, 165, 25);
		panel.add(passwordField);
		
		//recipient field
		recipientLabel = new JLabel("To:");
		recipientLabel.setBounds(10, 65, 70, 25);
		panel.add(recipientLabel);
		
		recipientTextField = new JTextField(20);
		recipientTextField.setBounds(75, 65, 165, 25);
		panel.add(recipientTextField);
		
		//subject field
		subjectLabel = new JLabel("Subject:");
		subjectLabel.setBounds(10, 95, 60, 25);
		panel.add(subjectLabel);
		
		subjectTextField = new JTextField(20);
		subjectTextField.setBounds(75, 95, 165, 25);
		panel.add(subjectTextField);
		
		//message field
		messageLabel = new JLabel("Message:");
		messageLabel.setBounds(10, 125, 100, 25);
		panel.add(messageLabel);
		
		messageTextField = new JTextField(20);
		messageTextField.setBounds(10, 155, 210, 100);
		panel.add(messageTextField);
		
		//send button
		sendButton = new JButton("Send");
		sendButton.setBounds(82, 260, 80, 25);
		sendButton.addActionListener(new mailClient());
		panel.add(sendButton);
		
		//sent message when message sends
		messageSendConfirmation = new JLabel("");
		messageSendConfirmation.setBounds(10, 290, 100, 25);
		panel.add(messageSendConfirmation);
		
		frame.setVisible(true);
		
	}
	
	//get info from GUI, set properties, set session, send message
	private static void sendMessage() throws Exception {
		sender = senderTextField.getText();
		password = new String(passwordField.getPassword());
		recipient = recipientTextField.getText();
		subject = subjectTextField.getText();
		text = messageTextField.getText();
				
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, password);
			}
		});
		
		Message message = prepareMessage(session);
		
		Transport.send(message);
	}
	
	//create a message object
	private static Message prepareMessage(Session session) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(subject);
			message.setText(text);
			return message;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error preparing message");
		}
		return null;
	}

	//send message and reset GUI when send button is pressed
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			sendMessage();
			messageSendConfirmation.setText("Message Sent");
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Error sending message");
			messageSendConfirmation.setText("Message Not Sent");
		}
		
		senderTextField.setText("");
		passwordField.setText("");
		recipientTextField.setText("");
		subjectTextField.setText("");
		messageTextField.setText("");
	}
}
