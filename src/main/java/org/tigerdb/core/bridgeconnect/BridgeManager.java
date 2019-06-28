package org.tigerdb.core.bridgeconnect;

import org.tigerdb.lion.store.LionStoreManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.tigerdb.core.system.SysInfo.CONFIG_FILE_NAME;

// probare primero con el nombre de la clase en codigo despues
// estandarizare a un archivo properties
public class BridgeManager {

    public static final String STORE_NAME_KEY = "store.classname";

    private final File fileStorePropers;
    private final Properties props;

    public BridgeManager() {
        fileStorePropers = new File(".", CONFIG_FILE_NAME);
        props = new Properties();

        if (fileStorePropers.exists()) {
            loadData();
        }
        else {
            try {
                fileStorePropers.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveDefaultData();
            saveData();
        }

    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public void setProperty(String key, String value) {
        props.setProperty(key, value);
        saveData();
    }

    private void saveData() {
        try {
            props.store(new FileOutputStream(fileStorePropers), "tigerdb config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDefaultData() {
        props.setProperty(STORE_NAME_KEY, LionStoreManager.class.getName());
        saveData();
    }

    private void loadData() {
        try {
            props.load(new FileInputStream(fileStorePropers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
