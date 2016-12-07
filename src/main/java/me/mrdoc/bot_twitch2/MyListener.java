/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.mrdoc.bot_twitch2;

import java.io.Console;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import org.pircbotx.hooks.events.UnknownEvent;

/**
 *
 * @author Doc
 */
public class MyListener extends ListenerAdapter {
    
    final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    final CooldownLib times = new CooldownLib(30);
    final ArrayList<String> repet = new ArrayList<>();
    
    int subs = 429;
    
    public MyListener() {
    }
    
    @Override
    public void onJoin(JoinEvent event) {
        System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewJoin");
        System.out.println(ColorsConsole.ANSI_WHITE + "[JOIN] [ID: " + event.getId() + "] [NICK: " + event.getUser().getNick() + "]" + "");
        
    }
    
    @Override
    public void onQuit(QuitEvent event) {
        System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewQuit");
        System.out.println(ColorsConsole.ANSI_WHITE + "[QUIT] [ID: " + event.getId() + "] [NICK: " + event.getUser().getNick() + "]" + "");
    }
    
    @Override
    public void onUnknown(UnknownEvent event) {
        System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewUnk");
        System.out.println(ColorsConsole.ANSI_RED + "[UNK] [CODE: " + event.getId() + "] [LINE: " + event.getLine() + "]" + "");
    }
    
    @Override
    public void onServerResponse(ServerResponseEvent event) {
        System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewResponse");
        System.out.println(ColorsConsole.ANSI_YELLOW + "[RESPONSE] [CODE: " + event.getCode() + "] [RAW: " + event.getRawLine() + "]" + "");
    }

    @Override
    public void onNotice(NoticeEvent event) {
        System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewNotice");
        if(event.getUser() instanceof User) {
            System.out.println(ColorsConsole.ANSI_RED + "[NOTICE] [EVENT: " + event.getNotice() + "] [NICK: " + event.getUser().getNick() + "]" + "[MESSAGE: " + event.getMessage() + "]");
        } else {
            System.out.println(ColorsConsole.ANSI_RED + "[NOTICE] [EVENT: " + event.getNotice() + "] [NICK: " + "UNK" + "]" + "[MESSAGE: " + event.getMessage() + "]");
        }
        
    }
    
