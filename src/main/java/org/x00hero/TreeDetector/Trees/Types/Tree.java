package org.x00hero.TreeDetector.Trees.Types;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.*;

import static org.x00hero.TreeDetector.Config.*;
import static org.x00hero.TreeDetector.Trees.TreeDetection.*;
import static org.x00hero.TreeDetector.Trees.TreeFunctions.*;

public class Tree {
    public final Block initialBlock;
    private String trunkType;
    private String leafType;
    private Block bottomTrunk, topTrunk, topLeaf, bottomLeaf;
    public Set<Block> connectedLogs = new HashSet<>();
    public Set<Block> connectedLeaves = new HashSet<>();
    public Set<Block> connectedHives = new HashSet<>();
    //region Performance Monitoring
    public int calls;
    public long created = System.currentTimeMillis();
    public long completed = -1;
    public long Result() { return completed - created; }
    //endregion

    //region Initializers
/*    public Tree(Set<Block> connectedLogs, Set<Block> connectedLeaves, Set<Block> connectedHives) {
        initialBlock = (Block) connectedLogs.toArray()[0];
        this.connectedLogs = connectedLogs;
        this.connectedLeaves = connectedLeaves;
        this.connectedHives = connectedHives;
    }*/
    public Tree(Tree tree) {
        this.bottomTrunk = tree.bottomTrunk;
        this.topTrunk = tree.topTrunk;
        this.topLeaf = tree.topLeaf;
        this.bottomLeaf = tree.bottomLeaf;
        this.trunkType = tree.trunkType;
        this.leafType = tree.leafType;
        this.initialBlock = tree.initialBlock;
        this.connectedLogs = tree.connectedLogs;
        this.connectedLeaves = tree.connectedLeaves;
        this.connectedHives = tree.connectedHives;
        this.calls = tree.calls;
        this.created = tree.created;
        this.completed = tree.completed;
    }
    public Tree(Block initialBlock) {
        this.initialBlock = initialBlock;
    }
    //endregion

    //region Functions
    public int getTrunkHeight() { return topTrunk.getY() - bottomTrunk.getY(); }
    public int getHeight() { return (topLeaf.getY() - bottomTrunk.getY()) + 1; }
    public int logCount() { return connectedLogs.size(); }
    public int leafCount() { return connectedLeaves.size(); }
    public Location getLocation() { return bottomTrunk.getLocation(); }
    public String getTreeType() { return trunkType; }
    public String getLeafType() { return leafType; }
    public Material getTrunkType() { return getBottomTrunk().getType(); }
    public Block getBottomTrunk() { return bottomTrunk; }
    public Block getTopTrunk() { return topTrunk; }
    public Block getBottomLeaf() { return bottomLeaf; }
    public Block getTopLeaf() { return topLeaf; }
    public Block getInitialBlock() { return initialBlock; }
    public Block getGround() { return bottomTrunk.getRelative(BlockFace.DOWN); }
    public List<Block> getConnectedBlocks() { List<Block> blocks = new ArrayList<>(); blocks.addAll(connectedHives); blocks.addAll(connectedLeaves); blocks.addAll(connectedLogs); return blocks; }
    public void playSound(Sound sound) { PlaySoundAtBlock(bottomTrunk, sound); }
    public void playSound(String sound) { PlaySoundAtBlock(bottomTrunk, sound); }
    public void playSound(Sound sound, float volume, float pitch) { PlaySoundAtBlock(bottomTrunk, sound, volume, pitch); }
    public void playSound(String sound, float volume, float pitch) { PlaySoundAtBlock(bottomTrunk, sound, volume, pitch); }
    public static BlockFace[] fallDirection = new BlockFace[]{BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST};
    public boolean addBlock(Block block) {
        if(connectedLogs.contains(block) || connectedLeaves.contains(block)) return false;
        if(isLogBlock(block)) {
            if(BlockWithinDistance(block, connectedLogs.stream().toList(), logDeviation)) { addLog(block); return true; }
            return false;
        } else if(isLeafBlock(block)) { addLeaf(block); return true;
        } else if(isHiveBlock(block)) { addHive(block); return true;
        }
        return false;
    }
    public static boolean BlockWithinDistance(Block block, List<Block> blocks, int distance) {
        if(blocks.size() == 0) return true;
        for(Block connectedBlock : blocks) {
            Location connectedLocation = connectedBlock.getLocation();
            if(connectedLocation.distance(block.getLocation()) < distance) return true;
        }
        return false;
    }
    public void addLog(Block block) {
        connectedLogs.add(block);
        if(bottomTrunk == null || block.getLocation().getY() < bottomTrunk.getLocation().getY()) bottomTrunk = block;
        if(topTrunk == null || block.getLocation().getY() > topTrunk.getLocation().getY()) topTrunk = block;
        if(trunkType == null) { trunkType = block.getType().name().toLowerCase().replace("_log", ""); }
        completed = System.currentTimeMillis();
    }
    public void addLeaf(Block block) {
        connectedLeaves.add(block);
        if(bottomLeaf == null || block.getLocation().getY() < bottomLeaf.getLocation().getY()) bottomLeaf = block;
        if(topLeaf == null || block.getLocation().getY() > topTrunk.getLocation().getY()) topLeaf = block;
        if(leafType == null) leafType = block.getType().name().toLowerCase().replace("_leaves", "");
        completed = System.currentTimeMillis();
    }
    public void addHive(Block block) {
        connectedHives.add(block);
        completed = System.currentTimeMillis();
    }
    //endregion
}
