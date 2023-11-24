package org.x00hero.TreeDetector;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;

import static org.x00hero.TreeDetector.Main.plugin;

public class Config {
    //region Detection
    public static int logsThreshold; // required amount of logs attached
    public static int leavesThreshold; // required amount of leaves attached to be considered a Tree
    public static int maxSearchDistance; // distance from initial block
    public static int maxSearchableBlocks; // max blocks to iterate through
    //endregion

    //region Zone
    public static int zoneSize;
    public static boolean invisible;
    public static EntityType entityType;
    public static String colliderName;
    //region Particle
    public static int particleAmount;
    public static long updateRate;
    public static double offsetX;
    public static double offsetY;
    public static double offsetZ;
    public static double speed;
    public static float particleSize;
    public static Particle particle;
    public static Particle.DustOptions dustOptions;
    //endregion
    //endregion

    public static void Load() {
        plugin.reloadConfig();
        logsThreshold = plugin.getConfig().getInt("Detection.logThreshold");
        leavesThreshold = plugin.getConfig().getInt("Detection.leavesThreshold");
        maxSearchDistance = plugin.getConfig().getInt("Detection.maxSearchDistance");
        maxSearchableBlocks = plugin.getConfig().getInt("Detection.maxSearchableBlocks");
        particleAmount = plugin.getConfig().getInt("Zone.Particles.amount");
        updateRate = plugin.getConfig().getLong("Zone.Particles.updateRate");
        zoneSize = plugin.getConfig().getInt("Zone.size");
        invisible = plugin.getConfig().getBoolean("Zone.invisible");
        entityType = EntityType.valueOf(plugin.getConfig().getString("Zone.entity"));
        colliderName = plugin.getConfig().getString("Zone.colliderName");
        offsetX = plugin.getConfig().getInt("Zone.Particles.offsetX");
        offsetY = plugin.getConfig().getInt("Zone.Particles.offsetY");
        offsetZ = plugin.getConfig().getInt("Zone.Particles.offsetZ");
        speed = plugin.getConfig().getDouble("Zone.Particles.speed");
        particle = Particle.valueOf(plugin.getConfig().getString("Zone.Particles.type"));
        particleSize = (float) plugin.getConfig().getDouble("Zone.Particles.size");
        if(plugin.getConfig().contains("Zone.Particles.options")) {
            if(particle == Particle.REDSTONE) {
                int R = plugin.getConfig().getInt("Zone.Particles.options.R");
                int G = plugin.getConfig().getInt("Zone.Particles.options.G");
                int B = plugin.getConfig().getInt("Zone.Particles.options.B");
                dustOptions = new Particle.DustOptions(Color.fromRGB(R, G, B), particleSize);
            } else dustOptions = null;
        }
    }
}