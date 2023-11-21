package org.x00hero.TreeDetector.Events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.x00hero.TreeDetector.Components.Tree;

public class TreeHitEvent extends Event {
    private static HandlerList handlerList = new HandlerList();
    private final Tree tree;

    public TreeHitEvent(Tree tree) { this.tree = tree; }
    public Block getHitBlock() { return tree.initialBlock; }
    public Location getLocation() { return getHitBlock().getLocation(); }
    public Tree getTree() { return tree; }
    public Location getTreeLocation() { return tree.bottomTrunk.getLocation(); }
    @Override
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHanderList() { return handlerList; }
}
