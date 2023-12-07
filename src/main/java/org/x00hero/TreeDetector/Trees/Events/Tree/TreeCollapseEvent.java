package org.x00hero.TreeDetector.Trees.Events.Tree;

import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.x00hero.TreeDetector.Trees.Types.Collapsable.FallingBranch;
import org.x00hero.TreeDetector.Trees.Types.Tree;

import java.util.List;

public class TreeCollapseEvent extends Event {
    private final Tree tree;
    private final BlockFace fallingFace;
    public static HandlerList handlerList = new HandlerList();
    private List<FallingBranch> collapsingBlocks;

    public TreeCollapseEvent(Tree tree, BlockFace fallingFace, List<FallingBranch> collapsingBlocks) {
        this.tree = tree;
        this.fallingFace = fallingFace;
        this.collapsingBlocks = collapsingBlocks;
    }
    public Tree getTree() { return tree; }
    public BlockFace getFallingFace() { return fallingFace; }
    public List<FallingBranch> getCollapsingBlocks() { return collapsingBlocks; }

    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
