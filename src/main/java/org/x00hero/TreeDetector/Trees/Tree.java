package org.x00hero.TreeDetector.Trees;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Controllers.ActivityController;
import org.x00hero.TreeDetector.Trees.Events.Tree.Zone.TreeZoneMissEvent;
import org.x00hero.TreeDetector.Trees.Events.Tree.Zone.TreeZoneStartEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.x00hero.TreeDetector.Trees.Events.TreeDetection.*;
import static org.x00hero.TreeDetector.Main.CallEvent;
import static org.x00hero.TreeDetector.Trees.TreeFunctions.*;

public class Tree {
    public final Block initialBlock;
    private String trunkType;
    private String leafType;
    private Block bottomTrunk, topTrunk, topLeaf, bottomLeaf;
    public Set<Block> connectedLogs = new HashSet<>();
    public Set<Block> connectedLeaves = new HashSet<>();
    public Set<Block> connectedHives = new HashSet<>();
    public boolean collapsed = false;
    // Mini-Game
    public TreeZone zone;
    public int zonesHit, zonesTotal, timesHit;
    // Performance Monitoring
    public int calls;
    public long created = System.currentTimeMillis();
    public long completed = -1;
    public long Result() { return completed - created; }
    // Initializers
    public Tree(Block initialBlock) {
        this.initialBlock = initialBlock;
    }
    // Functions
    public int getTrunkHeight() { return topTrunk.getY() - bottomTrunk.getY(); }
    public int getHeight() { return topLeaf.getY() - bottomTrunk.getY(); }
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
    public void hitZone(Player player) { zonesHit++; timesHit++; zone.hit(this, player); randomizeZone(); }
    public void missedZone(Player player) { timesHit++; CallEvent(new TreeZoneMissEvent(this, zone, player)); }
    public void randomizeZone() { randomizeZone(null); }
    public void randomizeZone(@Nullable BlockFace face) { if(collapsed) return; zone.randomizeLocation(face); zone.spawnSlime(); zone.updateExpiration(); zonesTotal++; }
    public void displayZone(Player player) { /*displaySlime();*/ displayParticle(player); }
    public BlockFace[] fallDirection = new BlockFace[]{BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST};
    public void collapse() { Collapse(this, fallDirection[randomize(fallDirection.length-1)]); }
    public void collapse(BlockFace fallingFace) { Collapse(this, fallingFace); }
    //public void displaySlime(Player player) { }
    public void displayParticle(Player player) { zone.display(player); }
    public void stopGame() { zone.endSlime(); }
    public void startGame(Player player, @Nullable BlockFace face) {
        if(zone == null) zone = new TreeZone(initialBlock.getLocation(), face);
        if(ActivityController.isActive(player)) ActivityController.getTree(player).zone.endSlime();
        ActivityController.setActive(player, this);
        randomizeZone(face);
        displayZone(player);
        CallEvent(new TreeZoneStartEvent(this, zone, player));
    }
    public boolean addBlock(Block block) {
        if(connectedLogs.contains(block) || connectedLeaves.contains(block)) return false;
        if(isLogBlock(block)) { addLog(block); return true; }
        else if(isLeafBlock(block)) { addLeaf(block); return true; }
        else if(isHiveBlock(block)) { addHive(block); return true; }
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
}
