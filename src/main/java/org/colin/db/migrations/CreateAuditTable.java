package org.colin.db.migrations;

import org.colin.db.DBConnection;

import java.sql.SQLException;

public class CreateAuditTable extends Migration {
    /**
     * Common constructor for dependency injection of database connection (see {@link DBConnection}) for easier testing.
     *
     * @param connection dependency injectable connection to use
     */
    public CreateAuditTable(DBConnection connection) {
        super(connection);
    }

    @Override
    public boolean up() {
        boolean success;
        try {
            success = connection.query("CREATE TABLE IF NOT EXISTS audits (\n" +
                    "audit_id integer PRIMARY KEY" +
                    "file_id integer NOT NULL,\n" +
                    "begin integer NOT NULL,\n" +
                    "end integer NOT NULL,\n" +
                    "context text NOT NULL)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return success;
    }

    @Override
    public boolean down() {
        boolean success;
        try {
            success = connection.query("DROP TABLE IF EXISTS files");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return success;
    }
}
