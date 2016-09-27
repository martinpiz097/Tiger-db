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
public class NullObjectException extends Exception {

    /**
     * Creates a new instance of <code>NullObjectException</code> without detail
     * message.
     */
    public NullObjectException() {
    }

    /**
     * Constructs an instance of <code>NullObjectException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NullObjectException(String msg) {
        super(msg);
    }
}
