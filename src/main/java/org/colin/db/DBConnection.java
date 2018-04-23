package org.colin.db;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;

    private Connection conn;

    public static synchronized DBConnection getInstance() {
        if(instance == null)
            instance = new DBConnection();

        return instance;
    }

    private DBConnection() {

    }

}
