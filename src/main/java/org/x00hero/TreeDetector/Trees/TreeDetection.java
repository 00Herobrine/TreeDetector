package org.x00hero.TreeDetector.Trees;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.x00hero.TreeDetector.Controllers.ActivityController;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeBranchLandEvent;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeHitEvent;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeSwapEvent;
import org.x00hero.TreeDetector.Trees.Types.Interactive.InteractiveTree;
import org.x00hero.TreeDetector.Trees.Types.Tree;

import java.util.ArrayList;

import static org.x00hero.TreeDetector.Config.*;
import static org.x00hero.TreeDetector.Controllers.ActivityController.StartGame;
import static org.x00hero.TreeDetector.Main.*;
import static org.x00hero.TreeDetector.Trees.TreeFunctions.fallingBlockList;

public class TreeDetection implements Listener {
    @EventHandler
    public void Damage(EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION && e.getEntity() instanceof Slime slime)
            if(slime.getCustomName() != null && slime.getCustomName().equals(colliderName)) e.setCancelled(true);
    }

    @EventHandler
    public void EntityHit(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player player)) return;
        if(ActivityController.isActive(player)) {
            if(!(e.getEntity() instanceof Slime slime)) return;
            InteractiveTree tree = ActivityController.getTree(player);
            if(tree.collapsed) { ActivityController.setInactive(player); return; }
            Slime activeSlime = tree.zone.getSlime();
            if(activeSlime != null && activeSlime.equals(slime))
                tree.hitZone(player);
        }
    }

    @EventHandler
    public static void onBlockFall(EntityChangeBlockEvent e) {
        if(!(e.getEntity() instanceof FallingBlock fallingBlock)) return;
        else if(!fallingBlockList.containsKey(fallingBlock)) return;
        CallEvent(new TreeBranchLandEvent(fallingBlockList.get(fallingBlock)));
        fallingBlockList.remove(fallingBlock);
    }

    @EventHandler
    public void onTreeHit(PlayerInteractEvent e) {
        if(e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if(block == null || !isLogBlock(block)) return;
        Tree tree = getTree(block);
        if(!isTree(tree)) return;
        if(ActivityController.isActive(player)) {
            InteractiveTree activeTree = ActivityController.getTree(player);
            if(activeTree.getBottomTrunk().equals(tree.getBottomTrunk())) { // similar trees
                tree = activeTree;
                if(activeTree.zone.isExpired()) activeTree.randomizeZone();
                else activeTree.missedZone(player); // player missed slime
            } else { StartGame(player, e.getBlockFace(), tree); CallEvent(new TreeSwapEvent(tree, activeTree, player, e.getBlockFace())); } // swapped trees
        } else StartGame(player, e.getBlockFace(), tree);  // first time punching
        CallEvent(new TreeHitEvent(tree, player, e.getBlockFace()));
    }
    private static boolean isTree(Tree result) { return result != null && result.logCount() > logsThreshold && result.leafCount() > leavesThreshold; }
    private static boolean isValidBlock(Block block) { return isLogBlock(block) || isLeafBlock(block) || (isHiveBlock(block) && connectHives); }
    public static boolean isLogBlock(Block block) { return block.getType().name().toLowerCase().endsWith("log"); }
    public static boolean isLeafBlock(Block block) { return block.getType().name().toLowerCase().endsWith("leaves"); }
    public static boolean isHiveBlock(Block block) { return block.getType() == Material.BEE_NEST; }
    public static boolean isSameType(Block block, String typePrefix) { return typePrefix == null || block.getType().name().toLowerCase().startsWith(typePrefix); }
    public static Tree getTree(Block block) { return (isValidBlock(block)) ? getTree(block, maxSearchCalls) : null; }
    private static Tree getTree(Block currentBlock, int maxCalls) { Tree tree = new Tree(currentBlock); attachTreeBlocks(currentBlock, tree, maxCalls, new ArrayList<>()); return tree; }
    private static void attachTreeBlocks(Block currentBlock, Tree result, int maxCalls, ArrayList<Location> searched) {
        result.calls++;
        //if (searched.contains(currentBlock.getLocation())) return;
        searched.add(currentBlock.getLocation());
        if (!result.addBlock(currentBlock)) return;
        int currentX = currentBlock.getX();
        int currentY = currentBlock.getY();
        int currentZ = currentBlock.getZ();
        int initialX = result.initialBlock.getX();
        int initialY = result.initialBlock.getY();
        int initialZ = result.initialBlock.getZ();
        if (maxSearchHeight != -1 && (Math.abs(currentX - initialX) > maxSearchWidth || Math.abs(currentZ - initialZ) > maxSearchWidth)
                || (maxSearchHeight != -1 && Math.abs(currentY - initialY) > maxSearchHeight)) return;
        for (SearchDirection direction : searchDirections) {
            Block neighbor = currentBlock.getRelative(direction.getModX(), direction.getModY(), direction.getModZ());
            if(result.calls >= maxCalls) return;
            if (searched.contains(neighbor.getLocation()) || !isValidBlock(neighbor) || (!isSameType(neighbor, result.getTreeType()) && materialConsistency && !isHiveBlock(neighbor))) continue;
            attachTreeBlocks(neighbor, result, maxCalls, searched);
        }
    }
}
