package org.x00hero.TreeDetector.Events.Tree;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.x00hero.TreeDetector.Components.Tree;

public class TreeHitEvent extends Event {
    private static HandlerList handlerList = new HandlerList();
    private final Tree tree;
    private final Player player;

    public TreeHitEvent(Tree tree, Player player) { this.tree = tree; this.player = player; }
    public Block getHitBlock() { return tree.initialBlock; }
    public Location getLocation() { return getHitBlock().getLocation(); }
    public Tree getTree() { return tree; }
    public Location getTreeLocation() { return tree.getBottomTrunk().getLocation(); }
    public Player getPlayer() { return player; }
    @Override
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHanderList() { return handlerList; }
}
