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

@SuppressWarnings({"serial", "unused"})
public class ProgramGUI extends JFrame {

	//Error numbers for the error(int error) method
	private final int USER_LIMIT_REACHED = 0;
	private final int USER_EXISTS = 1;
	private final int USER_NOT_FOUND = 2;
	private final int CMD_NOT_FOUND = 3;
	private final int ALREADY_ADMIN = 4;
	private final int ALREADY_REG = 5;
	private final int PERMISSIONS = 6;
	
	//Private nonstatic JComponents for access in this class
	private JTextField user;
	private JTextField pass;
	private JTextField oldUser;
	private JTextField cmd;
	private JFrame frame;
	
	//Private static variables for use in this class
	private static String[] users = LoginGUI.users;
	private static  File file = LoginGUI.file;
	private String type;
	private int index;
	
	public ProgramGUI(String type) {
		this.type = type;
		setTitle("Command Window");
		setSize(275, 120);
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
	
	private ProgramGUI() {
		setTitle("Command Window");
		setSize(275, 120);
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
	
	private void commandCheck() {
		
		if(cmd.getText().toLowerCase().equals("adduser")) {
			addUser();
		}
		else if(cmd.getText().toLowerCase().equals("deluser")) {
			delUser();
		}
		else if(cmd.getText().equals("rename")) {
			rename();
		}
		else if(cmd.getText().equals("promote")) {
			promote();
		}
		else if(cmd.getText().equals("demote")) {
			demote();
		}
		else {
			error(CMD_NOT_FOUND);
		}
	}
	
	private void addUser() {
		if(!(users[users.length-1].equals(""))) {
			error(USER_LIMIT_REACHED);
			ProgramGUI.this.dispose();
		}
		else {
			ProgramGUI.this.dispose();
			index = nextUserIndex();
			frame = new JFrame("Adding user #" + (index+1));
			frame.setSize(275, 120);
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
	
	private void error(int error) {
		if(error == USER_LIMIT_REACHED) {
			frame.dispose();
			frame = new JFrame("Error: User Limit Reached");
			frame.setSize(275, 120);
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
		else if(error == USER_EXISTS) {
			frame.dispose();
			frame = new JFrame("Error: User Exists");
			frame.setSize(275, 120);
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
		else if(error == USER_NOT_FOUND) {
			frame.dispose();
			frame = new JFrame("Error: User Not Found");
			frame.setSize(275, 120);
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
		else if(error == CMD_NOT_FOUND) {
			frame.dispose();
			frame = new JFrame("Error: Invalid Command");
			frame.setSize(275, 120);
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
		else if(error == ALREADY_ADMIN) {
			frame.dispose();
			frame = new JFrame("Error: Promotion");
			frame.setSize(275, 120);
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
		else if(error == ALREADY_REG) {
			frame.dispose();
			frame = new JFrame("Error: Demotion");
			frame.setSize(275, 120);
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
	}
	
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
	
	public void confirm(String toConfirm) {
		if(toConfirm.equals("add")) {
			frame.dispose();
			frame = new JFrame("User Added Successfully");
			frame.setSize(275, 120);
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
		else if(toConfirm.equals("del")) {
			frame.dispose();
			frame = new JFrame("User Deleted Successfully");
			frame.setSize(275, 120);
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
		else if(toConfirm.equals("rename")) {
			frame.dispose();
			frame = new JFrame("User Reanmed Successfully");
			frame.setSize(275, 120);
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
		else if(toConfirm.equals("promote")) {
			frame.dispose();
			frame = new JFrame("User Promoted Successfully");
			frame.setSize(275, 120);
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
		else if(toConfirm.equals("demote")) {
			frame.dispose();
			frame = new JFrame("User Demoted Successfully");
			frame.setSize(275, 120);
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
	
	public void delUser() {
		ProgramGUI.this.dispose();
		frame = new JFrame("Delete a User");
		frame.setSize(275, 120);
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
	
	private void rename() {
		ProgramGUI.this.dispose();
		frame = new JFrame("Rename a User");
		frame.setSize(275, 120);
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
	
	private void promote() {
		ProgramGUI.this.dispose();
		frame = new JFrame("Promote a User");
		frame.setSize(275, 120);
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
	
	private void demote() {
		ProgramGUI.this.dispose();
		frame = new JFrame("Demote a User");
		frame.setSize(275, 120);
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
	private class OKListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			commandCheck();
		}
	}
	
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
	
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			new ProgramGUI();
		}
	}
	
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