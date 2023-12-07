package org.x00hero.TreeDetector.Trees;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.x00hero.TreeDetector.Main;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeBranchFallEvent;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeCollapseEvent;
import org.x00hero.TreeDetector.Trees.Types.Collapsable.CollapsableTree;
import org.x00hero.TreeDetector.Trees.Types.Collapsable.FallingBranch;
import org.x00hero.TreeDetector.Trees.Types.Interactive.InteractiveTree;
import org.x00hero.TreeDetector.Trees.Types.Tree;

import java.util.*;

import static org.x00hero.TreeDetector.Config.*;
import static org.x00hero.TreeDetector.Controllers.ActivityController.schedulers;
import static org.x00hero.TreeDetector.Main.*;

public class TreeFunctions {
    private static final Random random = new Random();
    public static HashMap<FallingBlock, FallingBranch> fallingBlockList = new HashMap<>();
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
    public static void PoofBlockOutOfExistence(Block block, int lifetime) {
        timeUntilDDay.put(block, System.currentTimeMillis() + (lifetime * 1000L));
    }
    public static void Collapse(CollapsableTree tree, Player player) { Collapse(tree, player.getFacing()); }
    public static void Collapse(CollapsableTree tree, BlockFace fallingFace) {
        Sound randomSound = collapseSounds[randomize(collapseSounds.length-1)];
        tree.collapsed = true;
        PlaySoundAtLocation(tree.getLocation(), randomSound, collapseVolume, collapsePitch - (tree.getHeight() * collapsePitchMod) + 0.01f);
        List<FallingBranch> fallingBranches = new ArrayList<>();
        CallEvent(new TreeCollapseEvent(tree, fallingFace, fallingBranches));
        for(Block block : tree.getConnectedBlocks())
            fallingBranches.add(spawnFallingBranch(block, fallingFace, tree));
        tree.connectedLogs.clear();
        tree.connectedHives.clear();
        tree.connectedLogs.clear();
        if(tree instanceof InteractiveTree) ((InteractiveTree) tree).stopGame();
    }
    public static FallingBlock spawnFallingSand(Block block, BlockFace fallingFace) { return spawnFallingSand(block, fallingFace.getDirection()); }
    public static FallingBlock spawnFallingSand(Block block, Vector fallVector) { return spawnFallingSand(block.getLocation(), block.getType(), fallVector); }
    public static FallingBlock spawnFallingSand(Location location, Material material, Vector fallVector) {
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, material.createBlockData());
        fallingBlock.setVelocity(fallVector.multiply(velocityMod));
        fallingBlock.setDropItem(true);
        return fallingBlock;
    }
    public static FallingBranch spawnFallingBranch(Block block, BlockFace fallingFace, Tree tree) {
        FallingBranch branch = new FallingBranch(spawnFallingSand(block, fallingFace), fallingFace, tree);
        fallingBlockList.put(branch.fallingBlock(), branch);
        CallEvent(new TreeBranchFallEvent(branch));
        return branch;
    }
    private static HashMap<Block, Long> timeUntilDDay = new HashMap<>();
    public static void DDayCheck() {
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {
            Iterator<Map.Entry<Block, Long>> iterator = timeUntilDDay.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Block, Long> entry = iterator.next();
                Block block = entry.getKey();
                if (System.currentTimeMillis() >= entry.getValue()) {
                    DropAndDeleteBlock(block);
                    iterator.remove();
                }
            }
        }, 0L, expirationCheckRate);
        schedulers.add(id);
    }
    public static void DropAndDeleteBlock(Block block) {
        block.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, block.getLocation(), 1);
        if (block.getType() != Material.AIR) {
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
            PlaySoundAtBlock(block, dropSound, dropVolume, dropPitch);
        }
        block.setType(Material.AIR);
    }
    public static void HighlightBlocks(List<Block> blocks, Material highlightMaterial, int duration) {
        for(Block block : blocks) {
            if(block.getType() == Material.AIR || block.getType() == highlightMaterial) continue;
            temporarilyReplaceBlock(block, highlightMaterial, duration);
        }
    }
    public static void temporarilyReplaceBlock(Block block, Material tempMaterial, int duration) {
        Material originalMaterial = block.getType();
        block.setType(tempMaterial);
        new BukkitRunnable() {
            @Override
            public void run() {
                resetBlockType(block, originalMaterial);
            }
        }.runTaskLater(plugin, duration * 20L);
    }
    private static void resetBlockType(Block block, Material originalMaterial) { block.setType(originalMaterial); }
}
