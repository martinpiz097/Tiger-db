/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.powerDB.db.security;

import java.io.Serializable;
import org.martin.powerDB.util.Encryptor;

/**
 *
 * @author martin
 */
public class User implements Serializable{
    private final String nick;
    private final String password;

    public User(String nick, String password) {
        this.nick = nick;
        this.password = Encryptor.encrypt(password);
    }
    
    public boolean isEqualsCredentials(String nick, String password){
        return this.nick.equals(nick) && this.password.equals(Encryptor.encrypt(password));
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return Encryptor.decrypt(password);
    }
    
}
