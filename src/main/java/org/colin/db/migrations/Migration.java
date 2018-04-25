package org.colin.db.migrations;

import org.colin.db.DBConnection;

/**
 * Migrations allow for the easy creation and destruction of database schemas.
 */
public abstract class Migration {
    protected DBConnection connection;

    /**
     * Common constructor for dependency injection of database connection (see {@link DBConnection}) for easier testing.
     * @param connection
     */
    public Migration(DBConnection connection) {
        this.connection = connection;
    }

    /**
     * The method is for creational actions upon the database schema.
     */
    public abstract boolean up();

    /**
     * The method is for reversing the actions of {@link Migration#up()}.
     */
    public abstract boolean down();
}
