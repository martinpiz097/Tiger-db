/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.powerDB.db;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import org.martin.powerDB.db.security.User;
import org.martin.powerDB.exceptions.DatabaseNotExistsException;
import org.martin.powerDB.exceptions.InvalidAccessException;
import org.martin.powerDB.exceptions.NotConnectedException;
import org.martin.powerDB.system.SysInfo;

/**
 *
 * @author martin
 */
public class PowerDB {
    private final LinkedBlockingQueue<Database> databases;
    private final User defaultUser;
    private boolean isConnected;
    
    public PowerDB() {
        databases = new LinkedBlockingQueue<>();
        defaultUser = new User("root", "");
        isConnected = false;
    }

    public boolean isConnected(){
        return isConnected;
    }
    
    public void connect(String user, String password) throws InvalidAccessException {
        if (!defaultUser.isEqualsCredentials(user, password))
            throw new InvalidAccessException();

        isConnected = true;
    }
    
    public Database createDatabase(String name) throws IOException, NotConnectedException{
        if(!isConnected())
            throw new NotConnectedException();
        
        if (databases.stream().anyMatch(db->db.getName().equals(name)))
            return null;
        
        Database newDB = new Database(name);
        databases.add(newDB);
        return newDB;
    }

    public int getDatabasesCount(){
        return SysInfo.rootDir.listFiles(f->f.isDirectory()).length;
    }
    
    public Database getDatabase(String name){
        return databases.stream().filter(db->db.getName().equals(name)).findFirst().orElse(null);
    }
    
    public void dropDatabase(String name) throws DatabaseNotExistsException{
        if(getDatabase(name) == null) throw new DatabaseNotExistsException(name);
        
        databases.removeIf(db->db.getName().equals(name));
    }
    
}
