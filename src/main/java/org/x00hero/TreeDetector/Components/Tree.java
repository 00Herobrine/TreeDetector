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

import static org.x00hero.TreeDetector.Events.TreeDetection.isLogBlock;
import static org.x00hero.TreeDetector.Main.CallEvent;

public class Tree {
    public final Block initialBlock;
    public BlockFace initialFace;
    private Block bottomTrunk, topTrunk;
    public Set<Block> connectedLogs = new HashSet<>();
    public Set<Block> connectedLeaves = new HashSet<>();
    public TreeZone zone;
    public int zonesHit, zonesTotal, timesHit;

    public Tree(Block initialBlock, BlockFace face) {
        this.initialBlock = initialBlock;
        this.initialFace = face;
        if(isLogBlock(initialBlock)) addLog(initialBlock);
        zone = new TreeZone(initialBlock.getLocation());
        zone.updateExpiration();
    }
    public Tree(Block initialBlock) {
        this.initialBlock = initialBlock;
        if(isLogBlock(initialBlock)) addLog(initialBlock);
        zone = new TreeZone(initialBlock.getLocation());
        zone.updateExpiration();
    }
    public void addLog(Block block) {
        connectedLogs.add(block);
        if(bottomTrunk == null || block.getLocation().getY() < bottomTrunk.getLocation().getY()) bottomTrunk = block;
        if(topTrunk == null || block.getLocation().getY() > topTrunk.getLocation().getY()) topTrunk = block;
    }
    public void addLeaf(Block block) {
        connectedLeaves.add(block);
    }
    public int logCount() { return connectedLogs.size(); }
    public int leafCount() { return connectedLeaves.size(); }
    public Material getTrunkType() { return getBottomTrunk().getType(); }
    public Block getBottomTrunk() { return bottomTrunk; }
    public Block getTopTrunk() { return topTrunk; }
    public Block getInitialBlock() { return initialBlock; }
    public Block getGround() { return bottomTrunk.getRelative(BlockFace.DOWN); }
    public void startGame(Player player, @Nullable BlockFace face) {
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
    public void randomizeZone(@Nullable BlockFace face) { zone.randomizeLocation(face); zone.spawnSlime(); zonesTotal++; }
    public void displayZone(Player player) {
        //displaySlime(player);
        displayParticle(player);
    }
    //public void displaySlime(Player player) { }
    public void displayParticle(Player player) {
        zone.display(player);
    }
}
