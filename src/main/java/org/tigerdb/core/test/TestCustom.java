package org.tigerdb.core.test;

import org.tigerdb.core.model.Cursor;
import org.tigerdb.core.model.Database;

public class TestCustom {
    public static void main(String[] args) {
        Database db = new Database("dbmartin");

        Cursor cursor = db.iterate("personas");
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }
}
