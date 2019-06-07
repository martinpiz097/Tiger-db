/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tigerdb.core.exceptions;

/**
 *
 * @author martin
 */
public class UnknownDatabaseException extends RuntimeException{

    public UnknownDatabaseException() {
    }

    public UnknownDatabaseException(String message) {
        super(message);
    }
    
}
