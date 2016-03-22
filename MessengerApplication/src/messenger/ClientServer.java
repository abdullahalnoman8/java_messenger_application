package messenger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientServer extends JFrame implements Runnable {

	Socket socket;
	JTextArea textArea;
	JButton send, logout;
	JTextField textField;

	Thread thread;

	DataInputStream din;
	DataOutputStream dout;

	String loginName;

	public ClientServer(String login) throws UnknownHostException, IOException {
		super(login);
		loginName = login;

		textArea = new JTextArea(80, 58);
		textField = new JTextField(50);
		send = new JButton("Send");
		logout = new JButton("Logout");

		send.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				try {
					dout.writeUTF(loginName + " " + " DATA "
							+ textField.getText().toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		logout.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				try {
					dout.writeUTF(loginName + " " +" LOGOUT " );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		socket = new Socket("localhost", 5217);

		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());

		dout.writeUTF(loginName);
		dout.writeUTF(loginName + " ---  " + "LOGIN");
		thread = new Thread(this);
		thread.start();

		// setup Jframe
		setupJframe();

	}

	private void setupJframe() {
		setSize(700, 280);

		JPanel panel = new JPanel();
		panel.add(new JScrollPane(textArea));
		panel.add(textField);
		panel.add(send);
		panel.add(logout);

		add(panel);

		setVisible(true);

	}

	public void run() {

		while (true) {
			try {
				textArea.append("\n" + din.readUTF());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		ClientServer clientServer = new ClientServer("User1");

	}

}
