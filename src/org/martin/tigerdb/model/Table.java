/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.tigerdb.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.martin.electroList.structure.ElectroList;
import org.martin.lion.exceptions.UnknownFieldException;
import org.martin.lion.store.StoreManager;
import org.martin.lion.system.SysInfo;

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
    private final StoreManager<T> storeManager;
    
    public Table(String name, Class<T> objectClazz, String relatedDB) 
            throws IOException, ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append(SysInfo.ROOT_DIR.getCanonicalPath());
        sb.append(SEPARATOR);
        sb.append(relatedDB);
        sb.append(SEPARATOR);
        sb.append(name);
        sb.append(SEPARATOR);
        
        tblFolder = new File(SysInfo.ROOT_DIR.getCanonicalPath()+SEPARATOR+relatedDB+SEPARATOR+
                name+SEPARATOR);
        sb = null;
        
        if(!tblFolder.exists())
            tblFolder.mkdir();
        this.name = name;
        this.objectClazz = objectClazz;
        
        metadata = new TableMetadata(tblFolder);
        metadata.setTableName(name);
        metadata.setTableClass(objectClazz);
        
        this.relatedDb = relatedDB;
        storeManager = new StoreManager<>(objectClazz, tblFolder);
    }

    public Table(String relatedDb, File tblFolder) 
            throws IOException, ClassNotFoundException {
        this.relatedDb = relatedDb;
        this.tblFolder = tblFolder;
        metadata = new TableMetadata(tblFolder);
        name = metadata.getTableName();
        objectClazz = (Class<T>) metadata.getTableClass();
        storeManager = new StoreManager<>(objectClazz, tblFolder);
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

    public int selectCount(){
        return storeManager.getObjectsCount();
    }
    
    public ElectroList<T> selectAll(){
        return storeManager.getObjects();
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
    
    public T selectBy(String fieldName, Object valueToFind) throws IllegalArgumentException, IllegalAccessException, UnknownFieldException{
        return storeManager.getObjectBy(fieldName, valueToFind);
    }
    
    public ElectroList<T> getObjectsBy(String fieldName, Object valueToFind) 
            throws IllegalArgumentException, IllegalAccessException {
        return storeManager.getObjectsBy(fieldName, valueToFind);
    }
    
    public void update(int index, T newObject) throws IOException{
        storeManager.setObject(index, newObject);
    }

    public void update(String fieldName, Object valueToFind, T newObject) 
            throws UnknownFieldException, IllegalArgumentException, 
            IllegalAccessException, IOException{
        storeManager.setObjects(fieldName, valueToFind, newObject);
    }

    public void deleteAll() throws IOException{
        storeManager.deleteAllObjects();
    }
    
    public void deleteAt(int index) throws IOException{
        storeManager.deleteObject(index);
    }
    
    public void deleteBy(String fieldName, Object valueToFind) 
            throws UnknownFieldException, IllegalArgumentException, 
            IllegalAccessException, IOException{
        storeManager.deleteObjectsBy(fieldName, valueToFind);
    }
    
    public void drop(){
        storeManager.deleteFile();
        metadata.deleteFile();
        tblFolder.delete();
    }
    
}
