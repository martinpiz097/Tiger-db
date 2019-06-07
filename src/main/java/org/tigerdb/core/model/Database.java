/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tigerdb.core.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mpizutil.electrolist.structure.ElectroList;
import org.tigerdb.core.exceptions.IncompatibleObjectTypeException;
import org.tigerdb.core.exceptions.TableAlreadyExistsException;
import org.tigerdb.core.exceptions.UnknownTableException;
import org.tigerdb.lion.exceptions.UnknownFieldException;
import org.tigerdb.lion.system.SysInfo;

/**
 *
 * @author martin
 */
public class Database {
    private String name;
    private File dbFolder;
    private final ElectroList<Table> tables = new ElectroList<>();

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

    public Database(File dbFolder) {
        this.name = dbFolder.getName();
        this.dbFolder = dbFolder;
        if (!dbFolder.exists())
            dbFolder.mkdirs();
        else
            loadDB();
    }

    public Database(File parentFolder, String dbName){
        this(new File(parentFolder, dbName));
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
     * Evalua si la tabla solicitada por su nombre está vacia (no tiene registros).
     * @param tblName Nombre de la tabla solicitada
     * @return true si la tabla está vacía; false en caso contrario.
     */
    public boolean isTableEmpty(String tblName){
        Table tbl = getTable(tblName);
        return tbl.isEmpty();
    }
    
    public String getStorePath(){
        try {
            return dbFolder.getCanonicalPath();
        } catch (IOException ex) {
            return null;
        }
    }

    public File getStoreFolder(){
        return dbFolder;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        File newFolder = new File(dbFolder.getParentFile(), name);
        dbFolder.renameTo(newFolder);
        dbFolder = newFolder;
    }

    /**
     * Devuelve una tabla de acuerdo al nombre entregado como parámetro para
     * trabajar directamente con ésta y reducir tiempos de validación al hacer
     * consultas generales, en caso de no existir este método devuelve null.
     * @param tblName Nombre de la tabla a buscar.
     * @return Objeto Table con los datos de la tabla si es que existe, sino null.
     */
    public Table getTable(String tblName){
        Table tbl = tables.findFirst(t->t.getName().equals(tblName));
        if (tbl == null)
            throw new UnknownTableException(tblName);
        return tbl;
    }
    
    /**
     * Devuelve la cantidad de tablas en la base de datos.
     * @return Cantidad de tablas en la base de datos.
     */
    public int getTableCount(){
        return tables.size();
    }
    
    public Cursor getCursorFrom(String tblName){
        Table tbl = getTable(tblName);
        return new Cursor(tbl);
    }
    
    /**
     * Devuelve el número de objetos almacenados en la tabla
     * especificada por su nombre.
     * @param tblName Nombre de la tabla a buscar.
     * @return Cantidad de objetos de la tabla en caso de que exista, 
     * en caso contrario arroja una excepcion en tiempo de ejecución de tipo 
     * UnknownTableException.
     */
    public long selectCountFrom(String tblName){
        Table tbl = getTable(tblName);
        return tbl.selectCount();
    }
    
    public long selectSumFrom(String tblName, String fieldName){
        Table tbl = getTable(tblName);
        return tbl.selectSumBy(fieldName);
    }
    
    /**
     * Entrega el mayor valor númerico comparando los valores
     * de un campo específico
     * @param tblName Nombre de la tabla objetivo.
     * @param fieldName Nombre del campo numérico objetivo.
     * @return El valor máximo devuelto.
     */
    
    public long selectMaxFrom(String tblName, String fieldName){
        Table tbl = getTable(tblName);
        return tbl.selectMaxBy(fieldName);
    }
    
    public long selectMinFrom(String tblName, String fieldName){
        Table tbl = getTable(tblName);
        return tbl.selectMinBy(fieldName);
    }
    
    public double selectAvgFrom(String tblName, String fieldName){
        Table tbl = getTable(tblName);
        return tbl.selectAvgBy(fieldName);
    }
    
    /**
     * Agrega una nueva tabla a la base de datos (Lanza una excepción en
     * tiempo de ejecución si el nombre de la tabla ya existe).
     * @param tbl Objeto del tipo Table a agregar.
     */
    public void insertTable(Table tbl){
        if (getTable(tbl.getName()) != null)
            throw new TableAlreadyExistsException("Ya existe una tabla con el "
                    + "nombre "+tbl.getName());
        tables.add(tbl);
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
            
            tables.add(new Table(tblName, clazz, dbFolder));
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
            Table tbl = getTable(tblName);
            
            if(!tbl.isInstance(object))
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
            Table tbl = getTable(tblName);
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
        Table tbl = getTable(tblName);
        return new Cursor(tbl);
    }
    
    /**
     * Devuelve un objeto de una tabla por su índice.
     * @param tblName
     * @param index
     * @return 
     */
    public Object selectFrom(String tblName, int index){
        Table tbl = getTable(tblName);
        return tbl.selectBy(index);
    }
    
    public ElectroList selectAllFrom(String tblName){
        Table tbl = getTable(tblName);
        return tbl.selectAll();
    }
    
    public Object selectFirstFrom(String tblName){
        Table tbl = getTable(tblName);
        return tbl.selectFirst();
    }
    
    public Object selectLastFrom(String tblName){
        Table tbl = getTable(tblName);
        return tbl.selectLast();
    }
        
    public ElectroList selectFrom(String tblName, String fieldName, Object valueToFind) {
        try {
            Table tbl = getTable(tblName);
            return tbl.getObjectsBy(fieldName, valueToFind);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Object selectFirstFrom(String tblName, String fieldName, Object valueToFind){
        Table tbl = getTable(tblName);
        return tbl.selectFirstBy(fieldName, valueToFind);
    }
    
    public void update(String tblName, int index, Object newObject){
        try {
            Table tbl = getTable(tblName);
            tbl.update(index, newObject);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(String tblName, String fieldName, Object valueToFind, Object newObject) {
        try {
            Table tbl = getTable(tblName);
            tbl.update(fieldName, valueToFind, newObject);
        } catch (IllegalArgumentException | IllegalAccessException | IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(String tblName, Object oldObj, Object newObj){
        try {
            Table tbl = getTable(tblName);
            tbl.update(oldObj, newObj);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteAllFrom(String tblName) {
        try {
            Table tbl = getTable(tblName);
            tbl.deleteAll();
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteFrom(String tblName, int index) {
        try {
            Table tbl = getTable(tblName);
            tbl.deleteAt(index);
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteFrom(String tblName, String fieldName, Object valueToFind) {
        try {
            Table tbl = getTable(tblName);
            tbl.deleteBy(fieldName, valueToFind);
        } catch (UnknownFieldException | IllegalArgumentException | IllegalAccessException | IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dropTable(String tblName){
        Table tbl = getTable(tblName);
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
    
    public void showTables(){
        System.out.println("Cantidad de tablas: "+tables.size());
        System.out.println("-------------------------");
        for (Table table : tables)
            table.show();
        
    }
    
}
