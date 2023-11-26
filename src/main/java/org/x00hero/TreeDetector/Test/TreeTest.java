package org.x00hero.TreeDetector.Test;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.x00hero.TreeDetector.Components.Tree;
import org.x00hero.TreeDetector.Events.Tree.TreeHitEvent;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneHitEvent;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneMissEvent;
import org.x00hero.TreeDetector.Main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.x00hero.TreeDetector.Components.TreeZone.randomize;
import static org.x00hero.TreeDetector.Events.TreeDetection.fallingBlockList;
import static org.x00hero.TreeDetector.Main.*;

public class TreeTest implements Listener {

    @EventHandler
    public void onZoneHit(TreeZoneHitEvent e) {
        e.getPlayer().sendMessage("Hit " + e.getZonesHit() + " / " + e.getTimesHit() + " (" + e.getHitPercent() + "%)");
        if(e.getZonesHit() >= 6 && e.getHitPercent() > 75) Collapse(e.getTree(), e.getPlayer());
    }

    @EventHandler
    public void onZoneMiss(TreeZoneMissEvent e) {
        e.getPlayer().sendMessage("Missed " + e.getTimesMissed() + "/" + e.getTimesHit() + " (" + e.getHitPercent() + "%)");
    }

    @EventHandler
    public void onTreePunch(TreeHitEvent e) {
        Tree tree = e.getTree();
        e.getPlayer().sendMessage("Hit a tree that is " + tree.getHeight() + " blocks tall.");
        e.getPlayer().sendMessage("Trunks Top: " + tree.getTopTrunk().getY() + " Bottom: " + tree.getBottomTrunk().getY());
        e.getPlayer().sendMessage("Leaves Top: " + tree.getTopLeaf().getY() + " Bottom: " + tree.getBottomLeaf().getY());
        e.getPlayer().sendMessage("Search Took " + tree.Result() + "ms with " + tree.calls + " blocks searched. Added " + (tree.connectedLeaves.size() + tree.connectedLogs.size()) + " blocks.");
    }

    public static Sound[] treeSounds = new Sound[] {
            Sound.ITEM_CROSSBOW_QUICK_CHARGE_1, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, Sound.ITEM_CROSSBOW_QUICK_CHARGE_3,
                        Sound.ITEM_CROSSBOW_LOADING_MIDDLE, Sound.ITEM_CROSSBOW_LOADING_START };
    public static void Collapse(Tree tree, Player player) { Collapse(tree, player.getFacing()); }
    public static void Collapse(Tree tree, BlockFace fallingFace) {
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
    public static void PoofBlockOutOfExistence(Block block, int lifetime) {
        timeUntilDDay.put(block, System.currentTimeMillis() + (lifetime * 1000L));
    }
}
