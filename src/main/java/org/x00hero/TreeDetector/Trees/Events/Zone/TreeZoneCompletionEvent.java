package org.x00hero.TreeDetector.Trees.Events.Zone;

import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Trees.Tree;
import org.x00hero.TreeDetector.Trees.TreeZone;

public class TreeZoneCompletionEvent extends TreeZoneEvent {
    public TreeZoneCompletionEvent(Tree tree, TreeZone zone, Player player) {
        super(tree, zone, player);
    }
}