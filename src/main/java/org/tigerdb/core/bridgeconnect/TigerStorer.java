package org.tigerdb.core.bridgeconnect;

import org.tigerdb.bridge.StoreManager;
import org.tigerdb.core.model.Table;
import org.tigerdb.lion.store.LionStoreManager;

import java.io.File;
import java.io.IOException;

public class TigerStorer {
    public static <T> StoreManager<T> getStoreManager(
            Table<T> tbl, File tblFolder) throws IOException, ClassNotFoundException {
        return getStoreManager(tbl.getObjectClazz(), tblFolder);
    }

    public static <T> StoreManager<T> getStoreManager(
            Class<T> objectClass, File tblFolder) throws IOException, ClassNotFoundException {
        return new LionStoreManager<T>(objectClass, tblFolder);
    }
}
