package javaapplication1.persistence;

import javaapplication1.domain.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

//This class is responsible for query related to the Tickets table only
public class TicketDao {

    private static final String TABLE = "huddi_tickets";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String USER_ID = "user_id";

    private final Connection db;

    public TicketDao() {
        db = Database.getConnection();
    }

    public int insertTicket(Ticket ticket) {
        //Opening a new ticket
        String query = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?);",
                TABLE, ID, DESCRIPTION, START_DATE, END_DATE, USER_ID);

        try (PreparedStatement stmt = db.prepareStatement(query)) {
            int id = (int )(Math.random() * 5000 + 1);
            stmt.setInt(1, id);
            stmt.setString(2, ticket.getDescription());
            stmt.setDate(3, new java.sql.Date(ticket.getStartDate().toInstant().toEpochMilli()));
            stmt.setDate(4, new java.sql.Date(ticket.getEndDate().toInstant().toEpochMilli()));
            stmt.setInt(5, ticket.getUserId());

            stmt.executeUpdate();

            return id;
        } catch (SQLException sqe) {
            throw new RuntimeException("Failed to insert ticket", sqe);
        }
    }

//    public void updateTicket(Ticket ticket) {
//        //updating a ticket
//        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?;",
//                TABLE, DESCRIPTION, START_DATE, END_DATE, ID);
//
//        try (PreparedStatement stmt = db.prepareStatement(query)) {
//            stmt.setString(1, ticket.getDescription());
//            stmt.setDate(2, java.sql.Date.valueOf(ticket.getStartDate().toString()));
//            stmt.setDate(3, java.sql.Date.valueOf(ticket.getEndDate().toString()));
//            stmt.setInt(4, ticket.getId());
//
//            stmt.executeQuery();
//        } catch (SQLException sqe) {
//            throw new RuntimeException("Failed to Update ticket", sqe);
//        }
//    }

    public void updateTicket(int id, String description) {
        //Updating a ticket
        String query = String.format("UPDATE %s SET %s = ? WHERE %s = ?;",
                TABLE, DESCRIPTION, ID);

        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setString(1, description);
            stmt.setInt(2, id);

            stmt.executeUpdate();
        } catch (SQLException sqe) {
            throw new RuntimeException("Failed to Update ticket", sqe);
        }
    }

    public void removeTicket(int ticketId) {
        //Deleting a ticket
        removeTicket(new Ticket(ticketId, null, null, null, 0));
    }

    public void removeTicket(Ticket ticket) {
        String query = String.format("DELETE FROM %s WHERE %s = ?;",
                TABLE, ID);

        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, ticket.getId());

            stmt.executeUpdate();
        } catch (SQLException sqe) {
            throw new RuntimeException("Failed to Remove ticket", sqe);
        }
    }

//    public Optional<Ticket> getTicket(int id) {
//        String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE, ID);
//
//        try (PreparedStatement stmt = db.prepareStatement(query)) {
//            stmt.setInt(1, id);
//            ResultSet rs = stmt.executeQuery();
//
//            if (!rs.next()) {
//                return Optional.empty();
//            }
//
//            return Optional.of(toTicket(rs));
//
//        } catch (SQLException sqe) {
//            return Optional.empty();
//        }
//    }

    public List<Ticket> getTickets() {
        //Getting all the tickets in the system for admin
        String query = String.format("SELECT * FROM %s;", TABLE);

        try (PreparedStatement stmt = db.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            List<Ticket> tickets = new ArrayList<>();

            while (rs.next()) {
                tickets.add(toTicket(rs));
            }

            return tickets;

        }
        //catching any sql exceptions
        catch (SQLException sqe) {
            return Collections.emptyList();
        }
    }

    public List<Ticket> getTickets(int userId) {
        //Getting only for only for a specific user
        String query = String.format("SELECT * FROM %s WHERE %s = ?;", TABLE, USER_ID);

        try (PreparedStatement stmt = db.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            List<Ticket> tickets = new ArrayList<>();

            while (rs.next()) {
                tickets.add(toTicket(rs));
            }

            return tickets;

        }
        //catching any sql exceptions
        catch (SQLException sqe) {
            return Collections.emptyList();
        }
    }

    private static Ticket toTicket(ResultSet rs) throws SQLException {
        int id = rs.getInt(ID);
        String description = rs.getNString(DESCRIPTION);
        Date startDate = rs.getDate(START_DATE);
        Date endDate = rs.getDate(END_DATE);
        int user_id = rs.getInt(USER_ID);


        return new Ticket(id, description, startDate, endDate, user_id);
    }

}
