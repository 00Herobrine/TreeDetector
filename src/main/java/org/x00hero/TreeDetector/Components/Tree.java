package org.x00hero.treedetector;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

import static org.x00hero.treedetector.Events.BlockBreak.isLogBlock;

public class TreeResult {
    public final Block initialBlock;
    public Block bottomTrunk, topTrunk;
    public Set<Block> connectedLogs = new HashSet<>();
    public Set<Block> connectedLeaves = new HashSet<>();
    public final long timeCreated;
    public long expirationTime;
    public TreeGame game;

    public TreeResult(Block initialBlock) {
        this.initialBlock = initialBlock;
        if(isLogBlock(initialBlock)) addLog(initialBlock);
        game = new TreeGame(initialBlock.getLocation());
        timeCreated = System.currentTimeMillis();
        updateExpiration();
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
    public Block getGround() { return bottomTrunk.getRelative(BlockFace.DOWN); }
    public void updateExpiration() { expirationTime = System.currentTimeMillis() + (60 * 1000); }
    public boolean isExpired() { return System.currentTimeMillis() >= expirationTime; }
    public boolean isSimilar(TreeResult result) { return bottomTrunk == result.bottomTrunk; }
    public void randomize() {
        game.randomizeLocation();
    }
    public void startGame(Player player) {
        if(ActivityManager.isActive(player)) ActivityManager.getTree(player).game.endSlime();
        ActivityManager.setActive(player, this);
        game.spawnSlime();
        displayParticle(player);
    }
    public void displayParticle(Player player) {
        game.display(player);
        updateExpiration();
    }
}
