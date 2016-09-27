/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.powerDB.db;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.martin.powerDB.config.Info;
import org.martin.powerDB.exceptions.NullObjectException;
import org.martin.powerDB.exceptions.TableNotExistsException;
import org.martin.powerDB.system.SysInfo;
import org.martin.serializer.db.Serializer;
import org.martin.serializer.db.Table;

/**
 *
 * @author martin
 */
public class Database {
    private final File dbFolder;
    private final String name;
    private final Info info;
    private final Serializer<Table> rootSerializer;
    private LinkedList<Table> tables;

    public Database(String name) throws IOException {
        this.dbFolder = new File(SysInfo.rootDir, name);
        if(!dbFolder.exists()) dbFolder.mkdir();
        
        this.info = new Info(dbFolder);
        if (!info.hasName()) info.addProperty("name", name);
        this.name = name;
        rootSerializer = new Serializer<>(SysInfo.rootDir, Table.class);
        loadDB();
    }
    
    /**
     * Carga los datos de la base de datos guardados en los archivos.
     */
    private void loadDB(){
        if(rootSerializer.hasSerializedObjects())
            tables = (LinkedList<Table>) rootSerializer.getListObjects();
        
        else
            tables = new LinkedList<>();
    }
    
    /**
     * Actualiza el archivo que guarda las tablas de la base de datos.
     */
    private void updateDB(){
        rootSerializer.serializeFrom(tables);
    }
    
    /**
     * Evalua si la base de datos tiene o no tablas.
     * @return true si tiene tablas almacenadas; false en caso contrario.
     */
    public boolean hasTables(){
        if(tables == null) return false;
        return !tables.isEmpty();
    }
    
    /**
     * Evalua si existe una tabla especificada por su nombre.
     * @param name Nombre de la tabla a buscar.
     * @return true si la tabla existe; false en caso contrario.
     */
    public boolean hasTable(String name){
        for (Table table : tables) {
            if (table.getName().equals(name))
                return true;
            table = null;
        }
        return false;
    }
    
    /**
     * Evalua si existe una tabla especificada por la clase de los objetos almacenados.
     * @param clazz Clase de la tabla a buscar.
     * @return true si la tabla existe; false en caso contrario.
     */
    public boolean hasTable(Class<?> clazz){
        for (Table table : tables) {
            if (table.getSerialClass().equals(clazz))
                return true;
            table = null;
        }
        return false;
    }
    
    private void addTable(Table tbl){
        tables.add(tbl);
        rootSerializer.serialize(tbl);
    }
    
    /**
     * Crea una tabla en la base de datos.
     * @param name Nombre de la tabla a crear.
     * @param clazz Clase a la que pertenecerán los objetos de esta tabla(Puede ser 
     * cualquiera). Esta clase debe implementar la interfaz Serializable.
     * @return true si la tabla se creó con éxito; false en caso de que la tabla
     * ya exista.
     * @throws IOException En caso de error al guardar la tabla en la base de datos.
     */
    public boolean createTable(String name, Class<?> clazz) throws IOException{
        if (getTable(name) != null || getTable(clazz) != null) return false;

        addTable(new Table(name, dbFolder, clazz));
        return true;
    }
    
    /**
     * Devuelve el nombre de la base de datos
     * @return Nombre de la base de datos
     */
    public String getName(){
        return this.name;
    }
    
    /**
     * Devuelve el número de tablas almacenadas en la base de datos.
     * @return Cantidad de tablas de la base de datos.
     */
    public int getTablesCount(){
        return tables.size();
    }
    
    /**
     * Devuelve el número de objetos almacenados en la tabla
     * especificada por su nombre.
     * @param tableName Nombre de la tabla a buscar.
     * @return Cantidad de objetos de la tabla en caso de que exista, 
     * en caso contrario arroja una excepcion.
     * @throws org.martin.powerDB.exceptions.TableNotExistsException 
     * En caso de que la tabla no exista.
     */
    public int selectCount(String tableName) throws TableNotExistsException{
        Table t = getTable(tableName);
        if(t == null) {
            throw new TableNotExistsException(tableName);
        }
        return t.getSerializer().getObjectsCount();
    }
    
    /**
     * Elimina una tabla especificada por su nombre.
     * @param tableName Nombre de la tabla a eliminar.
     * @throws TableNotExistsException En caso de que la tabla no exista.
     */
    public void dropTable(String tableName) throws TableNotExistsException{
        if(tableName == null) return;
        
        if(tableName.equals("*")){
            dropAllTables();
            return;
        }
        
        Table table = getTable(tableName);
        if (table == null) 
            throw new TableNotExistsException(tableName);
        
        else{
            table.deleteAll();
            rootSerializer.delete(table);
            tables.remove(table);
        }
    }
    
    /**
     * Elimina todas las tablas de la base de datos.
     */
    public void dropAllTables(){
        rootSerializer.deleteAll();
        while (!tables.isEmpty())            
            tables.poll().deleteAll();
    }
    
    /**
     * Elimina la base de datos completamente
     */
    public void dropDatabase(){
        if(hasTables()) dropAllTables();

        info.deleteFile();
        dbFolder.delete();
        File rootDir = SysInfo.rootDir;
        File infoFile = SysInfo.infoFile;
        
        File[] databases = rootDir.listFiles(f->f.isDirectory());

        // Si la pirmera condicion es válida no se evalua la segunda
        // o sea que se puede mezclar un == null con un .lenght == 0
        // en este orden.
        if (databases == null || databases.length == 0){
            infoFile.delete();
            rootDir.delete();
        }
        
        databases = null;
        rootDir = null;
        infoFile = null;
    }

