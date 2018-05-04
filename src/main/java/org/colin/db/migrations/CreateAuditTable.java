package org.colin.db.migrations;

import org.colin.db.DBConnection;

import java.sql.SQLException;

/**
 * Migration that creates the audit table to store individual audits that reference files
 */
public class CreateAuditTable extends Migration {
    /**
     * Common constructor for dependency injection of database connection (see {@link DBConnection}) for easier testing.
     *
     * @param connection dependency injectable connection to use
     */
    public CreateAuditTable(DBConnection connection) {
        super(connection);
    }

    /**
     * Construct the audits table
     * @return if migration was successful
     */
    @Override
    public boolean up() {
        boolean success;
        try {
            success = connection.query("CREATE TABLE IF NOT EXISTS audits (\n" +
                    "audit_id integer PRIMARY KEY,\n" +
                    "file_id integer NOT NULL,\n" +
                    "begin integer NOT NULL,\n" +
                    "end integer NOT NULL,\n" +
                    "context text NOT NULL,\n" +
                    "comment text NOT NULL)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return success;
    }

    /**
     * Drop the table created by {@link CreateAuditTable#up()}.
     *
     * @return if migration was successful
     */
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
