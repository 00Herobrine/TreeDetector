package org.x00hero.treedetector;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.x00hero.treedetector.Events.Minecraft.BlockBreak;

import java.util.logging.Level;

public final class TreeDetector extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerEvents();
        log("["+ getDescription().getName() + "] Trees will be exterminated.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents() { Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);}
    public static void log(String message) { log(Level.INFO, message); }
    public static void log(Level level, String message) {
        Bukkit.getLogger().log(level, message);
    }
    public static void CallEvent(Event event) { Bukkit.getPluginManager().callEvent(event); }
}
