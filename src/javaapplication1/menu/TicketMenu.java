package javaapplication1.menu;

import javaapplication1.domain.Ticket;
import javaapplication1.domain.User;
import javaapplication1.persistence.TicketDao;
import javaapplication1.persistence.UserDao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class TicketMenu extends JFrame implements ActionListener {

	// class level member objects
	private TicketDao ticketDao = new TicketDao(); // for CRUD operations
	private User user;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("TicketMenu");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	public TicketMenu(User user) {
		this.user = user;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for TicketMenu main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for TicketMenu main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener((e) -> System.exit(0));
		mnuItemUpdate.addActionListener((e) -> updateTicket());
		mnuItemDelete.addActionListener((e) -> deleteTicket());
		mnuItemOpenTicket.addActionListener((e) -> openTicket());
		mnuItemViewTicket.addActionListener((e) -> viewTicket());

		// add any more listeners for any additional sub menu items if desired

	}

	private void prepareGUI() {

		// create jmenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		if (user.isAdmin()) {
			bar.add(mnuAdmin);
		}
		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
		// define a window close operation
		public void windowClosing(WindowEvent wE) {
		    System.exit(0);
		}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}


	private void openTicket() {
		//This method is responsible for opening the ticket
		JFrame frame = new JFrame("Open Ticket");
		String name = JOptionPane.showInputDialog(frame, "What's your name?");

		//Checking if the user has provided a name or not. They are nor restrictions on empty description
		if (name != null && !name.isEmpty()){
			String description = JOptionPane.showInputDialog(frame, "Ticket Description?");

			Ticket ticket = new Ticket(0, description, new Date(), new Date(), user.getId());

			int ticketId = ticketDao.insertTicket(ticket);

			JOptionPane.showMessageDialog(frame, "New ticker id: " + ticketId);
		}
		else {
			JOptionPane.showMessageDialog(frame, "No Name was provided");
		}
		//Once the the task is completed show the tickets
		viewTicket();
	}

	private void viewTicket() {
		// This method is used to display tickets
		Vector<Vector<Object>> data = new Vector<>();

		List<String> columns = new ArrayList<>();
		columns.add(TicketDao.ID);
		columns.add(TicketDao.DESCRIPTION);
		columns.add(TicketDao.START_DATE);
		columns.add(TicketDao.END_DATE);
		columns.add(TicketDao.USER_ID);

		Vector<String> column = new Vector<String>(columns);

		List<Ticket> tickets;

		//If the user is admin display all the tickets in the system
		if (user.isAdmin()) {
			 tickets = ticketDao.getTickets();
		}
		//if the user is not admin show only tickets only for that user.
		else {
			tickets = ticketDao.getTickets(user.getId());
		}

		//Making Jtable to display results
		tickets.forEach((ticket) -> {
			Vector<Object> row = new Vector<>();
			row.add(ticket.getId());
			row.add(ticket.getDescription());
			row.add(ticket.getStartDate());
			row.add(ticket.getEndDate());
			row.add(ticket.getUserId());
			data.add(row);
		});

		JTable table = new JTable(data, column);
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);

		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		//This is important to refresh the table to display new results
		getContentPane().revalidate();

	}

	private void updateTicket() {
		// This method is used by admin to update an existing ticket description
		//A new frame is opened where the prompt ask user to enter the ticket and then new description
		JFrame frame = new JFrame("Update Ticket");
		try {
			Integer ticketId = Integer.parseInt(JOptionPane.showInputDialog(frame, "Ticket id?"));
			String description = JOptionPane.showInputDialog(frame, "Ticket Description?");

			ticketDao.updateTicket(ticketId, description);

			JOptionPane.showMessageDialog(frame, "Updated!");

		}
		//Checking for valid input
		catch (NumberFormatException e){
			JOptionPane.showMessageDialog(frame, "Please Provide Proper Ticket ID");
		}
		//Once the the task is completed show the tickets
		viewTicket();
	}

	private void deleteTicket() {
		// This method is used by admin to delete an existing ticket description
		//A new frame is opened where the prompt ask user to enter the ticket id
		JFrame frame = new JFrame("Delete Ticket");
		try {
			Integer ticketId = Integer.parseInt(JOptionPane.showInputDialog(frame, "Ticket id?"));
			ticketDao.removeTicket(ticketId);
			JOptionPane.showMessageDialog(frame, "Deleted!");
		}
		//Checking for valid Input
		catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Please Provide Proper Ticket ID");
		}
		//Once the the task is completed show the tickets
		viewTicket();
	}
}
