/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.powerDB.util;


/**
 *
 * @author martin
 */
public class Encryptor {
    
    public static String encrypt(String password){
        String passEncrypted = "";
        for (char caracter : password.toCharArray()) 
            passEncrypted += ((char)(((int)caracter + 2) * 3));
        
        return passEncrypted;
    }
    
    public static String decrypt(String password){
        String passDecrypted = "";
        for (char caracter : password.toCharArray()) 
            passDecrypted += ((char)(((int)caracter/3) - 2));
        
        return passDecrypted;
    }
}