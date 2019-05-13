package javaapplication1.menu;

import javaapplication1.domain.User;
import javaapplication1.persistence.UserDao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//controls-label text fields, button
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginMenu extends JFrame {

	private final UserDao userDao;

	public LoginMenu() {

		super("IIT HELP DESK LOGIN");
		userDao = new UserDao();
		setSize(400, 210);
		setLayout(new GridLayout(4, 2));
		setLocationRelativeTo(null); // centers window

		// SET UP CONTROLS
		JLabel lblUsername = new JLabel("Username", JLabel.LEFT);
		JLabel lblPassword = new JLabel("Password", JLabel.LEFT);
		JLabel lblStatus = new JLabel(" ", JLabel.CENTER);
		// JLabel lblSpacer = new JLabel(" ", JLabel.CENTER);

		JTextField txtUname = new JTextField(10);

		JPasswordField txtPassword = new JPasswordField();
		JButton btn = new JButton("Submit");
		JButton btnExit = new JButton("Exit");

		// constraints

		lblStatus.setToolTipText("Contact help desk to unlock password");
		lblUsername.setHorizontalAlignment(JLabel.CENTER);
		lblPassword.setHorizontalAlignment(JLabel.CENTER);
 
		// ADD OBJECTS TO FRAME
		add(lblUsername);// 1st row filler
		add(txtUname);
		add(lblPassword); // 2nd row
		add(txtPassword);
		add(btn); // 3rd row
		add(btnExit);
		add(lblStatus); // 4th row

		btn.addActionListener(new ActionListener() {
			int count = 0; // count agent

			@Override
			public void actionPerformed(ActionEvent e) {
				count = count + 1;
				Optional<User> user = userDao.getUser(txtUname.getText(), txtPassword.getText());
				if (user.isPresent()) {
					new TicketMenu(user.get());
					setVisible(false); // HIDE THE FRAME
					dispose(); // CLOSE OUT THE WINDOW
				} else {
					lblStatus.setText("Try again! " + (3 - count) + " / 3 attempts left");
				}
			}
		});
		btnExit.addActionListener(e -> System.exit(0));

		setVisible(true); // SHOW THE FRAME
	}
}
