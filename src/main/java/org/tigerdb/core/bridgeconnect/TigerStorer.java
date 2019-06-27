package org.tigerdb.core.bridgeconnect;

import org.tigerdb.bridge.StoreManager;
import org.tigerdb.core.model.Table;
import org.tigerdb.lion.store.LionStoreManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TigerStorer {

    public static final String STORER_CLASS_NAME = LionStoreManager.class.getName();

    private static Constructor getStorerBuilder(
            Class<? extends StoreManager> clazz) throws NoSuchMethodException {
        return clazz.getConstructor(Class.class, File.class);
    }

    private static <T extends StoreManager> T getStorerInstance(
            Class<? extends StoreManager> storeClazz, Class clazz, File file) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (T) getStorerBuilder(storeClazz).newInstance(clazz, file);
    }

    public static <T> StoreManager<T> getStoreManager(
            Table<T> tbl, File tblFolder) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return getStoreManager(tbl.getObjectClazz(), tblFolder);
    }

    public static <T> StoreManager<T> getStoreManager(
            Class<T> objectClass, File tblFolder) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName(STORER_CLASS_NAME);
        return getStorerInstance((Class<? extends StoreManager>) aClass, objectClass, tblFolder);
    }


}
