package org.x00hero.TreeDetector.Trees.Types.Collapsable;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.x00hero.TreeDetector.Trees.Types.Tree;

public record FallingBranch(FallingBlock fallingBlock, BlockFace fallingFace, Tree tree) {
}
