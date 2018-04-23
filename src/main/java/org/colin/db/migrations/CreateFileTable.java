package org.colin.db.migrations;

import org.colin.db.DBConnection;

/**
 * Migration that creates (and destroys) table used for referencing files.
 */
public class CreateFileTable extends Migration {
    public CreateFileTable(DBConnection connection) {
        super(connection);
    }

    @Override
    public void up() {

    }

    @Override
    public void down() {

    }
}
