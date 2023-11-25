package org.x00hero.TreeDetector.Components;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.ActivityManager;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneMissEvent;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneStartEvent;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

import static org.x00hero.TreeDetector.Events.TreeDetection.isLeafBlock;
import static org.x00hero.TreeDetector.Events.TreeDetection.isLogBlock;
import static org.x00hero.TreeDetector.Main.CallEvent;

public class Tree {
    public final Block initialBlock;
    private String trunkType;
    private String leafType;
    private Block bottomTrunk, topTrunk, topLeaf, bottomLeaf;
    public Set<Block> connectedLogs = new HashSet<>();
    public Set<Block> connectedLeaves = new HashSet<>();
    // Mini-Game
    public TreeZone zone;
    public int zonesHit, zonesTotal, timesHit;
    // Performance Monitoring
    public int calls;
    public long created = System.currentTimeMillis();
    public long completed = -1;
    public long Result() { return completed - created; }
    public Tree(Block initialBlock) {
        this.initialBlock = initialBlock;
    }
    public void addLog(Block block) {
        connectedLogs.add(block);
        if(bottomTrunk == null || block.getLocation().getY() < bottomTrunk.getLocation().getY()) bottomTrunk = block;
        if(topTrunk == null || block.getLocation().getY() > topTrunk.getLocation().getY()) topTrunk = block;
        if(trunkType == null) trunkType = block.getType().name().toLowerCase().replace("_log", "");
        completed = System.currentTimeMillis();
    }
    public void addLeaf(Block block) {
        connectedLeaves.add(block);
        if(bottomLeaf == null || block.getLocation().getY() < bottomLeaf.getLocation().getY()) bottomLeaf = block;
        if(topLeaf == null || block.getLocation().getY() > topTrunk.getLocation().getY()) topLeaf = block;
        if(leafType == null) leafType = block.getType().name().toLowerCase().replace("_leaves", "");
        completed = System.currentTimeMillis();
    }
    public int getTrunkHeight() { return topTrunk.getY() - bottomTrunk.getY(); }
    public int getHeight() { return topLeaf.getY() - bottomTrunk.getY(); }
    public int logCount() { return connectedLogs.size(); }
    public int leafCount() { return connectedLeaves.size(); }
    public String getTreeType() { return trunkType; }
    public String getLeafType() { return leafType; }
    public Material getTrunkType() { return getBottomTrunk().getType(); }
    public Block getBottomTrunk() { return bottomTrunk; }
    public Block getTopTrunk() { return topTrunk; }
    public Block getBottomLeaf() { return bottomLeaf; }
    public Block getTopLeaf() { return topLeaf; }
    public Block getInitialBlock() { return initialBlock; }
    public Block getGround() { return bottomTrunk.getRelative(BlockFace.DOWN); }
    public void startGame(Player player, @Nullable BlockFace face) {
        if(zone == null) zone = new TreeZone(initialBlock.getLocation(), face);
        if(ActivityManager.isActive(player)) ActivityManager.getTree(player).zone.endSlime();
        ActivityManager.setActive(player, this);
        randomizeZone(face);
        displayZone(player);
        CallEvent(new TreeZoneStartEvent(this, zone, player));
    }
    public void stopGame() { zone.endSlime(); }
    public void hitZone(Player player) { zonesHit++; timesHit++; zone.hit(this, player); randomizeZone(); }
    public void missedZone(Player player) { timesHit++; CallEvent(new TreeZoneMissEvent(this, zone, player)); }
    public void randomizeZone() { randomizeZone(null); }
    public void randomizeZone(@Nullable BlockFace face) { zone.randomizeLocation(face); zone.spawnSlime(); zone.updateExpiration(); zonesTotal++; }
    public void displayZone(Player player) {
        //displaySlime(player);
        displayParticle(player);
    }
    //public void displaySlime(Player player) { }
    public void displayParticle(Player player) {
        zone.display(player);
    }

    public boolean addBlock(Block block) {
        if(connectedLogs.contains(block) || connectedLeaves.contains(block)) return false;
        if(isLogBlock(block)) { addLog(block); return true; }
        else if(isLeafBlock(block)) { addLeaf(block); return true; }
        return false;
    }
}
