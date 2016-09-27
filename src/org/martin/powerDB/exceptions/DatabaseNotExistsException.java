/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.powerDB.exceptions;

/**
 *
 * @author martin
 */
public class DatabaseNotExistsException extends Exception {

    public DatabaseNotExistsException() {
        super("La base de datos no existe");
    }

    public DatabaseNotExistsException(String database) {
        super("La base de datos "+database+" no existe");
    }

}
