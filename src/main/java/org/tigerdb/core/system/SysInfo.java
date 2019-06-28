/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tigerdb.core.system;

import org.tigerdb.core.bridgeconnect.BridgeManager;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author martin
 */
public class SysInfo {
    public static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    public static final String USER_NAME = System.getProperty("user.name");
    public static final String CONFIG_FILE_NAME = "tigerdb.properties";
    private static final boolean IS_WINDOWS = OS_NAME.contains("windows");
    public static final File ROOT_DIR;

    public static final String STORER_CLASS_NAME;

    
    static {
        String rootPath = IS_WINDOWS ? "C:/Users/"+USER_NAME+"/tigerdb" : "/home/"+USER_NAME
                +"/"+"tigerdb";
        ROOT_DIR = new File(rootPath);
        if (!ROOT_DIR.exists()) ROOT_DIR.mkdir();

        BridgeManager manager = new BridgeManager();
        STORER_CLASS_NAME = manager.getProperty(BridgeManager.STORE_NAME_KEY);
    }
    
}
