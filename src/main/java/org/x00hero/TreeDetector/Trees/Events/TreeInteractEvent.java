package org.x00hero.TreeDetector.Trees.Events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.x00hero.TreeDetector.Trees.Types.Tree;

public class TreeInteractEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private final Tree tree;
    private final Player player;
    private final BlockFace face;

    public TreeInteractEvent(Tree tree, Player player, BlockFace face) { this.tree = tree; this.player = player; this.face = face; }

    public Tree getTree() { return tree; }
    public Player getPlayer() { return player; }
    public Block getBlock() { return tree.initialBlock; }
    public BlockFace getFace() { return face; }
    public Location getLocation() { return getBlock().getLocation(); }
    public Location getTreeLocation() { return tree.getBottomTrunk().getLocation(); }
    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
