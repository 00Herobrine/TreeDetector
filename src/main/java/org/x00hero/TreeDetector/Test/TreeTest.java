package org.x00hero.TreeDetector.Test;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.x00hero.TreeDetector.Components.Tree;
import org.x00hero.TreeDetector.Events.Tree.TreeHitEvent;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneHitEvent;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneMissEvent;

public class TreeTest implements Listener {

    @EventHandler
    public void onZoneHit(TreeZoneHitEvent e) {
        e.getPlayer().sendMessage("Hit " + e.getZonesHit() + " / " + e.getTimesHit() + " (" + e.getHitPercent() + "%)");
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
        e.getPlayer().sendMessage("Search Took " + tree.Result() + "ms with " + tree.blocksSearched + " blocks searched." );
    }
}
