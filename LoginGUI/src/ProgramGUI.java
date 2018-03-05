import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/*
 * ProgramGUI version 2 by Johnny Console
 * 17 February 2018.
 * This program is a basic GUI program that uses Swing and AWT to
 * do the GUI components. This program is a  basic GUI program
 * that has a command interface with multiple commands, to make a dummy pos.
 * 
 * PLEASE NOTE: Card Processing does not charge cards! It is for play use only!
 */

@SuppressWarnings({"serial", "deprecation"})
public class ProgramGUI extends JFrame {

	//Error numbers for the error(int error) method
	private final int USER_LIMIT_REACHED = 0;
	private final int USER_EXISTS = 1;
	private final int USER_NOT_FOUND = 2;
	private final int CMD_NOT_FOUND = 3;
	private final int ALREADY_ADMIN = 4;
	private final int ALREADY_REG = 5;
	private final int PERMISSIONS = 6;
	private final int EXPIRED = 7;

	//Permissons numbers for user-admin commands
	private final int SELF = 0;
	private final int ALL = 1;
	
	private static final int FRAME_WIDTH = 300;
	private static final int FRAME_HEIGHT = 145;
	private static int scope;
	
	//Private nonstatic JComponents for access in this class
	private JTextField user;
	private JPasswordField pass;
	private JTextField oldUser;
	private JTextField cmd;
	private JTextField purch;
	private JTextField change;
	private JTextField tender;
	private JTextField card;
	private JTextField expiry;
	private JTextField cvv;
	private JFrame frame;
	
	//Private static variables for use in this class
	private static String[] users = LoginGUI.users;
	private static  File file = LoginGUI.file;
	private static String logged = LoginGUI.logged;
	private static String type;
	private int index;
	private static boolean isTillOpen = false;
	
