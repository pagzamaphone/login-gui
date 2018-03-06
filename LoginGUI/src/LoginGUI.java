import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * LoginGUI version 2 by Johnny Console
 * 17 February 2018.
 * This program is a basic GUI login program that uses Swing and AWT to
 * do the GUI components. This program leads into another basic GUI program
 * that has a command interface with multiple commands, to create a dummy pos.
 */

@SuppressWarnings({ "serial"})
public class LoginGUI extends JFrame {

	//Public Static variables for use with other program classes
	public static File file = new File("credentials.txt");
	public static String[] users = new String[10];
	public static String logged;
	
	
	//Private Static variables to use all over this program class
	private static Scanner fileScanner; 
	private JTextField user;
	private JPasswordField pass;
	private static final int FRAME_WIDTH = 300;
	private static final int FRAME_HEIGHT = 145;
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
			pass = new JPasswordField();
			user.setOpaque(true);
			pass.setOpaque(true);
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
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
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
			pass = new JPasswordField();
			user.setOpaque(true);
			pass.setOpaque(true);
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
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
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
	
	private String getPassword(char[] array) {
		String string = "";
		for(int i = 0; i < array.length; i++) {
			string = string + array[i];
		}
		return string;
		
	}

	//Listener for the initialization
	private class InitializeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			char[] password = pass.getPassword();
			user.setBackground(Color.WHITE);
			pass.setBackground(Color.WHITE);
			user.repaint();
			pass.repaint();
			
			if (user.getText().equals("") || getPassword(password).equals("")) {
				user.setBackground(Color.PINK);
				pass.setBackground(Color.PINK);
				user.repaint();
				pass.repaint();
			} 
			else {
				users[0] = user.getText() + ":" + getPassword(password) + ";admin";

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
			char[] password = pass.getPassword();
			for(int i = 0; i < users.length; i++) {
				if(Arrays.toString(users).contains(user.getText() + ":" + getPassword(password) + ";admin") ||
					Arrays.toString(users).contains(user.getText() + ":" + getPassword(password) + ";reg")) {
					user.setBackground(Color.WHITE);
					pass.setBackground(Color.WHITE);
					user.repaint();
					pass.repaint();
					if(users[i].equals(user.getText() + ":" + getPassword(password) + ";admin")) {
						logged = user.getText();
						LoginGUI.this.dispose();
						new ProgramGUI("admin", logged);
					}
					else if(users[i].equals(user.getText() + ":" + getPassword(password) + ";reg")) {
						logged = user.getText();
						LoginGUI.this.dispose();
						new ProgramGUI("reg", logged);
					}
				}
				else {
					user.setBackground(Color.PINK);
					pass.setBackground(Color.PINK);
					user.repaint();
					pass.repaint();
				}
			}
		}
	}
}
