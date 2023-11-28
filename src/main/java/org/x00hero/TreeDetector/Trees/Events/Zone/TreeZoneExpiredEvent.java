package org.x00hero.TreeDetector.Trees.Events.Zone;

import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Trees.Tree;
import org.x00hero.TreeDetector.Trees.TreeZone;

public class TreeZoneExpiredEvent extends TreeZoneEvent {
    public TreeZoneExpiredEvent(Tree tree, TreeZone zone, Player player) {
        super(tree, zone, player);
    }
}
