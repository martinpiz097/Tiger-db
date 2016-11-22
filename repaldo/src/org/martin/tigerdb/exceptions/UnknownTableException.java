/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.tigerdb.exceptions;

/**
 *
 * @author martin
 */
public class UnknownTableException extends RuntimeException {

    /**
     * Creates a new instance of <code>UnknownTableException</code> without
     * detail message.
     */
    public UnknownTableException() {
    }

    /**
     * Constructs an instance of <code>UnknownTableException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UnknownTableException(String msg) {
        super(msg);
    }
}
