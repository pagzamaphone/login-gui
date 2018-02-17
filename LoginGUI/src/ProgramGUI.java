import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * ProgramGUI version 2 by Johnny Console
 * 17 February 2018.
 * This program is a basic GUI program that uses Swing and AWT to
 * do the GUI components. This program is a  basic GUI program
 * that has a command interface with multiple commands, to make a dummy pos.
 */

@SuppressWarnings({"serial"})
public class ProgramGUI extends JFrame {

	//Error numbers for the error(int error) method
	private final int USER_LIMIT_REACHED = 0;
	private final int USER_EXISTS = 1;
	private final int USER_NOT_FOUND = 2;
	private final int CMD_NOT_FOUND = 3;
	private final int ALREADY_ADMIN = 4;
	private final int ALREADY_REG = 5;
	private final int PERMISSIONS = 6;

	private static final int FRAME_WIDTH = 300;
	private static final int FRAME_HEIGHT = 145;
	
	//Private nonstatic JComponents for access in this class
	private JTextField user;
	private JTextField pass;
	private JTextField oldUser;
	private JTextField cmd;
	private JFrame frame;
	
	//Private static variables for use in this class
	private static String[] users = LoginGUI.users;
	private static  File file = LoginGUI.file;
	private static String logged = LoginGUI.logged;
	private static String type;
	private int index;
	
	//public constructor for the initial login program to activate the program
	public ProgramGUI(String type, String logged) {
		ProgramGUI.type = type;
		ProgramGUI.logged = logged;
		setTitle("Command Window");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		panel.add(new JLabel("Enter Command: "));
		cmd = new JTextField();
		panel.add(cmd);
		add(panel, BorderLayout.NORTH);
		JPanel button = new JPanel();
		JButton ok = new JButton("Confirm Command");
		JButton logout = new JButton("Log Out");
		ok.addActionListener(new OKListener());
		logout.addActionListener(new LogoutListener());
		button.add(ok);
		button.add(logout);
		add(button, BorderLayout.SOUTH);
		setVisible(true);
		
	}
	//private constructor for use within the program
	private ProgramGUI() {
		setTitle("Command Window");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		panel.add(new JLabel("Enter Command: "));
		cmd = new JTextField();
		panel.add(cmd);
		add(panel, BorderLayout.NORTH);
		JPanel button = new JPanel();
		JButton ok = new JButton("Confirm Command");
		JButton logout = new JButton("Log Out");
		ok.addActionListener(new OKListener());
		logout.addActionListener(new LogoutListener());
		button.add(ok);
		button.add(logout);
		add(button, BorderLayout.SOUTH);
		setVisible(true);
		
	}
	
	//check the command after the button is pushed on the main interface
	private void commandCheck() {
		//add user command
		if(cmd.getText().toLowerCase().equals("adduser")) {
			/*
			 * permissions check: if user is an admin, proceed. If not, error with a permission error.
			 * There will be more user level commands added in version 2, like changing rename to allow a
			 * user to rename themselves, but not any other user. Admins can still rename all users.
			 */
			if(type.equals("admin")) {
				addUser();
			}
			else {
				error(PERMISSIONS);
			}
		}
		//delete user command
		else if(cmd.getText().toLowerCase().equals("deluser")) {
			//permissions check: if user is an admin, proceed. If not, error with a permission error.
			if(type.equals("admin")) {
				delUser();
			}
			else {
				error(PERMISSIONS);
			};
		}
		//rename user command
		else if(cmd.getText().equals("rename")) {
			//permissions check: if user is an admin, proceed. If not, error with a permission error.
			if(type.equals("admin")) {
				rename();
			}
			else {
				error(PERMISSIONS);
			}
		}
		//promote user command
		else if(cmd.getText().equals("promote")) {
			//permissions check: if user is an admin, proceed. If not, error with a permission error.
			if(type.equals("admin")) {
				promote();
			}
			else {
				error(PERMISSIONS);
			}
		}
		//demote user command
		else if(cmd.getText().equals("demote")) {
			//permissions check: if user is an admin, proceed. If not, error with a permission error.
			if(type.equals("admin")) {
				demote();
			}
			else {
				error(PERMISSIONS);
			}
		}
		//if the command that was entered does not match any command, return an error.
		else {
			error(CMD_NOT_FOUND);
		}
	}
	
