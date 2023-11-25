package org.x00hero.TreeDetector;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;

import static org.x00hero.TreeDetector.Main.plugin;

public class Config {
    //region Detection
    public static int logsThreshold; // required amount of logs attached
    public static int leavesThreshold; // required amount of leaves attached to be considered a Tree
    public static int maxSearchWidth; // distance from initial block (X & Z)
    public static int maxSearchHeight; // distance from initial block (Y)
    public static int maxSearchCalls; // max blocks to iterate through
    public static boolean materialConsistency; // if leaves match the trunk type
    //endregion

    //region Zone
    public static int zoneSize;
    public static boolean invisible;
    public static EntityType entityType;
    public static String colliderName;
    public static float constraintX;
    public static float constraintY;
    public static float faceOffset;
    public static Sound sound;
    public static float soundPitch;
    public static float soundVolume;
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
        maxSearchWidth = plugin.getConfig().getInt("Detection.maxSearchWidth");
        maxSearchHeight = plugin.getConfig().getInt("Detection.maxSearchHeight");
        maxSearchCalls = plugin.getConfig().getInt("Detection.maxSearchCalls");
        materialConsistency = plugin.getConfig().getBoolean("Detection.materialConsistency");
        particleAmount = plugin.getConfig().getInt("Zone.Particles.amount");
        updateRate = plugin.getConfig().getLong("Zone.Particles.updateRate");
        zoneSize = plugin.getConfig().getInt("Zone.size");
        invisible = plugin.getConfig().getBoolean("Zone.invisible");
        entityType = EntityType.valueOf(plugin.getConfig().getString("Zone.entity"));
        colliderName = plugin.getConfig().getString("Zone.colliderName");
        offsetX = plugin.getConfig().getDouble("Zone.Particles.offsetX");
        offsetY = plugin.getConfig().getDouble("Zone.Particles.offsetY");
        offsetZ = plugin.getConfig().getDouble("Zone.Particles.offsetZ");
        speed = plugin.getConfig().getDouble("Zone.Particles.speed");
        particle = Particle.valueOf(plugin.getConfig().getString("Zone.Particles.type"));
        particleSize = (float) plugin.getConfig().getDouble("Zone.Particles.size");
        constraintX = (float) plugin.getConfig().getDouble("Zone.constraint.x");
        constraintY = (float) plugin.getConfig().getDouble("Zone.constraint.y");
        faceOffset = (float) plugin.getConfig().getDouble("Zone.constraint.faceOffset");
        sound = Sound.valueOf(plugin.getConfig().getString("Zone.sound.audio"));
        soundPitch = (float) plugin.getConfig().getDouble("Zone.sound.pitch");
        soundVolume = (float) plugin.getConfig().getDouble("Zone.sound.volume");
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
