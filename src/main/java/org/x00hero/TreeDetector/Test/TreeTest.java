package org.x00hero.TreeDetector.Test;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeBranchLandEvent;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeCollapseEvent;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeSwapEvent;
import org.x00hero.TreeDetector.Trees.Events.Zone.TreeZoneExpiredEvent;
import org.x00hero.TreeDetector.Trees.Events.Zone.TreeZoneStartEvent;
import org.x00hero.TreeDetector.Trees.Tree;
import org.x00hero.TreeDetector.Trees.Events.Tree.TreeHitEvent;
import org.x00hero.TreeDetector.Trees.Events.Zone.TreeZoneHitEvent;
import org.x00hero.TreeDetector.Trees.Events.Zone.TreeZoneMissEvent;

import static org.x00hero.TreeDetector.Config.*;
import static org.x00hero.TreeDetector.Main.log;
import static org.x00hero.TreeDetector.Trees.TreeFunctions.*;

public class TreeTest implements Listener {
    @EventHandler
    public void onTreeHit(TreeHitEvent e) {
        Tree tree = e.getTree();
        e.getPlayer().sendMessage("Hit a tree that is " + tree.getHeight() + " blocks tall.");
        e.getPlayer().sendMessage("Trunks Top: " + tree.getTopTrunk().getY() + " Bottom: " + tree.getBottomTrunk().getY());
        e.getPlayer().sendMessage("Leaves Top: " + tree.getTopLeaf().getY() + " Bottom: " + tree.getBottomLeaf().getY());
        e.getPlayer().sendMessage("Search Took " + tree.Result() + "ms with " + tree.calls + " blocks searched. Added " + (tree.connectedLeaves.size() + tree.connectedLogs.size()) + " blocks.");
        HighlightBlocks(tree.getConnectedBlocks(), Material.ORANGE_WOOL, 5);
    }
    @EventHandler
    public void onTreeSwap(TreeSwapEvent e) {
        e.getPlayer().sendMessage("Indecisive aren't ya");
    }
    @EventHandler
    public void onZoneHit(TreeZoneHitEvent e) {
        e.getPlayer().sendMessage("Hit " + e.getZonesHit() + " / " + e.getTree().requiredZones + " (" + e.getHitPercent() + "%)");
        if(e.getZonesHit() >= e.getTree().requiredZones && e.getHitPercent() > 75) e.getTree().collapse(e.getPlayer().getFacing());
    }
    @EventHandler
    public void onZoneMiss(TreeZoneMissEvent e) {
        e.getPlayer().sendMessage("Missed " + e.getTimesMissed() + "/" + e.getTimesHit() + " (" + e.getHitPercent() + "%)");
    }
    @EventHandler
    public void onZoneExpire(TreeZoneExpiredEvent e) {
        e.getPlayer().sendMessage("Too bad :/");
    }
    @EventHandler
    public void onZoneStart(TreeZoneStartEvent e) {
        e.getPlayer().sendMessage("Let the games begin");
    }
    @EventHandler
    public void onTreeCollapse(TreeCollapseEvent e) {
        log("Tree Collapsing @ " + e.getTree().getLocation());
    }
    @EventHandler
    public void onBranchLand(TreeBranchLandEvent e) {
        int random = randomize(branchSoundChance);
        if(random == branchSoundChance-1) PlaySoundAtBlock(e.getBlock(), e.getBlock().getBlockData().getSoundGroup().getStepSound(), branchVolume, branchPitch);
        PoofBlockOutOfExistence(e.getBlock(), branchLifetime);
        //log("Branch Landed @ " + e.getBlock().getLocation());
    }
}
