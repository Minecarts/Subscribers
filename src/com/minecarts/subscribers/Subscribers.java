package com.minecarts.subscribers;

import java.util.logging.Level;
import java.text.MessageFormat;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


public class Subscribers extends JavaPlugin implements Listener {
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        
        log("Version {0} enabled.", getDescription().getVersion());
    }
    
    
    @EventHandler
    public void on(com.minecarts.dbpermissions.PermissionsCalculated event) {
        debug("Caught event {0}", event);
        if(event.getPermissible() instanceof Player) {
            debug("Permissible is a player: ", event.getPermissible());
            updatePlayer((Player) event.getPermissible());
        }
    }
    
    
    private void updatePlayer(Player player) {
        String name = player.hasPermission("subscriber")
            ? org.bukkit.ChatColor.YELLOW + player.getName()
            : player.getName();
        
        debug("Updating player display name {0} and player list name {1} to {2}", player.getDisplayName(), player.getPlayerListName(), name);
        
        player.setDisplayName(name);
        player.setPlayerListName(name.length() > 16
                ? name.substring(0, 14) + ".."
                : name);
    }
    
    
    public void log(String message) {
        log(Level.INFO, message);
    }
    public void log(Level level, String message) {
        getLogger().log(level, message);
    }
    public void log(String message, Object... args) {
        log(MessageFormat.format(message, args));
    }
    public void log(Level level, String message, Object... args) {
        log(level, MessageFormat.format(message, args));
    }
    
    public void debug(String message) {
        log(Level.FINE, message);
    }
    public void debug(String message, Object... args) {
        debug(MessageFormat.format(message, args));
    }
}