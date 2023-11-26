package org.x00hero.TreeDetector.Trees.Events.Tree.Zone;

import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Trees.Tree;
import org.x00hero.TreeDetector.Trees.TreeZone;

public class TreeZoneStartEvent extends TreeZoneEvent {
    public TreeZoneStartEvent(Tree tree, TreeZone zone, Player player) {
        super(tree, zone, player);
    }
}
