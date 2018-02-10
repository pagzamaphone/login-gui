import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings({"serial", "unused"})
public class ProgramGUI extends JFrame {

	private static final int USER_LIMIT_REACHED = 0;
	private static final int USER_EXISTS = 1;
	private static final int USER_NOT_FOUND = 3;
	
	private JTextField user;
	private JTextField pass;
	private JTextField oldUser;
	private JTextField cmd;
	private JFrame frame;
	
	private static String[] users = LoginGUI.users;
	private static  File file = LoginGUI.file;
	private String type;
	
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
	}
	
	private void addUser() {
		if(!(users[users.length-1].equals(""))) {
			error(USER_LIMIT_REACHED);
			ProgramGUI.this.dispose();
		}
		else {
			ProgramGUI.this.dispose();
			int index = nextUserIndex();
			frame = new JFrame("Adding user #" + index);
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
			setVisible(true);
		}
	}
	
	private int nextUserIndex() {
		int index = 1;
		for(int i = 0; i < users.length; i++) {
			if(users[i].equals("")) {
				index = i;
			}
		}
		return index;
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
			
		}
	}


}
