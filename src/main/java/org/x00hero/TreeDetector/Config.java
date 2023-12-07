package org.x00hero.TreeDetector;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.x00hero.TreeDetector.Trees.SearchDirection;

import static org.x00hero.TreeDetector.Main.plugin;

public class Config {
    //region Detection
    public static int logsThreshold; // required amount of logs attached
    public static int logDeviation; // allowed distance from another log to be considered the same tree
    public static int leavesThreshold; // required amount of leaves attached to be considered a Tree
    public static int maxSearchWidth; // distance from initial block (X & Z)
    public static int maxSearchHeight; // distance from initial block (Y)
    public static int maxSearchCalls; // max blocks to iterate through
    public static int expirationCheckRate; // frequency for Mini-Game expiration in Ticks
    public static boolean materialConsistency; // if leaves match the trunk type
    public static boolean connectHives;
    public static SearchDirection[] searchDirections;
    //endregion

    //region Zone
    public static int zoneSize;
    public static boolean invisible;
    public static EntityType entityType;
    public static String colliderName;
    public static float constraintX;
    public static float constraintY;
    public static float faceOffset;
    public static Sound zoneSound;
    public static float zoneSoundPitch;
    public static float zoneSoundVolume;
    public static int zoneTimeout;
    //region Particle
    public static int particleAmount;
    public static long particleUpdateRate;
    public static double particleOffsetX;
    public static double particleOffsetY;
    public static double particleOffsetZ;
    public static double particleSpeed;
    public static float particleSize;
    public static Particle particle;
    public static Particle.DustOptions dustOptions;
    //endregion
    //endregion

    //region Collapse
    public static Sound[] collapseSounds;
    public static float collapsePitch;
    public static float collapsePitchMod;
    public static float collapseVolume;
    public static float collapseVolumeMod;
    public static int collapseSoundChance;
    public static int baseZones;
    public static int zonesMod;
    public static float requiredPercentage;
    //region Branch
    public static float branchVolume;
    public static float branchPitch;
    public static int branchLifetime;
    public static int branchSoundChance;
    public static float velocityMod;
    //endregion

    //region Drop
    public static Sound dropSound;
    public static float dropPitch;
    public static float dropVolume;
    //endregion
    //endregion

    public static void Load() {
        plugin.reloadConfig();
        //region Detection
        logsThreshold = plugin.getConfig().getInt("Detection.logThreshold");
        logDeviation = plugin.getConfig().getInt("Detection.logDeviation");
        leavesThreshold = plugin.getConfig().getInt("Detection.leavesThreshold");
        maxSearchWidth = plugin.getConfig().getInt("Detection.maxSearchWidth");
        maxSearchHeight = plugin.getConfig().getInt("Detection.maxSearchHeight");
        maxSearchCalls = plugin.getConfig().getInt("Detection.maxSearchCalls");
        expirationCheckRate = plugin.getConfig().getInt("Detection.expirationCheckRate");
        materialConsistency = plugin.getConfig().getBoolean("Detection.materialConsistency");
        connectHives = plugin.getConfig().getBoolean("Detection.connectHives");
        searchDirections = plugin.getConfig().getList("Detection.searchDirections").stream()
                .map(o -> SearchDirection.valueOf((String) o))
                .toArray(SearchDirection[]::new);
        //endregion

        //region Zone
        zoneSize = plugin.getConfig().getInt("Zone.size");
        zoneTimeout = plugin.getConfig().getInt("Zone.timeout");
        invisible = plugin.getConfig().getBoolean("Zone.invisible");
        entityType = EntityType.valueOf(plugin.getConfig().getString("Zone.entity"));
        colliderName = plugin.getConfig().getString("Zone.colliderName");
        constraintX = (float) plugin.getConfig().getDouble("Zone.constraint.x");
        constraintY = (float) plugin.getConfig().getDouble("Zone.constraint.y");
        faceOffset = (float) plugin.getConfig().getDouble("Zone.constraint.faceOffset");
        zoneSound = Sound.valueOf(plugin.getConfig().getString("Sound.Zone.audio"));
        zoneSoundPitch = (float) plugin.getConfig().getDouble("Sound.Zone.pitch");
        zoneSoundVolume = (float) plugin.getConfig().getDouble("Sound.Zone.volume");
        //region Particles
        particleAmount = plugin.getConfig().getInt("Zone.Particles.amount");
        particleUpdateRate = plugin.getConfig().getLong("Zone.Particles.updateRate");
        particleOffsetX = plugin.getConfig().getDouble("Zone.Particles.offsetX");
        particleOffsetY = plugin.getConfig().getDouble("Zone.Particles.offsetY");
        particleOffsetZ = plugin.getConfig().getDouble("Zone.Particles.offsetZ");
        particleSpeed = plugin.getConfig().getDouble("Zone.Particles.speed");
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
        //endregion
        //endregion

        //region Collapse
        collapseSounds = plugin.getConfig().getList("Sound.Collapse.audio").stream()
                .map(o -> Sound.valueOf((String) o))
                .toArray(Sound[]::new);
        collapsePitch = (float) plugin.getConfig().getDouble("Sound.Collapse.pitch");
        collapsePitchMod = (float) plugin.getConfig().getDouble("Sound.Collapse.pitchMod");
        collapseVolume = (float) plugin.getConfig().getDouble("Sound.Collapse.volume");
        collapseVolumeMod = (float) plugin.getConfig().getDouble("Sound.Collapse.volumeMod");
        collapseSoundChance = plugin.getConfig().getInt("Sound.Collapse.chance");
        branchPitch = (float) plugin.getConfig().getDouble("Sound.Land.pitch");
        branchVolume = (float) plugin.getConfig().getDouble("Sound.Land.volume");
        branchLifetime = plugin.getConfig().getInt("Tree.Collapse.branchLifetime");
        branchSoundChance = plugin.getConfig().getInt("Sound.Land.chance");
        dropSound = Sound.valueOf(plugin.getConfig().getString("Sound.Drop.audio"));
        dropPitch = (float) plugin.getConfig().getDouble("Sound.Drop.pitch");
        dropVolume = (float) plugin.getConfig().getDouble("Sound.Drop.volume");
        velocityMod = (float) plugin.getConfig().getDouble("Tree.Collapse.velocityMod");
        baseZones = plugin.getConfig().getInt("Tree.Collapse.requiredZones");
        zonesMod = plugin.getConfig().getInt("Tree.Collapse.zonesMod");
        requiredPercentage = (float) plugin.getConfig().getDouble("Tree.Collapse.requiredPercentage");
        //endregion
    }

}