    /**
     * Devuelve una tabla especificando su nombre.
     * @param tableName Nombre de la tabla solicitada.
     * @return Objeto Table apuntando a la tabla solicitada.
     */
    private Table getTable(String tableName){
        for (Table table : tables) {
            if (table.getName().equals(tableName))
                return table;
            table = null;
        }
        return null;
    }
    
    /**
     * Devuelve una tabla especificando la clase de objetos almacenados en ella.
     * @param clazz Clase de objetos de la tabla solicitada.
     * @return Objeto Table con los datos solicitados.
     */
    private Table getTable(Class<?> clazz){
        for (Table table : tables){
            if (table.getSerialClass().equals(clazz))
                return table;
            table = null;
        }
        return null;
    }
    
    /**
     * Devuelve un objeto especificando la tabla y el índice.
     * @param table Tabla del objeto a buscar.
     * @param index Índice del objeto a buscar.
     * @return Objeto con los datos especificados.
     */
    public Object select(String table, int index){
        return getTable(table).getSerializer().deserializeAt(index);
    }
    
    /**
     * Devuelve un objeto especificando la tabla, el nombre del campo y el valor que 
     * dicho campo debe tener.
     * @param tableName Tabla del objeto a buscar.
     * @param fieldName Nombre del campo solicitado.
     * @param valueToFind Valor del campo a buscar.
     * @return Objeto con los datos solicitados.
     */
    public List<?> select(String tableName, String fieldName, Object valueToFind){
        try {
            return getTable(tableName).getSerializer().deserialize(fieldName, valueToFind);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            //Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            return null;
        }
    }

//    // La idea es que en cada where se diga
//    // campo = algo
//    public LinkedList<?> select(String tableName, String fieldName, String...where){
//        return null;
//    }
    
    /**
     * Devuelve una lista con todos los objetos almacenados en una tabla especificada
     * por su nombre.
     * @param table Tabla a buscar.
     * @return Lista que contiene todos los objetos de la tabla especificada.
     */
    public List<?> selectAll(String table){
        return getTable(table).getSerializer().deserializeAll();
    }
    
    /**
     * Inserta un objeto en una tabla dada por su nombre.
     * @param table Tabla solicitada.
     * @param object Objeto a insertar.
     */
    public void insert(String table, Object object) throws NullObjectException{
        if (object == null)
            throw new NullObjectException("El objeto que desea insertar es nulo");
            
        Table tbl = getTable(table);
        int tblIndex = tables.indexOf(tbl);
        tbl.getSerializer().serialize(object);
        
        rootSerializer.update(tblIndex, tbl);
        tbl = null;
    }
    
    /**
     * Inserta uno o más objetos en una tabla dada por su nombre.
     * @param table Tabla solicitada.
     * @param objects Objetos a insertar.
     */
    public void insert(String table, Object... objects) throws NullObjectException{
        if (objects == null)
            throw new NullObjectException("Los objetos a insertar son nulos");
            
        Table tbl = getTable(table);
        int tblIndex = tables.indexOf(tbl);
        tbl.getSerializer().serializeAll(Arrays.asList(objects));
        rootSerializer.update(tblIndex, tbl);
        tbl = null;
    }
    
    /**
     * Elimina un objeto en una tabla mediante el índice.
     * @param table Tabla a acceder.
     * @param index Índice del objeto a eliminar.
     */
    public void delete(String table, int index){
        Table tbl = getTable(table);
        int tblIndex = tables.indexOf(tbl);
        tbl.getSerializer().deleteAt(index);

        rootSerializer.update(tblIndex, tbl);
        tbl = null;
    }
    
    /**
     * Elimina un objeto en una tabla mediante la referencia al mismo. Si el
     * objeto es un String y es igual a * todos los objetos de la tabla
     * serán eliminados.
     * @param table Tabla a acceder.
     * @param obj Objeto a eliminar.
     */
    public void delete(String table, Object obj){
        Table tbl = getTable(table);
        int tblIndex = tables.indexOf(tbl);
        
        if (obj.equals("*"))
            tbl.getSerializer().deleteObjects();
        else
            tbl.getSerializer().delete(obj);
        
        rootSerializer.update(tblIndex, tbl);
        tbl = null;
    }
    
    /**
     * Actualiza un objeto en una tabla mediante la referencia al mismo.
     * @param table Tabla a acceder.
     * @param older Objeto a actualizar.
     * @param newObj Objeto nuevo que reemplazará al anterior.
     */
    public void update(String table, Object older, Object newObj){
        Table tbl = getTable(table);
        int tblIndex = tables.indexOf(tbl);
        tbl.getSerializer().update(older, newObj);
        
        rootSerializer.update(tblIndex, tbl);
        tbl = null;
    }
    
    /**
     * Actualiza un objeto en una tabla mediante su índice.
     * @param table Tabla a acceder.
     * @param index Índice del objeto a actualizar.
     * @param newObj Nuevo objeto que reemplazará al anterior.
     */
    public void update(String table, int index, Object newObj){
        Table tbl = getTable(table);
        int tblIndex = tables.indexOf(tbl);
        tbl.getSerializer().update(index, newObj);
        
        rootSerializer.update(tblIndex, tbl);
        tbl = null;
    }
    
}
