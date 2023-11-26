package org.x00hero.TreeDetector.Trees.Events.Tree;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.x00hero.TreeDetector.Trees.Tree;

public class TreeHitEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private final Tree tree;
    private final Player player;
    private final BlockFace face;

    public TreeHitEvent(Tree tree, Player player, BlockFace face) { this.tree = tree; this.player = player; this.face = face; }

    public Tree getTree() { return tree; }
    public Player getPlayer() { return player; }
    public Block getHitBlock() { return tree.initialBlock; }
    public BlockFace getFace() { return face; }
    public Location getLocation() { return getHitBlock().getLocation(); }
    public Location getTreeLocation() { return tree.getBottomTrunk().getLocation(); }
    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
