import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class TillOpenException extends Exception{
	private static final int FRAME_WIDTH = 300;
	private static final int FRAME_HEIGHT = 145;

	public TillOpenException() {
		tillOpenGUI();
	}
	
	private void tillOpenGUI() {
		JFrame frame = new JFrame("Error: Till is Open");
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		panel.add(new JLabel("The Till is Already Open"));
		JButton button = new JButton("OK");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		panel.add(button);
		frame.add(panel, BorderLayout.NORTH);
		frame.setSize(FRAME_HEIGHT, FRAME_WIDTH);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
	}
	
}
