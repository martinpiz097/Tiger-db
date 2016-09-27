///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.martin.powerDB.test;
//
//import java.io.Serializable;
//import java.util.LinkedList;
//
///**
// *
// * @author martin
// */
//public class Carpeta implements Serializable{
//    private String nombre;
//    private LinkedList<Archivo> archivos;
//
//    public Carpeta(int index) {
//        this("carpeta"+(index+1));
//    }
//
//    public Carpeta(String nombre) {
//        this.nombre = nombre;
//        this.archivos = new LinkedList<>();
//    }
//
//    public Carpeta(String nombre, LinkedList<Archivo> archivos) {
//        this.nombre = nombre;
//        this.archivos = archivos;
//    }
//
//    public void addArchivo(Archivo a){
//        archivos.add(a);
//    }
//    
//    public void clear(){
//        archivos.clear();
//    }
//
//    public String getNombre() {
//        return nombre;
//    }
//
//    public LinkedList<Archivo> getArchivos() {
//        return archivos;
//    }
//
//    @Override
//    public String toString() {
//        return "Carpeta{" + "nombre=" + nombre + ", archivos=" + archivos + '}';
//    }
//    
//}
