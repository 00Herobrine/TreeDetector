package org.x00hero.TreeDetector.Events.Tree.Zone;

import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Components.Tree;
import org.x00hero.TreeDetector.Components.TreeZone;

public class TreeZoneExpiredEvent extends TreeZoneEvent {
    public TreeZoneExpiredEvent(Tree tree, TreeZone zone, Player player) {
        super(tree, zone, player);
    }
}
