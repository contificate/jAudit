package org.colin.db.migrations;

import org.colin.db.DBConnection;

/**
 * Migrations allow for the easy creation and destruction of database schemas.
 */
public abstract class Migration {

    /**
     * Database connection wrapper
     */
    protected DBConnection connection;

    /**
     * Common constructor for dependency injection of database connection (see {@link DBConnection}) for easier testing.
     *
     * @param connection dependency injectable connection to be used by the migration
     */
    public Migration(DBConnection connection) {
        this.connection = connection;
    }

    /**
     * The method is for creational actions upon the database schema.
     *
     * @return whether the migration was completed successfully
     */
    public abstract boolean up();

    /**
     * The method is for reversing the actions of {@link Migration#up()}.
     *
     * @return whether the migration's reversal was completed successfully
     */
    public abstract boolean down();
}
