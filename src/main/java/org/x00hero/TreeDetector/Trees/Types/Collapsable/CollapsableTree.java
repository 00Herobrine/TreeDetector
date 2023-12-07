package org.x00hero.TreeDetector.Trees.Types.Collapsable;

import org.bukkit.block.BlockFace;
import org.x00hero.TreeDetector.Trees.Types.Tree;

import static org.x00hero.TreeDetector.Trees.TreeFunctions.Collapse;
import static org.x00hero.TreeDetector.Trees.TreeFunctions.randomize;

public class CollapsableTree extends Tree {
    public boolean collapsed = false;
    public CollapsableTree(Tree tree) { super(tree); }
    public void collapse() { Collapse(this, fallDirection[randomize(fallDirection.length-1)]); }
    public void collapse(BlockFace fallingFace) { Collapse(this, fallingFace); }
}
