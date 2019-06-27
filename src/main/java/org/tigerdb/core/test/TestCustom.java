package org.tigerdb.core.test;

import org.tigerdb.core.model.Database;
import org.tigerdb.core.model.Table;

import java.util.List;

public class TestCustom {
    public static void main(String[] args) {
        Database db = new Database("dbmartin");
        Table<Persona> personas = db.getTable("personas");
        System.out.println(personas.selectCount());

        double avg = db.selectAvgFrom("personas", "edad");
        System.out.println(avg);
    }
}
