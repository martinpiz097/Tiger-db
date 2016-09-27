/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.powerDB.system;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class SysInfo {
    public static final File rootDir = new File("powerDB");
    public static final File infoFile = new File(rootDir, "powerDbInfo.xml");
    static{
        if (!rootDir.exists()) rootDir.mkdir();
        if (!infoFile.exists()) try {
            infoFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(SysInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
