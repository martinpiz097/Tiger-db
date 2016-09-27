///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.martin.powerDB.test;
//
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.List;
//import org.martin.powerDB.db.Database;
//import org.martin.powerDB.exceptions.NullObjectException;
//import org.martin.powerDB.exceptions.TableNotExistsException;
//
///**
// *
// * @author martin
// */
//public class Main {
//    public static void main(String[] args) throws IOException, TableNotExistsException, NullObjectException {
//        Database db = new Database("dbTest");
//        long ti;
//        
//        ti = start();
//        db.createTable("carpeta", Carpeta.class);
//        System.out.println("Creación de tablas: "+finish(ti));
//        
//        ti = start();
//        for (int i = 0; i < 100; i++)
//            db.insert("carpeta", new Carpeta(i));
//        System.out.println("Agregado de 100 elementos: "+finish(ti));
//    
//        ti = start();
//        LinkedList<Carpeta> carpetas = (LinkedList<Carpeta>) db.selectAll("carpeta");
//        System.out.println("Rescate a una lista: "+finish(ti));
//
//        ti = start();
//        db.select("carpeta", 99);
//        System.out.println("Rescatar un objeto por indice: "+finish(ti));
//
//        ti = start();
//        db.select("carpeta", "nombre", "xd");
//        System.out.println("Rescatar un objeto por nombre de campo: "+finish(ti));
//        
//        Carpeta c = carpetas.get(20);
//        ti = start();
//        db.delete("carpeta", c);
//        System.out.println("Eliminar objeto por referencia: "+finish(ti));
//    
//        ti = start();
//        db.delete("carpeta", 98);
//        System.out.println("Eliminar por índice: "+finish(ti));
//    
//        c = carpetas.get(10);
//        ti = start();
//        db.update("carpeta", c, new Carpeta("xd2"));
//        System.out.println("Actualizar por referencia: "+finish(ti));
//    
//        ti = start();
//        db.update("carpeta", 95, new Carpeta("xd3"));
//        System.out.println("Actualizar por índice: "+finish(ti));
//
//        ti = start();
//        db.dropTable("carpeta");
//        System.out.println("Eliminar una tabla: "+finish(ti));
//        
//        ti = start();
//        db.dropDatabase();
//        System.out.println("Eliminar bd: "+finish(ti));
//    }
//    
//    public static long start(){
//        return System.currentTimeMillis();
//    }
//    
//    public static long finish(long ti){
//        return start()-ti;
//    }
//}
