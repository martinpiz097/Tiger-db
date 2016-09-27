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
public class InvalidAccessException extends Exception {

    /**
     * Creates a new instance of <code>InvalidAccessException</code> without
     * detail message.
     */
    public InvalidAccessException() {
        this("Credenciales inválidas");
    }

    /**
     * Constructs an instance of <code>InvalidAccessException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidAccessException(String msg) {
        super(msg);
    }
}
