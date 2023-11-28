package org.x00hero.TreeDetector.Trees;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.util.Consumer;
import org.x00hero.TreeDetector.Trees.Events.Zone.TreeZoneHitEvent;

import javax.annotation.Nullable;

import static org.x00hero.TreeDetector.Config.*;
import static org.x00hero.TreeDetector.Main.CallEvent;
import static org.x00hero.TreeDetector.Main.divideVector;
import static org.x00hero.TreeDetector.Trees.TreeFunctions.randomize;

public class TreeZone {
    public final Location initialLocation;
    public Location particleLocation;
    private Slime slime = null; // hitZone & detection
    public final long timeCreated;
    public long expirationTime;
    public long timeHit;
    public final BlockFace face;

    public TreeZone(Location initialLocation, BlockFace face) {
        this.initialLocation = initialLocation;
        timeCreated = System.currentTimeMillis();
        randomizeLocation(face);
        this.face = face;
    }

    public Slime getSlime() { return slime; }
    public void spawnSlime() {
        if(particleLocation.getWorld() == null) return;
        if(slime != null) endSlime();
        Class slimeClass = EntityType.SLIME.getEntityClass();
        if(slimeClass == null) return;
        Consumer<Slime> preSpawnFunction = slimer -> {
            slimer.setSize(zoneSize);
            slimer.setInvisible(invisible);
            slimer.setCustomName(colliderName);
            slimer.setCustomNameVisible(false);
            slimer.setSilent(true);
            slimer.setLootTable(null);
            slimer.setAI(false);
        };
        this.slime = particleLocation.getWorld().spawn(particleLocation, slimeClass, preSpawnFunction);
    }
    public void endSlime() {
        if(slime == null) return;
        slime.remove();
        slime = null;
    }
    public void hit(Tree tree, Player player) { timeHit = System.currentTimeMillis(); updateExpiration(); endSlime(); CallEvent(new TreeZoneHitEvent(tree,this, player)); }
    public void updateExpiration() { expirationTime = System.currentTimeMillis() + (zoneTimeout * 1000L); }
    public boolean isExpired() { return System.currentTimeMillis() >= expirationTime; }
    public Location getRandomLocation() { return getRandomLocation(initialLocation, new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST} ); }
    public Location getRandomLocation(BlockFace blockFace) { return getRandomLocation(initialLocation, new BlockFace[]{blockFace}); }
    public Location getRandomLocation(Location baseLocation, BlockFace[] directions) {
        Location randomLocation = baseLocation.clone();
        randomLocation.add(0.5, 0.5, 0.5); // centralize location
        BlockFace direction = directions[randomize(directions.length - 1)];
        randomLocation.add(divideVector(direction.getDirection(), 2));
        double x = randomize(-constraintX, constraintX);
        double y = randomize(-constraintY, constraintY);
        if(direction == BlockFace.EAST || direction == BlockFace.WEST) randomLocation.add(particleOffsetZ, y, x);
        else randomLocation.add(x, y, particleOffsetZ);
        return randomLocation;
    }
    public void randomizeLocation(@Nullable BlockFace face) { particleLocation = face == null ? getRandomLocation() : getRandomLocation(face); PlaySound(); }
    private void PlaySound() { particleLocation.getWorld().playSound(particleLocation, zoneSound, zoneSoundVolume, zoneSoundPitch); }
    public void display(Player player) { player.spawnParticle(particle, particleLocation.clone().add(particleOffsetX, particleOffsetY, particleOffsetZ), particleAmount, dustOptions); /*log("Spawned particles @ " + particleLocation);*/ }
}
