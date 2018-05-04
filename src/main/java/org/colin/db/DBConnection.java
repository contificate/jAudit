package org.colin.db;

import java.sql.*;

/**
 * Database connection class that acts as a minimal wrapper around a {@link Connection}.
 * This class is a singleton that's designed to connect to a globally registered database path.
 */
public class DBConnection {

    /**
     * Singleton instance of {@link DBConnection} this.
     */
    private static DBConnection instance;

    /**
     * Internal SQL connection that this class acts as a minimal wrapper for.
     */
    private Connection conn;

    /**
     * Protocol used for local SQLite database files.
     */
    private static final String PROTOCOL = "jdbc:sqlite:";

    /**
     * Get instance (or possibly create it) and return it.
     *
     * @return instance of {@link DBConnection}
     */
    public static synchronized DBConnection getInstance() {
        // if private static instance is null, try to connect by calling private constructor
        if (instance == null)
            instance = new DBConnection();

        // return the instance
        return instance;
    }

    /**
     * Execute a raw query
     *
     * @param query the SQL query string
     * @return whether query executed successfully or not
     * @throws SQLException SQL exception that can be thrown for many reasons
     */
    public boolean query(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.execute(query);
    }

    /**
     * Find out of database connection is open
     *
     * @return database connectivity state
     */
    public boolean isConnected() {
        try {
            return ((conn != null) && !(conn.isClosed()));
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Prepare a query
     *
     * @param query prepared SQL query with unbound parameters
     * @return created prepared statement
     * @throws SQLException thrown for a variety of reasons such as connection failure, query invalidity, etc.
     */
    public PreparedStatement prepare(String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    /**
     * Private constructor that initialises inner {@link DBConnection#conn} connection.
     */
    private DBConnection() {
        // get registry
        final GlobalRegistry registry = GlobalRegistry.getInstance();

        // attempt to connect
        try {
            conn = DriverManager.getConnection(PROTOCOL + registry.get(GlobalConstants.DB_PATH));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
