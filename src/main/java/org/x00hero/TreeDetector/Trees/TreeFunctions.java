package org.x00hero.TreeDetector.Trees;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.x00hero.TreeDetector.Main;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeCollapseEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static org.x00hero.TreeDetector.Main.CallEvent;
import static org.x00hero.TreeDetector.Main.divideVector;
import static org.x00hero.TreeDetector.Trees.Events.TreeDetection.fallingBlockList;

public class TreeFunctions {
    private static final Random random = new Random();
    public static int randomize(int max) { return (int) randomize(0, max); }
    public static float randomize(float max) { return randomize(0, max); }
   // public static int randomize(int min, int max) { return min + random.nextInt() * (max - min); }
    public static float randomize(float min, float max) { return min + random.nextFloat() * (max - min); }
    public static void PlaySoundAtBlock(Block block, Sound sound) { PlaySoundAtBlock(block, sound); }
    public static void PlaySoundAtBlock(Block block, String sound) { PlaySoundAtBlock(block, sound, 1f, 1f); }
    public static void PlaySoundAtBlock(Block block, Sound sound, float volume, float pitch) { PlaySoundAtLocation(block.getLocation(), sound, volume, pitch); }
    public static void PlaySoundAtBlock(Block block, String sound, float volume, float pitch) { PlaySoundAtLocation(block.getLocation(), sound, volume, pitch); }
    public static void PlaySoundAtLocation(Location location, Sound sound, float volume, float pitch) { location.getWorld().playSound(location, sound, volume, pitch); }
    public static void PlaySoundAtLocation(Location location, String sound, float volume, float pitch) { location.getWorld().playSound(location, sound, volume, pitch); }
    public static void PoofBlockOutOfExistence(Block block, int lifetime) { timeUntilDDay.put(block, System.currentTimeMillis() + (lifetime * 1000L)); }
    public static Sound[] treeSounds = new Sound[] {
            Sound.ITEM_CROSSBOW_QUICK_CHARGE_1, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, Sound.ITEM_CROSSBOW_QUICK_CHARGE_3,
            Sound.ITEM_CROSSBOW_LOADING_MIDDLE, Sound.ITEM_CROSSBOW_LOADING_START };
    public static void Collapse(Tree tree, Player player) { Collapse(tree, player.getFacing()); }
    public static void Collapse(Tree tree, BlockFace fallingFace) {
        CallEvent(new TreeCollapseEvent(tree, fallingFace));
        Sound randomSound = treeSounds[randomize(treeSounds.length-1)];
        tree.collapsed = true;
        PlaySoundAtLocation(tree.getLocation(), randomSound, 10f, 0.7f - (tree.getHeight() * 0.025f) + 0.01f);
        for(Block trunk : tree.connectedLogs) spawnFallingSand(trunk, fallingFace);
        for(Block leaves : tree.connectedLeaves) spawnFallingSand(leaves, fallingFace);
        tree.stopGame();
    }
    public static FallingBlock spawnFallingSand(Block block) { return spawnFallingSand(block.getLocation(), block.getType(), new Vector(0, -.5, 0)); }
    public static FallingBlock spawnFallingSand(Block block, BlockFace fallingFace) { return spawnFallingSand(block.getLocation(), block.getType(), fallingFace.getDirection()); }
    public static FallingBlock spawnFallingSand(Location location, Material material, Vector fallVector) {
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, material.createBlockData());
        fallingBlock.setVelocity(divideVector(fallVector, 3));
        fallingBlock.setDropItem(true);
        fallingBlockList.add(fallingBlock);
        return fallingBlock;
    }
    private static HashMap<Block, Long> timeUntilDDay = new HashMap<>();
    public static void DDayCheck() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
            Iterator<Map.Entry<Block, Long>> iterator = timeUntilDDay.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Block, Long> entry = iterator.next();
                Block block = entry.getKey();
                if (System.currentTimeMillis() >= entry.getValue()) {
                    block.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, block.getLocation(), 1);
                    if (block.getType() != Material.AIR)
                        block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
                    iterator.remove(); // Use iterator to remove the current element
                    block.setType(Material.AIR);
                    // PlaySoundAtBlock(block, Sound.ITEM_BUNDLE_INSERT);
                }
            }
        }, 0L, 20L);
    }
}
