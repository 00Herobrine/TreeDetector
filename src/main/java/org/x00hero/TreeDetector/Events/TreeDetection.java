package org.x00hero.TreeDetector.Events;

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

import static org.x00hero.TreeDetector.Main.log;

public class TreeDetection implements Listener {
    int logsThreshold = 3;
    int leavesThreshold = 2; // amount of leaves attached required to be considered a "tree"
    static int maxSearchDistance = 10; // distance from initial block

    @EventHandler
    public void Damage(EntityDamageEvent e) {
        if(e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION && e.getEntity() instanceof Slime slime)
            if(slime.getCustomName() != null && slime.getCustomName().equalsIgnoreCase("TREE-COLLIDER")) e.setCancelled(true);
    }

    @EventHandler
    public void EntityHit(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player player)) return;
        if(ActivityManager.isActive(player)) {
            if(!(e.getEntity() instanceof Slime slime)) return;
            Tree tree = ActivityManager.getTree(player);
            log("Hitting entity");
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
        Tree result = getTree(block);
        if(!isTree(result)) return;
        if(ActivityManager.isActive(player)) {
            Tree activeTree = ActivityManager.getTree(player);
            if(activeTree.getBottomTrunk().equals(result.getBottomTrunk())) { // similar trees
                if(activeTree.zone.isExpired()) activeTree.randomizeZone();
                else activeTree.missedZone(player); // player missed slime
            } else result.startGame(player); // swapped trees
        } else result.startGame(player);
    }

    private static final BlockFace[] searchDirections = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
    private boolean isTree(Tree result) { return result.logCount() > logsThreshold && result.leafCount() > leavesThreshold; }
    private static boolean isValidBlock(Block block) { return isLogBlock(block) || isLeafBlock(block); }
    public static boolean isLogBlock(Block block) { return block.getType().name().toLowerCase().endsWith("log"); }
    public static boolean isLeafBlock(Block block) { return block.getType().name().toLowerCase().endsWith("leaves"); }
    public Tree getTree(Block block) { return findConnectedBlocks(block, maxSearchDistance); }
    public static Tree findConnectedBlocks(Block initialBlock, int maxSearchDistance) {
        Tree result = new Tree(initialBlock);
        searchConnectedBlocks(initialBlock, result, maxSearchDistance);
        return result;
    }
    private static void searchConnectedBlocks(Block currentBlock, Tree result, int remainingSearchDistance) {
        if (currentBlock.getLocation().distance(result.initialBlock.getLocation()) > maxSearchDistance) return;
        for (BlockFace face : searchDirections) {
            Block neighbor = currentBlock.getRelative(face);
            if (isValidBlock(neighbor) && !result.connectedLogs.contains(neighbor) && !result.connectedLeaves.contains(neighbor)) {
                if (isLogBlock(neighbor)) result.addLog(neighbor);
                 else if (isLeafBlock(neighbor)) result.addLeaf(neighbor);
                searchConnectedBlocks(neighbor, result, remainingSearchDistance - 1);
            }
        }
    }
}
