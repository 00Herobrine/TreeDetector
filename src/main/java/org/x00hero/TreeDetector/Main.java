package org.x00hero.TreeDetector;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.x00hero.TreeDetector.Events.TreeDetection;
import org.x00hero.TreeDetector.Test.TreeTest;

import java.util.logging.Level;

public final class Main extends JavaPlugin {
    private final String prefix = "["+ getDescription().getName() + "] ";
    public static Main plugin;
    @Override
    public void onEnable() {
        plugin = this;
        // Plugin startup logic
        registerEvents();
        ActivityManager.ExpireCheck();
        log(prefix + "Trees will be exterminated. (Enabled)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        log(prefix + "Trees have been spared. (Disabled)");
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new TreeDetection(), this);
        Bukkit.getPluginManager().registerEvents(new TreeTest(), this);
    }
    public static void log(String message) { log(Level.INFO, message); }
    public static void log(Level level, String message) {
        Bukkit.getLogger().log(level, message);
    }
    public static void CallEvent(Event event) { Bukkit.getPluginManager().callEvent(event); }
}
