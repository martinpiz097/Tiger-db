/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tigerdb.core.model;

import org.mpizutil.electrolist.structure.ElectroList;
import org.tigerdb.bridge.StoreManager;
import org.tigerdb.core.bridgeconnect.TigerStorer;
import org.tigerdb.lion.store.LionStoreManager;
import org.tigerdb.lion.system.SysInfo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author martin
 */
public class Table<T>{
    private static final char SEPARATOR = '/';
    private final String relatedDb;
    private final File tblFolder;
    private String name;
    private final Class<T> objectClazz;
    private final TableMetadata metadata;
    private StoreManager<T> storeManager;
    
    // Arreglar problema de que cuando intento crear bases de datos en 
    // rutas externas necesito otro constructor para que el metadata
    // no me arroje FileNotFoundException.
    public Table(String name, Class<T> objectClazz, String relatedDB) 
            throws IOException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append(SysInfo.ROOT_DIR.getCanonicalPath());
        sb.append(SEPARATOR);
        sb.append(relatedDB);
        sb.append(SEPARATOR);
        sb.append(name);
        sb.append(SEPARATOR);
        
        tblFolder = new File(sb.toString());
        sb = null;
        
        if(!tblFolder.exists())
            tblFolder.mkdir();
        this.name = name;
        this.objectClazz = objectClazz;

        metadata = new TableMetadata(tblFolder);
        metadata.setTableName(name);
        metadata.setTableClass(objectClazz);
        
        this.relatedDb = relatedDB;
        try {
            storeManager = TigerStorer.getStoreManager(this, tblFolder);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public Table(String relatedDb, File tblFolder) 
            throws IOException, ClassNotFoundException {
        this.relatedDb = relatedDb;
        this.tblFolder = tblFolder;
        metadata = new TableMetadata(tblFolder);
        name = metadata.getTableName();
        objectClazz = (Class<T>) metadata.getTableClass();
        try {
            storeManager = TigerStorer.getStoreManager(objectClazz, tblFolder);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Table(String tblName, Class<T> objectClazz, File dbFolder) 
            throws IOException, ClassNotFoundException {
        tblFolder = new File(dbFolder, tblName);
        
        if(!tblFolder.exists())
            tblFolder.mkdir();
        this.name = tblName;
        this.objectClazz = objectClazz;

        metadata = new TableMetadata(tblFolder);
        metadata.setTableName(name);
        metadata.setTableClass(objectClazz);
        
        this.relatedDb = dbFolder.getName();
        storeManager = new LionStoreManager<>(objectClazz, tblFolder);
    }
    
    public boolean isInstance(T obj){
        return objectClazz.isInstance(obj);
    }
    
    public boolean isEmpty(){
        return selectCount() == 0;
    }

    public StoreManager<T> getStoreManager() {
        return storeManager;
    }

    public Class<T> getObjectClazz() {
        return objectClazz;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        metadata.setTableName(name);
        tblFolder.renameTo(new File(tblFolder.getParentFile(), name));
    }
    
    public void insert(T object) throws IOException{
        storeManager.addObject(object);
    }

    public void insertFrom(Collection<T> collection) throws IOException{
        storeManager.addObjectsFrom(collection);
    }

    public long selectCount(){
        return storeManager.getObjectsCount();
    }
    
    public long selectSumBy(String fieldName){
        return storeManager.getSumBy(fieldName);
    }
    
    public long selectMaxBy(String fieldName){
        return storeManager.getMaxBy(fieldName);
    }
    public long selectMinBy(String fieldName){
        return storeManager.getMinBy(fieldName);
    }

    public double selectAvgBy(String fieldName){
        return storeManager.getAvgBy(fieldName);
    }
    
    public ElectroList<T> selectAll(){
        List<T> objects = storeManager.getObjects();
        if (objects instanceof ElectroList)
            return (ElectroList<T>) objects;
        else
            return new ElectroList<>(objects);
    }

    public T selectFirst(){
        return storeManager.getFirstObject();
    }
    
    public T selectLast(){
        return storeManager.getLastObject();
    }
    
    public T selectBy(int index){
        return storeManager.getObjectBy(index);
    }
    
    public T selectBy(String fieldName, Object valueToFind) throws Exception {
        return storeManager.getObjectBy(fieldName, valueToFind);
    }
    
    public T selectFirstBy(String fieldName, Object valueToFind){
        return storeManager.getFirstObjectBy(fieldName, valueToFind);
    }

    public ElectroList<T> getObjectsBy(String fieldName, Object valueToFind)
            throws Exception {
        List<T> objectsBy = storeManager.getObjectsBy(fieldName, valueToFind);
        if (objectsBy instanceof ElectroList)
            return (ElectroList<T>) objectsBy;
        else
            return new ElectroList<>(objectsBy);
    }
    
    public void update(int index, T newObject) throws IOException{
        storeManager.setObject(index, newObject);
    }

    public void update(String fieldName, Object valueToFind, T newObject)
            throws Exception {
        storeManager.setObjects(fieldName, valueToFind, newObject);
    }
    
    public void update(T oldObj, T newObj) throws IOException{
        storeManager.setObject(oldObj, newObj);
    }

    public void deleteAll() throws IOException{
        storeManager.deleteAllObjects();
    }
    
    public void deleteAt(int index) throws IOException{
        storeManager.deleteObject(index);
    }
    
    public void deleteBy(String fieldName, Object valueToFind)
            throws Exception {
        storeManager.deleteObjectsBy(fieldName, valueToFind);
    }
    
    public void drop(){
        storeManager.deleteRecordFile();
        metadata.deleteFile();
        tblFolder.delete();
    }

    public void show(){
        System.out.println("Tabla: "+name);
        System.out.println("Clase: "+objectClazz.getName());
        System.out.println("Cantidad de registros: "+selectCount());
        System.out.println("-------------------------");
    }
    
}
