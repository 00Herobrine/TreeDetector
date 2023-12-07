package org.x00hero.TreeDetector.Trees.Events.Tree;

import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.x00hero.TreeDetector.Trees.Types.Collapsable.FallingBranch;
import org.x00hero.TreeDetector.Trees.Types.Tree;

public class TreeBranchEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private final FallingBranch fallingBranch;
    public TreeBranchEvent(FallingBranch fallingBranch) { this.fallingBranch = fallingBranch; }
    public FallingBranch getFallingBranch() { return fallingBranch; }
    public FallingBlock getFallingBlock() { return fallingBranch.fallingBlock(); }
    public Block getBlock() { return getFallingBlock().getLocation().getBlock(); }
    public Tree getTree() { return fallingBranch.tree(); }
    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
