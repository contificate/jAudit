package org.colin.tests;

import org.colin.db.DBConnection;
import org.colin.db.migrations.CreateFileTable;

public class DatabaseTest {

    public static void main(String[] args) {
        DBConnection connection = DBConnection.getInstance();
        new CreateFileTable(connection).up();
    }

}
