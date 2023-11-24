package org.x00hero.TreeDetector.Events.Tree;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.x00hero.TreeDetector.Components.Tree;

public class TreeHitEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private final Tree tree;
    private final Player player;

    public TreeHitEvent(Tree tree, Player player) { this.tree = tree; this.player = player; }

    public Tree getTree() { return tree; }
    public Player getPlayer() { return player; }
    public Block getHitBlock() { return tree.initialBlock; }
    public Location getLocation() { return getHitBlock().getLocation(); }
    public Location getTreeLocation() { return tree.getBottomTrunk().getLocation(); }
    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
