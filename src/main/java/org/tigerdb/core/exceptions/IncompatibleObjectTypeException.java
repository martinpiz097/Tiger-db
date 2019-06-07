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
public class IncompatibleObjectTypeException extends RuntimeException {

    /**
     * Creates a new instance of <code>ObjectTypeIncompatibleException</code>
     * without detail message.
     */
    public IncompatibleObjectTypeException() {
    }

    /**
     * Constructs an instance of <code>ObjectTypeIncompatibleException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public IncompatibleObjectTypeException(String msg) {
        super(msg);
    }
}
