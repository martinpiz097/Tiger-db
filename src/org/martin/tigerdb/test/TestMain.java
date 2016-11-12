/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.tigerdb.test;

import java.io.IOException;
import org.martin.electroList.structure.ElectroList;
import org.martin.lion.store.StoreManager;
import org.martin.tigerdb.exceptions.TableAlreadyExistsException;
import org.martin.tigerdb.model.Database;
import org.martin.tigerdb.model.Cursor;

/**
 *
 * @author martin
 */
public class TestMain {
    static long ti, tf;
    
    public static void main(String[] args) throws TableAlreadyExistsException, IOException {
        Database dbPersonas = new Database("dbPersonas");
        //dbPersonas.createTable("persona", Persona.class);
        System.out.println(dbPersonas.selectCountFrom("persona"));
        for (int i = 0; i < 10; i++) {
            dbPersonas.insertInto("persona", new Persona(i, "nom"+i));
        }
        Cursor cursor = dbPersonas.iterate("persona");
        
        int counter = 0;
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
            cursor.remove();
        }
    }
    
    public static long currentTime(){
        return System.currentTimeMillis();
    }
    
    public static void start(){
        ti = currentTime();
    }
    
    public static void finish(){
        tf = currentTime();
        tf-=ti;
        System.out.println(tf);
    }
}
