/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.powerDB.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.powerDB.system.SysInfo;
import static org.martin.powerDB.util.Encryptor.encrypt;
import static org.martin.powerDB.util.Encryptor.decrypt;

/**
 *
 * @author martin
 */
public final class Info {
    private final File logFile;
    private final Properties p;
    private FileOutputStream os;
    private FileInputStream is;
    
    public Info() throws IOException {
        logFile = SysInfo.infoFile;
        p = new Properties();
        
        if (!logFile.exists()) saveConfigs();
        
        else loadConfigs();
    }

    public Info(File dbFolder) throws IOException{
        logFile = new File(dbFolder, dbFolder.getName()+"Info.xml");
        p = new Properties();
        
        if (!logFile.exists()) saveConfigs();
        
        else loadConfigs();
    }
    
    public void addProperty(String property, String value) throws IOException{
        //p.put(property, value);
        p.setProperty(property, value);
        saveConfigs();
    }
    
    public boolean hasName(){
        return p.getProperty("name") != null;
    }

    public boolean hasSaveConfigs(){
        return p.getProperty("host") != null || p.getProperty("user") != null ||
                p.getProperty("password") != null;
    }
    
    public boolean equals(String server, String user, String password){
        return server.equalsIgnoreCase(getHost()) && user.equalsIgnoreCase(getUser()) && 
                password.equalsIgnoreCase(getPassword());
    }
    
    public void deleteSettings() throws IOException{
        p.clear();
        saveConfigs();
    }

    public void deleteFile(){
        logFile.delete();
    }
    
    public void setSettings(String server, String user, String password) throws IOException{
        setHost(server);
        setUser(user);
        setPassword(password);
    }
    
    public String getDbName(){
        return p.getProperty("dbName");
    }
    
    public String getHost(){
        return p.getProperty("host");
    }
    
    public void setHost(String newHost) throws IOException{
        p.setProperty("host", newHost);
        saveConfigs();
    }
    
    public String getUser(){
        return p.getProperty("user");
    }
    
    public void setUser(String newUser) throws IOException{
        p.setProperty("user", newUser);
        saveConfigs();
    }
    
    public String getPassword(){
        return decrypt(p.getProperty("password"));
    }
    
    public void setPassword(String newPassword) throws IOException{
        p.setProperty("password", encrypt(newPassword));
        saveConfigs();
    }
    
    public void loadConfigs() throws IOException{
        is = new FileInputStream(logFile);
        p.loadFromXML(is);
    }
    
    public void saveConfigs() throws IOException{
        os = new FileOutputStream(logFile);
        p.storeToXML(os, "Archivo de configuración PowerDB");
    }
    
}