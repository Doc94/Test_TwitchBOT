/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.mrdoc.bot_twitch2;

/**
 *
 * @author Doc
 */
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.exception.IrcException;

public class MyBot {
    
    final String CHANNEL = "#channel";
    
    final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    
    PircBotX bot;
    
    public MyBot() throws IOException, IrcException {
        Configuration.Builder configuration = new Configuration.Builder()
                
                                        .setEncoding(Charset.forName("UTF-8")) //Support UTF-8
                
                                        .setAutoNickChange(false) //Twitch doesn't support multiple users
                                        .setOnJoinWhoEnabled(false) //Twitch doesn't support WHO command
                
                                        .setCapEnabled(true)
                                        .addCapHandler(new EnableCapHandler("twitch.tv/membership")) //Twitch by default doesn't send JOIN, PART, and NAMES unless you request it, see https://github.com/justintv/Twitch-API/blob/master/IRC.md#membership
                                        .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                                        .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                
                
                                        .addServer("irc.twitch.tv")
                                        
                                        .setName("thedoc94") //Your twitch.tv username
                                        .setServerPassword("mypass") //Your oauth password from http://twitchapps.com/tmi
                
                                        .addAutoJoinChannel(CHANNEL)
                
                                        .addListener(new MyListener())
                                        
                                        
                                        ;
        bot = new PircBotX(configuration.buildConfiguration());
        
        exec.schedule(() -> {
            bot.send().message(CHANNEL, "BOT By Doc Connected");
            System.out.println("me.mrdoc.bot_twitch2.MyBot.<init>()");
        }, 5, TimeUnit.SECONDS);
        
        bot.startBot();
        
        
        
    }
}
