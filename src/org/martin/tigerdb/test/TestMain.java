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
import org.martin.tigerdb.model.Table;

/**
 *
 * @author martin
 */
public class TestMain {
    static long ti, tf;
    
    public static void main(String[] args) throws IOException {
        System.out.print("Limit: ");
        int lim = new java.util.Scanner(System.in).nextInt();
        
        start();
        Database dbPersonas;
        dbPersonas = new Database("dbPersonas");
        finish();
        printMsg("Cargar bd");
        
        start();
        dbPersonas.createTable("persona", Persona.class);
        finish();
        printMsg("Crear tabla");
        
        start();
        for (int i = 0; i < lim; i++) {
            dbPersonas.insertInto("persona", new Persona(i, "nom"+i));
        }
        finish();
        printMsg("insert de "+lim+" elementos");

        start();
        ElectroList<Persona> list = dbPersonas.selectAllFrom("persona");
        for (Persona p : list) {}
        finish();
        printMsg("selectAllFrom");
        
        dbPersonas.setName("dbx");
        start();
        dbPersonas.dropTable("persona");
        finish();
        printMsg("drop table");
    
        start();
        dbPersonas.drop();
        finish();
        printMsg("drop database");
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
    }
    
    public static void printMsg(String msg){
        System.out.println(msg+": "+tf);
    }
    
}