	//public constructor for the initial login program to activate the program
	public ProgramGUI(String type, String logged) {
		ProgramGUI.type = type;
		ProgramGUI.logged = logged;
		setTitle("Till Closed");
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
		if(isTillOpen) {
			setTitle("Till Open");
		}
		else {
			setTitle("Till Closed");
		}
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
		else if(cmd.getText().toLowerCase().equals("rename")) {
			//permissions check: if user is an admin, allow the user o chose a user to rename.
			//If not, allow the user to rename themselves only.
			if(type.equals("admin")) {
				rename(ALL);
			}
			else {
				rename(SELF);
			}
		}
		//promote user command
		else if(cmd.getText().toLowerCase().equals("promote")) {
			//permissions check: if user is an admin, proceed. If not, error with a permission error.
			if(type.equals("admin")) {
				promote();
			}
			else {
				error(PERMISSIONS);
			}
		}
		//demote user command
		else if(cmd.getText().toLowerCase().equals("demote")) {
			//permissions check: if user is an admin, proceed. If not, error with a permission error.
			if(type.equals("admin")) {
				demote();
			}
			else {
				error(PERMISSIONS);
			}
		}
		//the rest of the commands are user level, no permission checking
		else if(cmd.getText().toLowerCase().equals("ccprocess")) {
			ccprocess();
		}
		else if(cmd.getText().toLowerCase().equals("cprocess")) {
			cprocess();
		}
		else if(cmd.getText().toLowerCase().equals("opentill")) {
			openTill();
		}
		else if(cmd.getText().toLowerCase().equals("closetill")) {
			closeTill();
		}
		else if(cmd.getText().toLowerCase().equals("opendrawer")) {
			openDrawer();
		}
		else if(cmd.getText().toLowerCase().equals("sale")) {
			sale();
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
			pass = new JPasswordField();
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
		//card expired gui
		else if(error == EXPIRED) {
			frame.dispose();
			frame = new JFrame("Error: Card Expired");
			frame.setSize(390, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The card " + card.getText() + " expired on " + expiry.getText()));
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
			if(scope == ALL) {
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
			else {
				frame.dispose();
				frame = new JFrame("User Reanmed Successfully");
				frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(2, 1));
				panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
				panel.add(new JLabel("You are now " + user.getText()));
				JButton button = new JButton("Return to Main Menu");
				button.addActionListener(e -> frame.dispose());
				panel.add(button);
				frame.add(panel, BorderLayout.NORTH);
				frame.setAlwaysOnTop(true);
				frame.setVisible(true);
			}
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
			panel.add(new JLabel("The user " +user.getText() + " is now a regular user"));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(e -> frame.dispose());
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
		}
		//confirm cc payment
		else if(toConfirm.equals("ccpayment")) {
			frame.dispose();
			frame = new JFrame("Payment Successful");
			frame.setSize(390, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The payment of $" + purch.getText() + " on " + card.getText() + " is successfull"));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
					openDrawer();
				}
			});
			panel.add(button);
			frame.add(panel, BorderLayout.NORTH);
			frame.setAlwaysOnTop(true);
			frame.setVisible(true);
		}
		//confirm cash payment
		else if(toConfirm.equals("cpayment")) {
			frame.dispose();
			frame = new JFrame("Payment Successful");
			frame.setSize(390, FRAME_HEIGHT);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2, 1));
			panel.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));
			panel.add(new JLabel("The payment of $" + purch.getText() + " is successfull"));
			JButton button = new JButton("Return to Main Menu");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
					openDrawer();
				}
			});
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
	private void rename(int scope) {
		ProgramGUI.scope = scope;
		ProgramGUI.this.dispose();
		if(scope == ALL) {
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
		else {
			frame = new JFrame("Rename a User");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1, 2));
			panel.add(new JLabel("Rename to: "));
			user = new JTextField();
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
	//the card processing gui is build in this method
	private void ccprocess() {
		ProgramGUI.this.dispose();
		frame.dispose();
		try {
			if(isTillOpen) {
				frame = new JFrame("Process Payment: Credit Card");
				frame.setSize(390, 175);
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(4, 2));
				panel.add(new JLabel("Purchase amount: "));
				purch = new JTextField();
				panel.add(purch);
				panel.add(new JLabel("Enter card number: "));
				card = new JTextField();
				panel.add(card);
				panel.add(new JLabel("Enter the expiry (MM/YYYY): "));
				expiry = new JTextField();
				panel.add(expiry);
				panel.add(new JLabel("Enter the CVV: "));
				cvv = new JTextField();
				panel.add(cvv);
				panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
				frame.add(panel, BorderLayout.NORTH);
				JPanel button = new JPanel();
				JButton confirm = new JButton("Confirm Payment");
				JButton cancel = new JButton("Cancel");
				cancel.addActionListener(new ButtonListener());
				confirm.addActionListener(new ccListener());
				button.add(confirm);
				button.add(cancel);
				frame.add(button, BorderLayout.SOUTH);
				frame.setVisible(true);
			}
			else {
				throw new TillNotOpenException();
			}
		} catch(TillNotOpenException e) {
			new ProgramGUI();
		}
	}
	
	private void cprocess() {
		ProgramGUI.this.dispose();
		frame.dispose();
		try {
			if(isTillOpen) {
				frame = new JFrame("Process Payment: Cash");
				frame.setSize(FRAME_WIDTH, FRAME_HEIGHT + 10);
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(4, 2));
				panel.add(new JLabel("Purchase amount: "));
				purch = new JTextField();
				panel.add(purch);
				panel.add(new JLabel("Amount Tendered: "));
				tender = new JTextField();
				panel.add(tender);
				panel.add(new JLabel("Change Due: "));
				change = new JTextField();
				change.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent arg0) {
						double ch = Double.parseDouble(tender.getText()) - Double.parseDouble(purch.getText());
						change.setText((int)(ch*100) / 100.0 + "");
					}
				});
				panel.add(change);
				panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
				frame.add(panel, BorderLayout.NORTH);
				JPanel button = new JPanel();
				JButton confirm = new JButton("Confirm Payment");
				JButton cancel = new JButton("Cancel");
				cancel.addActionListener(new ButtonListener());
				confirm.addActionListener(new cListener());
				button.add(confirm);
				button.add(cancel);
				frame.add(button, BorderLayout.SOUTH);
				frame.setVisible(true);
			}
			else {
				throw new TillNotOpenException();
			}
		} catch(TillNotOpenException e) {
			new ProgramGUI();
		}
		
	}
	
	private void openTill() {
		ProgramGUI.this.dispose();
		try {
			if(!isTillOpen) {
				frame = new JFrame("Till Opened");
				frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(2,1));
				panel.add(new JLabel("Your Till is now Open"));
				JButton cancel = new JButton("OK");
				cancel.addActionListener(new ButtonListener());
				panel.add(cancel);
				panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
				frame.add(panel, BorderLayout.NORTH);
				isTillOpen = true;
				frame.setVisible(true);
			}
			else {
				throw new TillOpenException();
			}
		} catch(TillOpenException e) {
			new ProgramGUI();
		}
	}
	
	private void closeTill() {
		ProgramGUI.this.dispose();
		try {
			if(isTillOpen) {
				frame = new JFrame("Till Closed");
				frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(2,1));
				panel.add(new JLabel("Your Till is now closed"));
				JButton cancel = new JButton("OK");
				cancel.addActionListener(new ButtonListener());
				panel.add(cancel);
				panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
				frame.add(panel, BorderLayout.NORTH);
				isTillOpen = false;
				frame.setVisible(true);
			}
			else {
				throw new TillClosedException();
			}
		} catch(TillClosedException e) {
			new ProgramGUI();
		}
	}
	
	private void openDrawer() {
		ProgramGUI.this.dispose();
		try {
			if(isTillOpen) {
				frame = new JFrame("Drawer Open");
				frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				JButton cancel = new JButton("Close Drawer");
				cancel.addActionListener(new ButtonListener());
				frame.add(cancel);
				frame.setVisible(true);
			}
			else {
				throw new TillNotOpenException();
			}
		} catch(TillNotOpenException e) {
			new ProgramGUI();
		}
	}
	
	private void sale() {
		ProgramGUI.this.dispose();
		try {
			if(isTillOpen) {
				frame = new JFrame("Select Purchase Tender");
				frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
				frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				JPanel panel = new JPanel();
				ButtonGroup group = new ButtonGroup();
				JRadioButton cash = new JRadioButton("Cash Tender");
				JRadioButton card = new JRadioButton("Credit/Debit Tender");
				cash.addActionListener(e -> cprocess());
				card.addActionListener(e -> ccprocess());
				group.add(cash);
				group.add(card);
				panel.add(cash);
				panel.add(card);
				frame.add(panel, BorderLayout.NORTH);
				JPanel button = new JPanel();
				JButton cancel = new JButton("Cancel");
				cancel.addActionListener(new ButtonListener());
				button.add(cancel);
				frame.add(button, BorderLayout.SOUTH);
				frame.setVisible(true);
			}
			else {
				throw new TillNotOpenException();
			}
		} catch (TillNotOpenException e) {
			new ProgramGUI();
		}
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
			if(scope == ALL) {
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
			}
			else {
				for(int i = 0; i < users.length; i++) {
					if(!(users[i].equals(""))) {
						if(users[i].substring(0, users[i].indexOf(':')).equals(user.getText())) {
							error(USER_EXISTS);
						}
					}
				}
					for(int i = 0; i < users.length; i++) {
						if(!(users[i].equals(""))) {
							if(users[i].substring(0, users[i].indexOf(':')).equals(logged)) {
								users[i] = user.getText() + users[i].substring(users[i].indexOf(':'));
								logged = user.getText();
							}
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
			if(Arrays.toString(users).contains(user.getText() + ":")) {
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
	
	//listener for processng card payments
	private class ccListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(card.getText().equals("") || expiry.getText().equals("") || purch.getText().equals("") || cvv.getText().equals("")) {
				cvv.setBackground(Color.PINK);
				card.setBackground(Color.PINK);
				expiry.setBackground(Color.PINK);
				purch.setBackground(Color.PINK);
				cvv.repaint();
				card.repaint();
				expiry.repaint();
				purch.repaint();
			}
			else {
				cvv.setBackground(Color.WHITE);
				card.setBackground(Color.WHITE);
				expiry.setBackground(Color.WHITE);
				purch.setBackground(Color.WHITE);
				cvv.repaint();
				card.repaint();
				expiry.repaint();
				purch.repaint();
				
				if(Integer.parseInt(expiry.getText().substring(expiry.getText().indexOf('/') + 1)) < Calendar.YEAR && 
						Integer.parseInt(expiry.getText().substring(0, expiry.getText().indexOf('/'))) < Calendar.MONTH) {
					error(EXPIRED);
				}
				else {
					confirm("ccpayment");
					
				}
			}
		}
	}
	
	
	private class cListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(purch.getText().equals("") || tender.getText().equals("")) {
				purch.setBackground(Color.PINK);
				tender.setBackground(Color.PINK);
				purch.repaint();
				tender.repaint();
			}
			else {
				purch.setBackground(Color.WHITE);
				tender.setBackground(Color.WHITE);
				purch.repaint();
				tender.repaint();
				confirm("cpayment");
			}
		}
	}
}