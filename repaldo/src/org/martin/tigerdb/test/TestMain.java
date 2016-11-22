///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.martin.tigerdb.test;
//
//import java.io.File;
//import java.io.IOException;
//import org.martin.electroList.structure.ElectroList;
//import org.martin.lion.store.StoreManager;
//import org.martin.tigerdb.model.Cursor;
//import org.martin.tigerdb.model.Database;
//import org.martin.tigerdb.model.Table;
//
///**
// *
// * @author martin
// */
//public class TestMain {
//    static long ti, tf;
//    
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        execTestProblemaLista();
//        System.out.print("Limit: ");
//        int lim = new java.util.Scanner(System.in).nextInt();
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
//        start();
//        for (int i = 0; i < lim; i++)
//            dbPersonas.insertInto("persona", new Persona(
//                    (int) (dbPersonas.selectCountFrom("persona")+1), "nom"+i));
//        
//        finish();
//        printMsg("insert de "+lim+" elementos");
//        
//        System.out.println("Max: "+dbPersonas.selectMaxFrom("persona", "edad"));
//        System.out.println("Min: "+dbPersonas.selectMinFrom("persona", "edad"));
//        System.out.println("Sum: "+dbPersonas.selectSumFrom("persona", "edad"));
//        System.out.println("Avg: "+dbPersonas.selectAvgFrom("persona", "edad"));
////        Cursor cursor = dbPersonas.getCursorFrom("persona");
////        while (cursor.hasNext()) {
////            System.out.println(cursor.next());
////        }
//
//        Persona selectFirstFrom = (Persona) dbPersonas.selectFirstFrom("persona");
//        System.out.println("Select first: "+selectFirstFrom);
//        Persona first = (Persona) dbPersonas.selectFirstFrom("persona", "name", "nom4");
//        System.out.println("First search: "+first);
//        
//        
//        start();
//        ElectroList<Persona> list = dbPersonas.selectAllFrom("persona");
//        for (Persona p : list) {
//            System.out.println(p);
//        }
//        finish();
//        printMsg("selectAllFrom");
//
//        long selectMaxFrom = dbPersonas.selectMaxFrom("persona", "name");
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
//    private static void execTestProblemaLista() throws IOException, ClassNotFoundException {
//        Database db = new Database("dbPersonas");
//    
//        Table<Persona> tbl1 = db.getTable("persona");
//        
//        tbl1.insert(new Persona(1, "nom1"));
//        tbl1.insert(new Persona(2, "nom2"));
//        tbl1.insert(new Persona(3, "nom3"));
//        
//        Table tbl2 = db.getTable("persona");
//        
////        ElectroList<Persona> selectAll = db.selectAllFrom("persona");
////        ElectroList<Persona> selectTblGen = tbl1.selectAll();
////        ElectroList selectTblO = tbl2.selectAll();
////        ElectroList<Persona> objectsStoreGen = tbl1.getStoreManager().getObjects();
////        ElectroList<Persona> objectsStoreO = tbl2.getStoreManager().getObjects();
////        StoreManager<Persona> store = new StoreManager<>(Persona.class, new File(db.getStorePath(), "persona"));
////        
////        System.out.println("ListDB: "+selectAll);
////        System.out.println("ListTblGeneric: "+selectTblGen);
////        System.out.println("ListTblObject: "+selectTblO);
////        System.out.println("ListStoreGen: "+objectsStoreGen);
////        System.out.println("ListStoreObject: "+objectsStoreO);
////        System.out.println("ListStorePuro: "+store.getObjects());
//        Persona selectFirst = (Persona) db.selectFirstFrom("persona", "name", "nom1");
//    }
//    
//}
