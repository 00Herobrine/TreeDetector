package org.x00hero.TreeDetector.Events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.x00hero.TreeDetector.ActivityManager;
import org.x00hero.TreeDetector.Components.Tree;
import org.x00hero.TreeDetector.Events.Tree.TreeHitEvent;

import java.util.ArrayList;

import static org.x00hero.TreeDetector.Config.*;
import static org.x00hero.TreeDetector.Main.CallEvent;

public class TreeDetection implements Listener {
    @EventHandler
    public void Damage(EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION && e.getEntity() instanceof Slime slime)
            if(slime.getCustomName() != null && slime.getCustomName().equals(colliderName)) e.setCancelled(true);
    }

    @EventHandler
    public void EntityHit(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player player)) return;
        if(ActivityManager.isActive(player)) {
            if(!(e.getEntity() instanceof Slime slime)) return;
            Tree tree = ActivityManager.getTree(player);
            Slime activeSlime = tree.zone.getSlime();
            if(activeSlime != null && activeSlime.equals(slime))
                tree.hitZone(player);
        }
    }

    @EventHandler
    public void onTreeHit(PlayerInteractEvent e) {
        if(e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if(block == null || !isLogBlock(block)) return;
        Tree tree = getTree(block, maxSearchCalls);
        if(!isTree(tree)) return;
        if(ActivityManager.isActive(player)) {
            Tree activeTree = ActivityManager.getTree(player);
            if(activeTree.getBottomTrunk().equals(tree.getBottomTrunk())) { // similar trees
                tree = activeTree;
                if(activeTree.zone.isExpired()) activeTree.randomizeZone();
                else activeTree.missedZone(player); // player missed slime
            } else tree.startGame(player, e.getBlockFace()); // swapped trees
        } else tree.startGame(player, e.getBlockFace()); // first time punching
        CallEvent(new TreeHitEvent(tree, player));
    }

    private static final BlockFace[] searchDirections = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
    private static boolean isTree(Tree result) { return result.logCount() > logsThreshold && result.leafCount() > leavesThreshold; }
    private static boolean isValidBlock(Block block) { return (isLogBlock(block) || isLeafBlock(block)); }
    public static boolean isLogBlock(Block block) { return block.getType().name().toLowerCase().endsWith("log"); }
    public static boolean isLeafBlock(Block block) { return block.getType().name().toLowerCase().endsWith("leaves"); }
    public static boolean isSameType(Block block, String typePrefix) { return block.getType().name().toLowerCase().startsWith(typePrefix); }
    public static Tree getTree(Block block) { return (isValidBlock(block)) ? getTree(block, maxSearchCalls) : null; }
    private static Tree getTree(Block currentBlock, int searchableBlocks) { Tree tree = new Tree(currentBlock); attachTreeBlocks(currentBlock, searchableBlocks, tree, new ArrayList<>()); return tree; }
    private static void attachTreeBlocks(Block currentBlock, int searchableBlocks, Tree result, ArrayList<Location> searched) {
        result.calls++;
        if (searched.contains(currentBlock.getLocation())) return;
        searched.add(currentBlock.getLocation());
        if (!result.addBlock(currentBlock)) return;
        int currentX = currentBlock.getX();
        int currentY = currentBlock.getY();
        int currentZ = currentBlock.getZ();
        int initialX = result.initialBlock.getX();
        int initialY = result.initialBlock.getY();
        int initialZ = result.initialBlock.getZ();
        if (Math.abs(currentX - initialX) > maxSearchWidth || Math.abs(currentZ - initialZ) > maxSearchWidth
                || Math.abs(currentY - initialY) > maxSearchHeight || searchableBlocks <= 0
                || (!isSameType(currentBlock, result.getTreeType()) && materialConsistency)) {
            return;
        }
        for (BlockFace face : searchDirections) {
            Block neighbor = currentBlock.getRelative(face);
            if (searched.contains(neighbor.getLocation())) continue;
            attachTreeBlocks(neighbor, searchableBlocks - 1, result, searched);
        }
    }
}