	//gui for the adduser command is built in this method.
	private void addUser() {
		//check for the user limit, and if it is reached, return an error.
		if(!(users[users.length-1].equals(""))) {
			error(USER_LIMIT_REACHED);
			ProgramGUI.this.dispose();
		}
		else {
			ProgramGUI.this.dispose();
			index = nextUserIndex();
			frame = new JFrame("Adding user #" + (index+1));
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 2));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("Enter Username: "));
			user = new JTextField();
			panel.add(user);
			panel.add(new JLabel("Enter Password: "));
			pass = new JTextField();
			panel.add(pass);
			frame.add(panel, BorderLayout.NORTH);
			JPanel button = new JPanel();
			JButton ok = new JButton("Confirm User");
			ok.addActionListener(new AddListener());
			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ButtonListener());
			button.add(ok);
			button.add(cancel);
			frame.add(button, BorderLayout.SOUTH);
			frame.setVisible(true);
		}
	}
	
	//all errors are dispached from this method
	private void error(int error) {
		//user limit reached error gui
		if(error == USER_LIMIT_REACHED) {
			frame.dispose();
			frame = new JFrame("Error: User Limit Reached");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("You have reached the maximum amount of users (" + users.length + " users)."));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ButtonListener());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setVisible(true);
		}
		//user exists error gui
		else if(error == USER_EXISTS) {
			frame.dispose();
			frame = new JFrame("Error: User Exists");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " + user.getText() + " already exists."));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ButtonListener());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setVisible(true);
		}
		//user not found error gui
		else if(error == USER_NOT_FOUND) {
			frame.dispose();
			frame = new JFrame("Error: User Not Found");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " + user.getText() + " does not exist."));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ButtonListener());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setVisible(true);
		}
		//command not found error gui
		else if(error == CMD_NOT_FOUND) {
			ProgramGUI.this.dispose();
			frame = new JFrame("Error: Invalid Command");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The command " + cmd.getText() + " does not exist."));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ButtonListener());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setVisible(true);
		}
		//user is already admin error gui
		else if(error == ALREADY_ADMIN) {
			frame.dispose();
			frame = new JFrame("Error: Promotion");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " + user.getText() + " is already an admin."));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ButtonListener());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setVisible(true);
		}
		//user is already regular error gui
		else if(error == ALREADY_REG) {
			frame.dispose();
			frame = new JFrame("Error: Demotion");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " + user.getText() + " is already a regular user."));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ButtonListener());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setVisible(true);
		}
		//permissions error gui
		else if(error == PERMISSIONS) {
			ProgramGUI.this.dispose();
			frame = new JFrame("Error: Permissions");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("You do not have permission."));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ButtonListener());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setVisible(true);
		}
	}
	//this method returns the index of the next available user spot in the array
	private int nextUserIndex() {
		int index = 1;
		for(int i = 0; i < users.length; i++) {
			if(users[i].equals("")) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	//this method repopulates the credentials file
	public void repopFile() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file.getName());
		for(int i = 0; i < users.length; i++) {
			if(users[i].equals("")) {
				continue;
			}
			writer.println(users[i]);
		}
		writer.close();
	}
	
	//all operations are confirmed from within this method
	public void confirm(String toConfirm) {
		//confirm user addition gui
		if(toConfirm.equals("add")) {
			frame.dispose();
			frame = new JFrame("User Added Successfully");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " + user.getText() + " has been added"));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(e -> frame.dispose());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
		}
		//confirm user deleted gui
		else if(toConfirm.equals("del")) {
			frame.dispose();
			frame = new JFrame("User Deleted Successfully");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " + user.getText() + " has been deleted"));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(e -> frame.dispose());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
		}
		//confirm user rename gui
		else if(toConfirm.equals("rename")) {
			frame.dispose();
			frame = new JFrame("User Reanmed Successfully");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " +oldUser.getText() + " is now " + user.getText()));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(e -> frame.dispose());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
		}
		//confirm user promotion gui
		else if(toConfirm.equals("promote")) {
			frame.dispose();
			frame = new JFrame("User Promoted Successfully");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " +user.getText() + " is now an admin"));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(e -> frame.dispose());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
		}
		//confirm user demotion gui
		else if(toConfirm.equals("demote")) {
			frame.dispose();
			frame = new JFrame("User Demoted Successfully");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The user " +user.getText() + " is now aregular user"));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(e -> frame.dispose());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
		}
	}
	//delete user gui is built from this method
	public void delUser() {
		ProgramGUI.this.dispose();
		frame = new JFrame("Delete a User");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new JLabel("Delete which user: "));
		user = new JTextField();
		panel.add(user);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		frame.add(panel, BorderLayout.NORTH);
		JPanel button = new JPanel();
		JButton confirm = new JButton("Confirm Delete");
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ButtonListener());
		confirm.addActionListener(new DelListener());
		button.add(confirm);
		button.add(cancel);
		frame.add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	//the rename user gui is built in this method
	private void rename() {
		ProgramGUI.this.dispose();
		frame = new JFrame("Rename a User");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		panel.add(new JLabel("Rename which user: "));
		oldUser = new JTextField();
		user = new JTextField();
		panel.add(oldUser);
		panel.add(new JLabel("Rename to: "));
		panel.add(user);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		frame.add(panel, BorderLayout.NORTH);
		JPanel button = new JPanel();
		JButton confirm = new JButton("Confirm Rename");
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ButtonListener());
		confirm.addActionListener(new RenameListener());
		button.add(confirm);
		button.add(cancel);
		frame.add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	//the promote user gui is built in this method
	private void promote() {
		ProgramGUI.this.dispose();
		frame = new JFrame("Promote a User");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new JLabel("Promote user: "));
		user = new JTextField();
		panel.add(user);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		frame.add(panel, BorderLayout.NORTH);
		JPanel button = new JPanel();
		JButton confirm = new JButton("Confirm Promotion");
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ButtonListener());
		confirm.addActionListener(new PromoteListener());
		button.add(confirm);
		button.add(cancel);
		frame.add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	//the demote user gui is built in this method
	private void demote() {
		ProgramGUI.this.dispose();
		frame = new JFrame("Demote a User");
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		panel.add(new JLabel("Demote user: "));
		user = new JTextField();
		panel.add(user);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		frame.add(panel, BorderLayout.NORTH);
		JPanel button = new JPanel();
		JButton confirm = new JButton("Confirm Demotion");
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ButtonListener());
		confirm.addActionListener(new DemoteListener());
		button.add(confirm);
		button.add(cancel);
		frame.add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	//action listener for the ok button in the command interface
	private class OKListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			commandCheck();
		}
	}
	
	//listener for the logout button on the command interface
	private class LogoutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
			ProgramGUI.this.dispose();
			new LoginGUI();
			}
			catch (IOException ex) {}
		}
	}
	//listener for all cancel buttons in all commands
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			new ProgramGUI();
		}
	}
	//listener for adding a user
	private class AddListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < users.length; i++) {
				if(Arrays.toString(users).contains(user.getText() + ":")) {
					error(USER_EXISTS);
				}
				
				else {
					users[index] = user.getText() + ":" + pass.getText() + ";reg";
					break;
				}
			}
			try {
				repopFile();
				confirm("add");
				new ProgramGUI();
			} catch (FileNotFoundException ex) {}
		}
	}
	//listener for deleting a user
	private class DelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!(Arrays.toString(users).contains(user.getText() + ":"))) {
				error(USER_NOT_FOUND);
			}
			else {
				for(int i = 0; i < users.length; i++) {
					if(users[i].contains(user.getText())) {
						users[i] = "";
						break;
						
					}
				}
				try {
					repopFile();
					confirm("del");
					new ProgramGUI();
				} catch (FileNotFoundException ex) {}
			}
		}
	}
	//listener for renaming a user
	private class RenameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			for(int i = 0; i < users.length; i++) {
				if(!(users[i].equals(""))) {
					if(users[i].substring(0, users[i].indexOf(':')).equals(user.getText())) {
						error(USER_EXISTS);
					}
				}
			}
				for(int i = 0; i < users.length; i++) {
					if(!(users[i].equals(""))) {
						if(users[i].substring(0, users[i].indexOf(':')).equals(oldUser.getText())) {
							users[i] = user.getText() + users[i].substring(users[i].indexOf(':'));
						}
					}
				}
			try {
				repopFile();
				confirm("rename");
				new ProgramGUI();
			} catch(FileNotFoundException ex) {}
		}
	}
	//listener for promoting a user
	private class PromoteListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(Arrays.toString(users).contains(user.getText())) {
				for(int i = 0; i < users.length; i++) {
						if(!(users[i].equals(""))) {
						if(users[i].substring(0, users[i].indexOf(':')).equals(user.getText()) && users[i].contains("admin")) {
							error(ALREADY_ADMIN);
						}
						else if(users[i].substring(0, users[i].indexOf(':')).equals(user.getText()) && users[i].contains("reg")) {
							users[i] = users[i].substring(0, users[i].indexOf(';') + 1) + "admin";
						}
				}
			}
			try {
				repopFile();
				confirm("promote");
				new ProgramGUI();
			}catch (FileNotFoundException ex) {}
		}
		else {
			error(USER_NOT_FOUND);			
			}
		}
	}
	//listener for demoting a user
	private class DemoteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(Arrays.toString(users).contains(user.getText())) {
				for(int i = 0; i < users.length; i++) {
						if(!(users[i].equals(""))) {
						if(users[i].substring(0, users[i].indexOf(':')).equals(user.getText()) && users[i].contains("reg")) {
							error(ALREADY_REG);
						}
						else if(users[i].substring(0, users[i].indexOf(':')).equals(user.getText()) && users[i].contains("admin")) {
							users[i] = users[i].substring(0, users[i].indexOf(';') + 1) + "reg";
						}
				}
			}
			try {
				repopFile();
				confirm("demote");
				new ProgramGUI();
			}catch (FileNotFoundException ex) {}
		}
		else {
			error(USER_NOT_FOUND);			
			}
		}
	}
}