    @Override
    public void onMessage(MessageEvent event) {
        //System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewMessage");
        Map userData = event.getV3Tags();
        
        //System.out.println("testtwitchbot.MyListener.onMessage() TAGS: " + userData.keySet().size());
        
        userData.keySet().forEach((key) -> {
            //System.out.println(ColorsConsole.ANSI_YELLOW + "TAG: " + key + " - V: " + userData.get(key).toString());
        });
        
        //System.out.println(ColorsConsole.ANSI_CYAN + "[CHAT] [NICK: " + event.getUser().getNick() + "]" + "[MESSAGE: " + event.getMessage() + "]");
        onRespMessage(event); //Sin esto el evento de abajo no se ejecuta
    }
    
    
    public void onRespMessage(MessageEvent event) {
        final User user = event.getUser();
        if(user instanceof User) {
            String nick = user.getNick();
            String twmsg = event.getMessage();
            if(nick.equalsIgnoreCase("twitchnotify")) { //Si el usuario es twitchnotify (se supone solo comenta los sub,resub,subprime)
                System.out.println("me.mrdoc.bot_twitch2.MyListener.onRespMessage() " + ColorsConsole.ANSI_RED + "SUBBBBBBBBBBBBBBBBBB");
                
                System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewMessage");
                System.out.println(ColorsConsole.ANSI_CYAN + "[CHAT] [NICK: " + event.getUser().getNick() + "]" + "[MESSAGE: " + event.getMessage() + "]");
                
                //Envio mensaje de """spam""" de sub.
                exec.scheduleAtFixedRate(new Runnable() {
                    private int c = 0;
                    
                    @Override
                    public void run() {
                        if(c >= 3) { //Si "c" llega o supera a 3
                            System.out.println("me.mrdoc.bot_twitch2.MyListener.onRespMessage() " + ColorsConsole.ANSI_RED + "FIN - SUBBBBBBBBBBBBBBBBBB");
                            throw new RuntimeException();
                        }
                        
                        if(c >= 5) {
                            System.out.println("FALLA");
                            System.exit(1);
                        }
                        
                        event.respondChannel("elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB elrichSUB");
                    
                        c += 1;
                    }
                }, 0, 3, TimeUnit.SECONDS); //En teoria realizara la accion por cada 2 segundos
                
                if(!twmsg.contains("row")) {
                    subs +=1;
                    exec.schedule(() -> {
                        //bot.send().message("#elrichmc", "BOT Connected");
                        event.respondChannel("BOT: Desde que me conectaron se unieron " + subs + " subs :3");
                    }, 5, TimeUnit.SECONDS);
                }
                
            }
            
            if(twmsg.trim().equalsIgnoreCase("time")) { //Detecta texto
                String time = new java.util.Date().toString();
                event.respondChannel("@" + nick + " elrichSUB La fecha es " + time);
            }
            
            if(twmsg.trim().equalsIgnoreCase("!userson")) {
                System.out.println(ColorsConsole.ANSI_YELLOW + "[!userson] El usuario " + nick + " ha solicitado un conteo del canal.");
                UUID uuid = UUID.fromString("54112417-2fb6-49e1-975f-af6cf00911e7");
                int sise = event.getChannel().getUsersNicks().size();
                if(!times.hasCooldown(uuid)) {
                    times.addCooldown(uuid);
                    event.respondChannel("@" + nick + " elrichTIME Hay " + sise + " personas online en este canal.");
                } else {
                    System.out.println(ColorsConsole.ANSI_YELLOW + "[!userson] Solicitud denegada por cooldown");
                }
                
            }
            
            if(twmsg.trim().equalsIgnoreCase("spam") && nick.equalsIgnoreCase("thedoc94")) {
                exec.scheduleAtFixedRate(new Runnable() {
                    private int c = 0;
                    
                    @Override
                    public void run() {
                        if(c >= 25) { //Si "c" llega o supera a 3
                            System.out.println("me.mrdoc.bot_twitch2.MyListener.onRespMessage() " + ColorsConsole.ANSI_RED + "FIN - SUBBBBBBBBBBBBBBBBBB");
                            throw new RuntimeException();
                        }
                        
                        if(c >= 30) {
                            System.out.println("FALLA");
                            System.exit(1);
                        }
                        
                        event.respondChannel("elrichTIME GUF elrichTIME");
                    
                        c += 1;
                    }
                }, 0, 10, TimeUnit.SECONDS); //En teoria realizara la accion por cada 2 segundos
            }
            
            if(twmsg.trim().equalsIgnoreCase("test") && nick.equalsIgnoreCase("thedoc94")) {
                exec.scheduleAtFixedRate(new Runnable() {
                    private int c = 0;
                    
                    @Override
                    public void run() {
                        if(c >= 3) { //Si "c" llega o supera a 3
                            System.out.println("me.mrdoc.bot_twitch2.MyListener.onRespMessage() " + ColorsConsole.ANSI_RED + "FIN - TIME");
                            throw new RuntimeException();
                        }
                        
                        
                        event.respondChannel("elrichTIME");
                        System.out.println(".run() + REPEAT");
                    
                        c += 1;
                    }
                }, 0, 3, TimeUnit.SECONDS); //En teoria realizara la accion por cada 2 segundos
            }
            
            //
            if(repet.isEmpty()) {
                repet.add(twmsg);
            } else {
                if(repet.get(repet.size()).equalsIgnoreCase(twmsg)) {
                    
                }
                //repet.add(twmsg)
            }
            //
        }
        
    }
    
    @Override
    public void onPrivateMessage(PrivateMessageEvent event) {
        System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewPVMessage");
        
        System.out.println(ColorsConsole.ANSI_BLUE + "[PCHAT] [NICK: " + event.getUser().getNick() + "]" + "[MESSAGE: " + event.getMessage() + "]");
    }
    
    @Override
    public void onAction(ActionEvent event) {
        System.out.println(ColorsConsole.ANSI_WHITE + "==========================================\nnewAction");
        System.out.println(ColorsConsole.ANSI_PURPLE + "[ACTION] [ACTION: " + event.getAction() + "] [NICK: " + event.getUser().getNick() + "]" + "[MESSAGE: " + event.getMessage() + "]");
    }
    
}
