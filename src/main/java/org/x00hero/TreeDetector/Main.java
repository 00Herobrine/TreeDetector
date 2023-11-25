package org.x00hero.TreeDetector;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.x00hero.TreeDetector.Events.TreeDetection;
import org.x00hero.TreeDetector.Test.TreeTest;

import java.util.logging.Level;

public final class Main extends JavaPlugin {
    public static Main plugin;
    private final String prefix = "["+ getDescription().getName() + "] ";
    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        Config.Load();
        registerCommands();
        registerEvents();
        ActivityManager.ExpireCheck();
        log(prefix + "Trees will be exterminated. (Enabled)");
    }

    public static void Reload() {
        Bukkit.getScheduler().cancelTask(ActivityManager.schedulerID);
        Config.Load();
        ActivityManager.ExpireCheck();
        log(plugin.prefix + "Trees thought they were safe. (Reload)");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTask(ActivityManager.schedulerID);
        log(prefix + "Trees have been spared. (Disabled)");
    }

    public void registerCommands() { getCommand("treedetector").setExecutor(new CommandController()); }
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new TreeDetection(), this);
        Bukkit.getPluginManager().registerEvents(new TreeTest(), this);
        Bukkit.getPluginManager().registerEvents(new CommandController(), this);
    }
    public static void log(String message) { log(Level.INFO, message); }
    public static void log(Level level, String message) { Bukkit.getLogger().log(level, message); }
    public static void CallEvent(Event event) { Bukkit.getPluginManager().callEvent(event); }
}
