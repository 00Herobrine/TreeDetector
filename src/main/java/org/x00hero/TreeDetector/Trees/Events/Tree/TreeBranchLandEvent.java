package org.x00hero.TreeDetector.Trees.Events.Tree;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TreeBranchLandEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private final Block block;
    //private final Tree tree;
    public TreeBranchLandEvent(Block block) {
        this.block = block;
        //this.tree = tree;
    }
    public Block getBlock() { return block; }
    //public Tree getTree() { return tree; }
    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
