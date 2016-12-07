/**
 * Clase CooldownLib creada en netbeans por Doc
 */

package me.mrdoc.bot_twitch2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Doc
 */
public class CooldownLib {
    
    private Map<UUID,Long> dbCoolDown = null;
    
    private int secondsCooldown = 0;
    
    public CooldownLib() {
        dbCoolDown = new HashMap<UUID,Long>();
    }
    
    public CooldownLib(int segundosCooldown) {
        dbCoolDown = new HashMap<UUID,Long>();
        secondsCooldown = segundosCooldown;
    }

    public Map<UUID, Long> getDbCoolDown() {
        return dbCoolDown;
    }
    
    public int getSecondsCooldown() {
        return secondsCooldown;
    }

    public void setSecondsCooldown(int secondsCooldown) {
        this.secondsCooldown = secondsCooldown;
    }
    
    /**
     * Añade el cooldown a un jugador
     * @param uuid UUID a procesar.
     */
    public void addCooldown(UUID uuid) {
        long time = System.currentTimeMillis();
        
        if(getDbCoolDown().containsKey(uuid)) {
            getDbCoolDown().replace(uuid, time);
        } else {
            getDbCoolDown().put(uuid, time);
        }
    }
    
    /**
     * Remueve el cooldown a un jugador
     * @param uuid UUID a procesar.
     */
    public void remCooldown(UUID uuid) {
        if(getDbCoolDown().containsKey(uuid)) {
            getDbCoolDown().remove(uuid);
        }
    }
    
    /**
     * Busca en base a una UUID y a un valor de segundos si la UUID tiene o no el cooldown
     * @param uuid UUID a procesar.
     * @param seconds Segundos de espera para revisar
     * @return Devuelve si aun tiene el cooldown en base a los segundos dados
     */
    public boolean hasCooldown(UUID uuid, int seconds) {
        if(!getDbCoolDown().containsKey(uuid)) {
            return false;
        }
        
        int cooldown = 1000 * seconds;
        long now = System.currentTimeMillis();
        long time = getDbCoolDown().get(uuid);
        
        long difcooldown = now - time;

        if (difcooldown >= cooldown) {
            getDbCoolDown().remove(uuid);
            return false;
        }
        
        return true;
    }
    
    /**
     * Busca en base a una UUID usando el valor de cooldown seteado en la clase
     * @param uuid UUID a procesar.
     * @return Devuelve si aun tiene el cooldown en base a los segundos dados
     */
    public boolean hasCooldown(UUID uuid) {
        if(!getDbCoolDown().containsKey(uuid)) {
            return false;
        }
        
        int seconds = getSecondsCooldown();
        
        int cooldown = 1000 * seconds;
        long now = System.currentTimeMillis();
        long time = getDbCoolDown().get(uuid);
        
        long difcooldown = now - time;

        if (difcooldown >= cooldown) {
            getDbCoolDown().remove(uuid);
            return false;
        }
        
        return true;
    }
    
    /**
     * Obtiene los segundos necesarios para completar el cooldown
     * @param uuid UUID a procesar.
     * @param seconds Segundos de espera para procesar
     * @return Devuelve un long segun el tiempo restante (En caso de no tener retorna 0)
     */
    public long getTimeReaming(UUID uuid, int seconds) {
        if(!getDbCoolDown().containsKey(uuid)) {
            return 0;
        }
        
        int cooldown = 1000 * seconds;
        long now = System.currentTimeMillis();
        long time = getDbCoolDown().get(uuid);
        
        long difcooldown = now - time;

        if (difcooldown >= cooldown) {
            getDbCoolDown().remove(uuid);
            return 0;
        }
        
        long timereaming = seconds - (difcooldown/1000);
        
        return timereaming;
    }
    
    /**
     * Obtiene los segundos necesarios para completar el cooldown
     * @param uuid UUID a procesar.
     * @return Devuelve un long segun el tiempo restante (En caso de no tener retorna 0)
     */
    public long getTimeReaming(UUID uuid) {
        if(!getDbCoolDown().containsKey(uuid)) {
            return 0;
        }
        
        int seconds = getSecondsCooldown();
        
        int cooldown = 1000 * seconds;
        long now = System.currentTimeMillis();
        long time = getDbCoolDown().get(uuid);
        
        long difcooldown = now - time;

        if (difcooldown >= cooldown) {
            getDbCoolDown().remove(uuid);
            return 0;
        }
        
        long timereaming = seconds - (difcooldown/1000);
        
        return timereaming;
    }
    
    /**
     * Entrega un formato en texto del tiempo a partir de los segundos
     * @param baseSeconds Segundos a transformar
     * @return Devuelve un string con un formato de 00:00:00
     */
    public static String formatTimeOfSec(Long baseSeconds) {
        
        /*
        Calculamos los segundos,minutos,horas en base al total en segundos
        */
        Long sec = (baseSeconds%60);
        Long min = (baseSeconds/60)%60;
        Long hor = ((baseSeconds/60)/60);
        
        /*
        Declaramos fragmentos de string para formatear las horas, minutos, segundos y el resultado
        */
        String f_hor;
        String f_min;
        String f_sec;
        
        String formatofinal;        
        
        /*
        Realizamos el formateo de f_hor
        */
        if(hor == 0) {
            f_hor = "";
        } else {
            f_hor = hor+":";
        }
        
        /*
        Realizamos el formateo de f_min
        */
        if(min == 0) {
            f_min = "00:";
        } else if(min > 9){
            f_min = ""+min+":";
        } else {
            if(hor == 0) {
                f_min = ""+min+":";
            } else {
                f_min = "0"+min+":";
            }            
        }
        
        /*
        Realizamos el formateo de f_sec
        */
        if(sec == 0) {
            f_sec = "00";
        } else if(sec > 9){
            f_sec = ""+sec+"";
        } else {
            f_sec = "0"+sec+"";
        }
        
        /*
        Si el minuto y la hora son 0 entonces solo habran segundos asique reformateamos el f_min
        */
        if(min == 0 && hor == 0) {
            f_min = "";
            f_sec += " segundos";
        }
        
        /*
        Se unen los distintos formatos para el resultado final
        */
        formatofinal = f_hor+f_min+f_sec;
        
        return formatofinal;
    }
}
