package javaapplication1.persistence;

import javaapplication1.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

//This class is responsible for maintain any connection with the user table only
public class UserDao {

	private static final String TABLE = "huddi_users";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String PASSWORD = "password";
	private static final String ADMIN = "admin";

	private final Connection db;

	public UserDao() {
		db = Database.getConnection();
	}

	public Optional<User> getUser(String name, String password) {
		String query = String.format("SELECT * FROM %s WHERE %s = ? and %s = ?;", TABLE, NAME, PASSWORD);

		try (PreparedStatement stmt = db.prepareStatement(query)) {
			stmt.setString(1, name);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				return Optional.empty();
			}

			return Optional.of(toUser(rs));

		} catch (SQLException sqe) {
			return Optional.empty();
		}
	}

	private static User toUser(ResultSet rs) throws SQLException {
		int id = rs.getInt(ID);
		String name = rs.getNString(NAME);
		String password = rs.getNString(PASSWORD);
		boolean isAdmin = rs.getBoolean(ADMIN);

		return new User(id, name, password, isAdmin);
	}
}
