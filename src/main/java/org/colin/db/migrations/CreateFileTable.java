package org.colin.db.migrations;

import org.colin.db.DBConnection;

import java.sql.SQLException;

/**
 * Migration that creates (and destroys) table used for referencing files.
 */
public class CreateFileTable extends Migration {
    /**
     * Common constructor for dependency injection of database connection (see {@link DBConnection}) for easier testing.
     *
     * @param connection dependency injectable connection to use
     */
    public CreateFileTable(DBConnection connection) {
        super(connection);
    }

    /**
     * Construct the files table
     *
     * @return if migration was successful
     */
    @Override
    public boolean up() {
        boolean success;
        try {
            success = connection.query("CREATE TABLE IF NOT EXISTS files (\n" +
                    "file_id integer PRIMARY KEY,\n" +
                    "path text NOT NULL,\n" +
                    "checksum integer NOT NULL,\n" +
                    "date_added integer NOT NULL)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return success;
    }

    /**
     * Drop the table created by {@link CreateFileTable#up()}.
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
