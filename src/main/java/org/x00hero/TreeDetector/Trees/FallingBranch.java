package org.x00hero.TreeDetector.Trees;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;

public record FallingBranch(FallingBlock fallingBlock, BlockFace fallingFace, Tree tree) {
}
