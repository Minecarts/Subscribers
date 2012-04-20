package com.minecarts.subscribers;

import java.util.logging.Level;
import java.text.MessageFormat;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import com.minecarts.dbpermissions.PermissionsCalculated;


public class Subscribers extends JavaPlugin implements Listener {
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        
        for(Player player : getServer().getOnlinePlayers()) {
            updatePlayer(player);
        }
    }
    
    
    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        if(event.getPlayer().hasPermission("subscriber") && event.getAmount() > 0) {
            debug("Subscriber {0} exp changed by {1}, doubling", event.getPlayer(), event.getAmount());
            event.setAmount(event.getAmount() * 2);
        }
    }
    
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayer(event.getPlayer());
    }
    
    @EventHandler
    public void onPermissions(PermissionsCalculated event) {
        if(event.getPermissible() instanceof Player) {
            updatePlayer((Player) event.getPermissible());
        }
    }
    
    private void updatePlayer(Player player) {
        if(!player.isOnline()) {
            debug("Player {0} is not online?", player.getName());
            return;
        }
        
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