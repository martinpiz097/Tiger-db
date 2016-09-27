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
public class NotConnectedException extends Exception {

    /**
     * Creates a new instance of <code>NotConnectedException</code> without
     * detail message.
     */
    public NotConnectedException() {
        this("No estás conectado");
    }

    /**
     * Constructs an instance of <code>NotConnectedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotConnectedException(String msg) {
        super(msg);
    }
}
