package org.x00hero.TreeDetector.Trees.Events.Zone;

import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Trees.Types.Interactive.InteractiveTree;
import org.x00hero.TreeDetector.Trees.Types.Tree;
import org.x00hero.TreeDetector.Trees.Types.Interactive.TreeZone;

public class TreeZoneMissEvent extends TreeZoneEvent {
    public TreeZoneMissEvent(InteractiveTree tree, TreeZone zone, Player player) { super(tree, zone, player); }
}
