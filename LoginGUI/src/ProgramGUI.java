import java.awt.*;
import java.io.File;
import javax.swing.*;

@SuppressWarnings({"serial", "unused"})
public class ProgramGUI extends JFrame {

	private JTextField user;
	private JTextField pass;
	private JTextField oldUser;
	private JTextField cmd;
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
		//ok.addActionListener(new OKListener());
		//logout.addActionListener(new LogoutListener());
		button.add(ok);
		button.add(logout);
		add(button, BorderLayout.SOUTH);
		setVisible(true);
		
	}
	
	private ProgramGUI() {
		
	}


}
