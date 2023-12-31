package org.x00hero.TreeDetector.Trees.Events.Tree;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Trees.Types.Tree;

public class TreeSwapEvent extends TreeHitEvent {
    public final Tree initialTree;
    public TreeSwapEvent(Tree tree, Tree initialTree, Player player, BlockFace hitFace) {
        super(tree, player, hitFace);
        this.initialTree = initialTree;
    }
    public Tree getInitialTree() { return initialTree; }
}
