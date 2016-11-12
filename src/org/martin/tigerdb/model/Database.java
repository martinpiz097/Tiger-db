/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.tigerdb.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.electroList.structure.ElectroList;
import org.martin.lion.exceptions.UnknownFieldException;
import org.martin.lion.system.SysInfo;
import org.martin.tigerdb.exceptions.TableAlreadyExistsException;
import org.martin.tigerdb.exceptions.UnknownTableException;

/**
 *
 * @author martin
 */
public class Database {
    private String name;
    private final File dbFolder;
    private ElectroList<Table> tables = new ElectroList<>();
    
    /**
     * Constructor de clase Database, recibe el nombre de ésta como parámetro
     *  y en caso de que la base de datos no exista se crea una nueva, sino se 
     * cargan los datos.
     * @param name Nombre de la base de datos nueva o existente.
     */
    public Database(String name) {
        this.name = name;
        dbFolder = new File(SysInfo.ROOT_DIR, name);
        if(!dbFolder.exists())
            dbFolder.mkdir();
        loadDB();
    }

    private void loadDB() {
        File[] tblDirs = dbFolder.listFiles(f->f.isDirectory());
        
        // Si hay tablas en la bd.
        if (tblDirs != null) {
            for (int i = 0; i < tblDirs.length; i++) {
                try {
                tables.add(new Table(name, tblDirs[i]));
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Evalua si existe una tabla especificada por su nombre.
     * @param tblName Nombre de la tabla a buscar.
     * @return true si la tabla existe; false en caso contrario.
     */
    public boolean hasTable(String tblName){
        return tables.anyMatch(t->t.getName().equals(tblName));
    }
    
    /**
     * Evalua si existen tablas en la base de datos.
     * @return true si existen tablas en la base de datos; false en caso contrario.
     */
    public boolean hasTables(){
        return !tables.isEmpty();
    }

    /**
     * Devuelve una tabla de acuerdo al nombre entregado como parámetro para
     * trabajar directamente con ésta y reducir tiempos de validación al hacer
     * consultas generales, en caso de no existir este método devuelve null.
     * @param name Nombre de la tabla a buscar.
     * @return Objeto Table con los datos de la tabla si es que existe, sino null.
     */
    public Table getTableBy(String name){
        return tables.findFirst(t->t.getName().equals(name));
    }
    
    /**
     * Devuelve la cantidad de tablas en la base de datos.
     * @return Cantidad de tablas en la base de datos.
     */
    public int getTableCount(){
        return tables.size();
    }
    
    /**
     * Devuelve el número de objetos almacenados en la tabla
     * especificada por su nombre.
     * @param tblName Nombre de la tabla a buscar.
     * @return Cantidad de objetos de la tabla en caso de que exista, 
     * en caso contrario arroja una excepcion en tiempo de ejecución de tipo 
     * UnknownTableException.
     */
    public int selectCountFrom(String tblName){
        Table tbl = getTableBy(tblName);
        if (tbl == null)
            throw new UnknownTableException(tblName);
        return tbl.selectCount();
    }
    
    /**
     * Crea una tabla en la base de datos.
     * @param tblName Nombre de la tabla a crear.
     * @param clazz Cl  ase a la que pertenecerán los objetos de esta tabla(Puede ser 
     * cualquiera). Esta clase debe implementar la interfaz Serializable.
     */
    public void createTable(String tblName, Class<?> clazz) {
        try {
            if (hasTable(tblName))
                throw new TableAlreadyExistsException(tblName);
            
            tables.add(new Table(tblName, clazz, this.name));
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     /**
     * Inserta un objeto en una tabla dada por su nombre. Si la clase del objeto a insertar
     * no coincide con la clase de los objetos de la tabla o si la tabla no existe
     * se lanzará una excepción.
     * @param tblName Tabla solicitada.
     * @param object Objeto a insertar.
     */
    public void insertInto(String tblName, Object object){
        try {
            Table tbl = getTableBy(tblName);
            
            if (tbl == null)
                throw new UnknownTableException(tblName);
                
            else if(!tbl.getObjectClazz().equals(object.getClass()))
                    throw new IncompatibleObjectTypeException(
                            tbl.getObjectClazz().getSimpleName()+"!="+object.getClass().getSimpleName());
            tbl.insert(object);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Inserta uno o más objetos en una tabla dada por su nombre.
     * @param tblName Tabla solicitada.
     * @param objects Objetos a insertar.
     */
    public void insertInto(String tblName, Object... objects){
        for (int i = 0; i < objects.length; i++)
            insertInto(tblName, objects[i]);
    }
    
    /**
     * Inserta todos los objetos de la colección en una tabla dada por su nombre.
     * @param tblName Tabla solicitada.
     * @param collection Colección con los objetos a insertar.
     */
    public void insertInto(String tblName, Collection collection){
        try {
            Table tbl = getTableBy(tblName);
            
            if (tbl == null)
                throw new UnknownTableException(tblName);
            
            tbl.insertFrom(collection);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Devuelve un cursor que accede de manera iterativa a los datos de
     * la tabla entregada como parámetro.
     * @param tblName Tabla a acceder.
     * @return Cursor para acceder a los datos de la tabla especificada.
     */
    public Cursor iterate(String tblName){
        Table tbl = getTableBy(tblName);
        if (tbl == null)
            throw new UnknownTableException(tblName);
        return new Cursor(tbl);
    }
    
    public Object selectFrom(String tblName, int index){
        Table tbl = getTableBy(tblName);
        if (tbl == null)
            throw new UnknownTableException(tblName);
        
        return tbl.selectBy(index);
    }
    
    public ElectroList selectAllFrom(String tblName){
        Table tbl = getTableBy(tblName);
        if (tbl == null)
            throw new UnknownTableException(tblName);
        
        return tbl.selectAll();
    }
    
    public Object selectFirstFrom(String tblName){
        Table tbl = getTableBy(tblName);
        if (tbl == null)
            throw new UnknownTableException(tblName);
        return tbl.selectFirst();
    }
    
    public Object selectLastFrom(String tblName){
        Table tbl = getTableBy(tblName);
        if (tbl == null)
            throw new UnknownTableException(tblName);
        return tbl.selectLast();
    }
        
    public ElectroList selectFrom(String tblName, String fieldName, Object valueToFind) {
        try {
            Table tbl = getTableBy(tblName);
            if (tbl == null)
                throw new UnknownTableException(tblName);
            return tbl.getObjectsBy(fieldName, valueToFind);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void update(String tblName, int index, Object newObject){
        try {
            Table tbl = getTableBy(tblName);
            if (tbl == null)
                throw new UnknownTableException(tblName);
            
            tbl.update(index, newObject);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(String tblName, String fieldName, Object valueToFind, Object newObject) {
        try {
            Table tbl = getTableBy(tblName);
            if (tbl == null)
                throw new UnknownTableException(tblName);
            tbl.update(fieldName, valueToFind, newObject);
        } catch (IllegalArgumentException | IllegalAccessException | IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteAllFrom(String tblName) {
        try {
            Table tbl = getTableBy(tblName);
            if (tbl == null)
                throw new UnknownTableException(tblName);
            tbl.deleteAll();
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteFrom(String tblName, int index) {
        try {
            Table tbl = getTableBy(tblName);
            if (tbl == null)
                throw new UnknownTableException(tblName);
            tbl.deleteAt(index);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteFrom(String tblName, String fieldName, Object valueToFind) {
        try {
            Table tbl = getTableBy(tblName);
            if (tbl == null)
                throw new UnknownTableException(tblName);
            tbl.deleteBy(fieldName, valueToFind);
        } catch (UnknownFieldException | IllegalArgumentException | IllegalAccessException | IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dropTableBy(String tblName){
        Table tbl = getTableBy(tblName);
        if (tbl == null)
            throw new UnknownTableException(tblName);
        tbl.drop();
        tables.remove(tbl);
    }
    
    public void dropAllTables(){
        tables.forEach(Table::drop);
        tables.clear();
    }
    
    public void drop(){
        dropAllTables();
        dbFolder.delete();
    }
    
}
