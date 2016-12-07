/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.mrdoc.bot_twitch2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pircbotx.exception.IrcException;

/**
 *
 * @author Doc
 */
public class TestTWITCHBOT {

    /**
     * @param args the command line arguments
     * @throws org.pircbotx.exception.IrcException
     */
    public static void main(String[] args) throws IrcException {
        try {
            // TODO code application logic here
            MyBot bot = new MyBot();
            
            bot.bot.send().message("#elrichmc", "BOT Connected");
            
            System.out.println("me.mrdoc.bot_twitch2.TestTWITCHBOT.main()");
            
            //bot.sendMessage("#elrichmc", "[BOT] Buenas me estoy ejecutando :D");
            
            //bot.sendMessage("#elrichmc", "[BOT] El tiempo actual es: " + System.currentTimeMillis() + "");
            
            
            
        } catch (IOException | IrcException ex) {
            Logger.getLogger(TestTWITCHBOT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
