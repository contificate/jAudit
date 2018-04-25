package org.colin.db;

import java.sql.*;

public class DBConnection {
    private static DBConnection instance;

    private Connection conn;
    private static String path;

    /**
     * Protocol used for local SQLite database files.
     */
    private static final String PROTOCOL = "jdbc:sqlite:";

    public static synchronized DBConnection getInstance() {
        if(instance == null)
            instance = new DBConnection();

        return instance;
    }

    public boolean query(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.execute(query);
    }

    public boolean isConnected() {
        try {
            return ((conn != null ) && !(conn.isClosed()));
        } catch (SQLException e) {
            return false;
        }
    }

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
