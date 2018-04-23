package org.colin.db.migrations;

import org.colin.db.DBConnection;

public abstract class Migration {
    protected DBConnection connection;

    public Migration(DBConnection connection) {
        this.connection = connection;
    }

    public abstract void up();
    public abstract void down();
}
