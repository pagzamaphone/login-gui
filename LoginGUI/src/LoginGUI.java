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

	public static File file = new File("credentials.txt");
	public static String[] users = new String[10];
	public static String type;
	

	private JTextField user;
	private JTextField pass;
	
	public LoginGUI() throws IOException {
		for(int i = 0; i < users.length; i++) {
			users[i] = "";
		}
		if(file.createNewFile()) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2,2));
			user = new JTextField();
			pass = new JTextField();
			JButton ok = new JButton("Confirm");
			ok.addActionListener(new InitializeListener());
			add(ok, BorderLayout.SOUTH);
			panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
			panel.add(new JLabel("Enter a username: "),BorderLayout.WEST);
			panel.add(user, BorderLayout.EAST);
			panel.add(new JLabel("Enter a password: "), BorderLayout.WEST);
			panel.add(pass, BorderLayout.EAST);
			add(panel, BorderLayout.NORTH);
			setLocationRelativeTo(null);
			setTitle("Initial Configuration - User");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(275,120);
			setVisible(true);
			
		}
		else {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2,2));
			user = new JTextField();
			pass = new JTextField();
			JPanel button = new JPanel();
			JButton ok = new JButton("Confirm");
			JButton cancel = new JButton("Cancel");
			button.add(ok);
			button.add(cancel);
			cancel.addActionListener(e -> this.dispose());
			//ok.addActionListener(new LoginListener());
			add(button, BorderLayout.SOUTH);
			panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
			panel.add(new JLabel("Enter username: "),BorderLayout.WEST);
			panel.add(user, BorderLayout.EAST);
			panel.add(new JLabel("Enter password: "), BorderLayout.WEST);
			panel.add(pass, BorderLayout.EAST);
			add(panel, BorderLayout.NORTH);
			setLocationRelativeTo(null);
			setTitle("Log In");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setSize(275,120);
			setVisible(true);
		}
	}
	private void repopFile() throws IOException {
		PrintWriter writer = new PrintWriter(file.getName());
		for(int i = 0; i < users.length; i++) {
			if(users[i].equals("")) {
				continue;
			}
			writer.println(users[i]);
		}
	}
	
	private class InitializeListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0){
				if(user.getText().equals("") || pass.getText().equals("")) {
					
				}
				else {
				users[0] = user.getText() + ":" + pass.getText() + ";admin";
				
				LoginGUI.this.dispose();
					try {
						repopFile();
						new LoginGUI();
					} catch (IOException e) {}
				}
			}
	}
}

