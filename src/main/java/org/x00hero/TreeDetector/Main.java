package org.x00hero.TreeDetector;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.x00hero.TreeDetector.Controllers.ActivityController;
import org.x00hero.TreeDetector.Controllers.CommandController;
import org.x00hero.TreeDetector.Trees.TreeDetection;
import org.x00hero.TreeDetector.Test.TreeTest;
import org.x00hero.TreeDetector.Trees.TreeFunctions;

import java.util.logging.Level;

import static org.x00hero.TreeDetector.Controllers.ActivityController.schedulers;

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
        StartSchedulers();
        log(prefix + "Trees will be exterminated. (Enabled)");
    }

    public static void Reload() {
        CancelSchedulers();
        Config.Load();
        StartSchedulers();
        log(plugin.prefix + "Trees thought they were safe. (Reload)");
    }

    @Override
    public void onDisable() {
        CancelSchedulers();
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
    public static Vector divideVector(Vector vector, double divisor) {
        double quotientX = vector.getX() / divisor;
        double quotientY = vector.getY() / divisor;
        double quotientZ = vector.getZ() / divisor;
        return new Vector(quotientX, quotientY, quotientZ);
    }
    public static void StartSchedulers() {
        ActivityController.ExpireCheck();
        ActivityController.ParticleCheck();
        TreeFunctions.DDayCheck();
    }
    public static void CancelSchedulers() { for(int id : schedulers) Bukkit.getScheduler().cancelTask(id); }
}
