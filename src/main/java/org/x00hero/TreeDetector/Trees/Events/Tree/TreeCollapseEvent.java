package org.x00hero.TreeDetector.Trees.Events.Tree;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.x00hero.TreeDetector.Trees.Tree;

import java.util.List;

public class TreeCollapseEvent extends Event {
    private final Tree tree;
    private final BlockFace fallingFace;
    public static HandlerList handlerList = new HandlerList();
    private List<FallingBlock> collapsingBlocks;

    public TreeCollapseEvent(Tree tree, BlockFace fallingFace) {
        this.tree = tree;
        this.fallingFace = fallingFace;
    }
    public Tree getTree() { return tree; }
    public BlockFace getFallingFace() { return fallingFace; }
    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
