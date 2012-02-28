package com.minecarts.subscribers;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.MessageFormat;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


public class Subscribers extends JavaPlugin implements Listener {
    private static final Logger logger = Logger.getLogger("com.minecarts.subscribers");
    
    
    @Override
    public void onEnable() {
        reloadConfig();
        
        // internal plugin commands
        getCommand("subscribers").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                if(!sender.hasPermission("subscribers.admin.reload")) return true; // "hide" command output for nonpermissibles
                
                if(args[0].equalsIgnoreCase("reload")) {
                    Subscribers.this.reloadConfig();
                    sender.sendMessage("Subscribers config reloaded.");
                    return true;
                }
                
                return false;
            }
        });
        
        log("Version {0} enabled.", getDescription().getVersion());
    }
    
    
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        final FileConfiguration config = getConfig();
        
        for(Player player : getServer().getOnlinePlayers()) {
            updatePlayer(player);
        }
    }
    
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void on(org.bukkit.event.player.PlayerLoginEvent event) {
        updatePlayer(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void on(org.bukkit.event.player.PlayerJoinEvent event) {
        updatePlayer(event.getPlayer());
    }
    
    
    private void updatePlayer(Player player) {
        String name = player.hasPermission("subscriber")
            ? org.bukkit.ChatColor.YELLOW + player.getName()
            : player.getName();
        
        player.setDisplayName(name);
        player.setPlayerListName(name.length() > 16
                ? name.substring(0, 14) + ".."
                : name);
    }
    
    
    public void log(String message) {
        log(Level.INFO, message);
    }
    public void log(Level level, String message) {
        logger.log(level, MessageFormat.format("{0}> {1}", getDescription().getName(), message));
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