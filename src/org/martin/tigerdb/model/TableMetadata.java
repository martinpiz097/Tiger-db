/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.tigerdb.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class TableMetadata {
    private final File fileMetadata;
    private final Properties p;
    
    public TableMetadata(File tblFolder) {
        fileMetadata = new File(tblFolder, "metadata.properties");
        p = new Properties();
        if (fileMetadata.exists())
            loadFile();
        
        else
            saveFile();
    }

    public String getTableName(){
        return p.getProperty("tblName");
    }
    
    public void setTableName(String name){
        p.setProperty("tblName", name);
        saveFile();
    }
    
    public Class<?> getTableClass(){
        try {
            return Class.forName(p.getProperty("tblClass"));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TableMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void setTableClass(Class<?> clazz){
        p.setProperty("tblClass", clazz.getName());
        saveFile();
    }
    
    public void deleteFile(){
        fileMetadata.delete();
    }
    
    private void saveFile() {
        try {
            p.store(new FileWriter(fileMetadata), "Metadatos");
        } catch (IOException ex) {
            Logger.getLogger(TableMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void loadFile() {
        try {
            p.load(new FileReader(fileMetadata));
        } catch (IOException ex) {
            Logger.getLogger(TableMetadata.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
