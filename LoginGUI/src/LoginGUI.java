import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings({ "serial" })
public class LoginGUI extends JFrame {

	//Public Static variables for use with other program classes
	public static File file = new File("credentials.txt");
	public static String[] users = new String[10];
	
	
	//Private Static variables to use all over this program class
	private static Scanner fileScanner; 
	private JTextField user;
	private JTextField pass;

	//Constructor for the LoginGUI
	public LoginGUI() throws IOException {
		//Fill the array with empty strings to avoid a NullPointerExcetion later
		for (int i = 0; i < users.length; i++) {
			users[i] = "";
		}
		
		//If the credentials file needs to be created, ask the user for a username and password to set up.
		if (file.createNewFile()) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			user = new JTextField();
			pass = new JTextField();
			JButton ok = new JButton("Confirm");
			ok.addActionListener(new InitializeListener());
			add(ok, BorderLayout.SOUTH);
			panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
			panel.add(new JLabel("Enter a username: "), BorderLayout.WEST);
			panel.add(user, BorderLayout.EAST);
			panel.add(new JLabel("Enter a password: "), BorderLayout.WEST);
			panel.add(pass, BorderLayout.EAST);
			add(panel, BorderLayout.NORTH);
			setLocationRelativeTo(null);
			setTitle("Initial Configuration - User");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(275, 120);
			setVisible(true);

		} 
		
		//If the credentials file exists, import the file's contents and store in the array for using, then ask the user to log in.
		else {
			try {
				fileScanner = new Scanner(file);
				for(int i = 0;  i < users.length && fileScanner.hasNextLine(); i++) {
					users[i] = fileScanner.nextLine();
				}
			
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			user = new JTextField();
			pass = new JTextField();
			JPanel button = new JPanel();
			JButton ok = new JButton("Confirm");
			JButton cancel = new JButton("Exit Program");
			button.add(ok);
			button.add(cancel);
			cancel.addActionListener(e -> this.dispose());
			ok.addActionListener(new LoginListener());
			add(button, BorderLayout.SOUTH);
			panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
			panel.add(new JLabel("Enter username: "), BorderLayout.WEST);
			panel.add(user, BorderLayout.EAST);
			panel.add(new JLabel("Enter password: "), BorderLayout.WEST);
			panel.add(pass, BorderLayout.EAST);
			add(panel, BorderLayout.NORTH);
			setLocationRelativeTo(null);
			setTitle("Log In");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(275, 120);
			setVisible(true);
			}
			catch(FileNotFoundException e) {}
		}
	}

	//Method to print the admin username once configured
	private void repopFile() throws IOException {
		PrintWriter writer = new PrintWriter(file.getName());
		writer.println(users[0]);
		writer.close();
	}

	//Listener for the initialization
	private class InitializeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (user.getText().equals("") || pass.getText().equals("")) {

			} 
			else {
				users[0] = user.getText() + ":" + pass.getText() + ";admin";

				LoginGUI.this.dispose();
				try {
					repopFile();
					new LoginGUI();
				}
				catch (IOException e) {}
			}
		}
	}
	
	//Listener for the login
	private class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			for(int i = 0; i < users.length; i++) {
				if(Arrays.toString(users).contains(user.getText() + ":" + pass.getText() + ";admin") ||
					Arrays.toString(users).contains(user.getText() + ":" + pass.getText() + ";reg")) {
					if(users[i].equals(user.getText() + ":" + pass.getText() + ";admin")) {
						LoginGUI.this.dispose();
						new ProgramGUI("admin");
					}
					else if(users[i].equals(user.getText() + ":" + pass.getText() + ";reg")) {
						LoginGUI.this.dispose();
						new ProgramGUI("reg");
					}
				}
			}
		}
	}
}
