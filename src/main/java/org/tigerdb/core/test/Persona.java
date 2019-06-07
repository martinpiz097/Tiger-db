/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tigerdb.core.test;

import java.io.Serializable;

/**
 *
 * @author martin
 */
public class Persona implements Serializable{
    private int edad;
    private String name;

    public Persona(int edad, String name) {
        this.edad = edad;
        this.name = name;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Persona{" + "edad=" + edad + ", name=" + name + '}';
    }
        
}
