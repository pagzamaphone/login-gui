import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings({"serial","unused","resource"})
public class LoginGUI extends JFrame {

	public static File creds = new File("credentials.txt");
	public static String[] users;
	public static String type;
	
	private Scanner scanner = new Scanner(System.in);
	private static int count = 0;

	
	public LoginGUI() throws IOException {
		if(creds.createNewFile()) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2,2));
			JTextField user = new JTextField();
			JTextField pass = new JTextField();
			JButton ok = new JButton("Confirm");
			ok.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					users[0] = user.getText() + ":" + pass.getText() + ";admin";
					try {
					PrintWriter writer = new PrintWriter(creds);
					writer.println(users[0]);
					LoginGUI.this.dispose();
						new LoginGUI();
					} catch (IOException e) {}
				}
				
			});
			add(ok, BorderLayout.SOUTH);
			
			panel.add(new JLabel("Enter a username: "),BorderLayout.WEST);
			panel.add(user, BorderLayout.EAST);
			panel.add(new JLabel("Enter a password: "), BorderLayout.WEST);
			panel.add(pass, BorderLayout.EAST);
			add(panel, BorderLayout.NORTH);
			setLocationRelativeTo(null);
			setTitle("Initial Configuration - User");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(250,200);
			setVisible(true);
			
		}
	}
}
