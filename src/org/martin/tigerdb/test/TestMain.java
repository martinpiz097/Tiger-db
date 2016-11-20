///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.martin.tigerdb.test;
//
//import java.io.IOException;
//import org.martin.electroList.structure.ElectroList;
//import org.martin.tigerdb.model.Cursor;
//import org.martin.tigerdb.model.Database;
//
///**
// *
// * @author martin
// */
//public class TestMain {
//    static long ti, tf;
//    
//    public static void main(String[] args) {
//        System.out.print("Limit: ");
//        //int lim = new java.util.Scanner(System.in).nextInt();
//        
//        start();
//        Database dbPersonas;
//        dbPersonas = new Database("dbPersonas");
//        finish();
//        printMsg("Cargar bd");
//        
//        start();
//        if (!dbPersonas.hasTable("persona"))
//            dbPersonas.createTable("persona", Persona.class);
//        
//        finish();
//        //10printMsg("Crear tabla");
//        
////        start();
////        for (int i = 0; i < lim; i++) {
////            dbPersonas.insertInto("persona", new Persona(i, "nom"+i));
////        }
////        finish();
////        printMsg("insert de "+lim+" elementos");
//        
//        System.out.println("Max: "+dbPersonas.selectMaxFrom("persona", "edad"));
//        System.out.println("Min: "+dbPersonas.selectMinFrom("persona", "edad"));
//        System.out.println("Sum: "+dbPersonas.selectSumFrom("persona", "edad"));
//        System.out.println("Avg: "+dbPersonas.selectAvgFrom("persona", "edad"));
//        System.exit(0);
//        Cursor cursor = dbPersonas.getCursorFrom("persona");
//        while (cursor.hasNext()) {
//            System.out.println(cursor.next());
//        }
//        System.exit(0);
//        
//        start();
//        ElectroList<Persona> list = dbPersonas.selectAllFrom("persona");
//        for (Persona p : list) {}
//        finish();
//        printMsg("selectAllFrom");
//        
//        dbPersonas.setName("dbx");
//        start();
//        dbPersonas.dropTable("persona");
//        finish();
//        printMsg("drop table");
//    
//        start();
//        dbPersonas.drop();
//        finish();
//        printMsg("drop database");
//    }
//    
//    public static long currentTime(){
//        return System.currentTimeMillis();
//    }
//    
//    public static void start(){
//        ti = currentTime();
//    }
//    
//    public static void finish(){
//        tf = currentTime();
//        tf-=ti;
//    }
//    
//    public static void printMsg(String msg){
//        System.out.println(msg+": "+tf);
//    }
//    
//}
