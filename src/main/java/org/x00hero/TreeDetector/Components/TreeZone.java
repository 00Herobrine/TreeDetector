package org.x00hero.TreeDetector.Components;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneHitEvent;

import javax.annotation.Nullable;
import java.util.Random;

import static org.x00hero.TreeDetector.Config.*;
import static org.x00hero.TreeDetector.Main.CallEvent;
import static org.x00hero.TreeDetector.Main.log;

public class TreeZone {
    public final Location initialLocation;
    public Location particleLocation;
    private Slime slime = null; // hitZone & detection
    public final long timeCreated;
    public long expirationTime;
    public long timeHit;

    public TreeZone(Location initialLocation, BlockFace face) {
        this.initialLocation = initialLocation;
        timeCreated = System.currentTimeMillis();
        particleLocation = getRandomLocation(initialLocation, new BlockFace[]{face});
    }
    public TreeZone(Location initialLocation) {
        this.initialLocation = initialLocation;
        timeCreated = System.currentTimeMillis();
        particleLocation = getRandomLocation();
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
            //slimer.setInvulnerable(true);
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
    public void updateExpiration() { expirationTime = System.currentTimeMillis() + (60 * 1000); }
    public boolean isExpired() { return System.currentTimeMillis() >= expirationTime; }
    public Location getRandomLocation() { return getRandomLocation(initialLocation, new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST} ); }
    public Location getRandomLocation(BlockFace blockFace) { return getRandomLocation(initialLocation, new BlockFace[]{blockFace}); }
    public Location getRandomLocation(Location baseLocation, BlockFace[] directions) {
        log("Initial: " + baseLocation);
        Location randomLocation = baseLocation.clone();
        randomLocation.add(0.5, 0.5, 0.5); // centralize location
        BlockFace direction = directions[randomize(directions.length - 1)];
        randomLocation.add(divideVector(direction.getDirection(), 2));
        log("Initial + Direction: " + direction + " | " + randomLocation);
        double x = randomize(constraintX);
        double y = randomize(constraintY);
        if(direction == BlockFace.EAST || direction == BlockFace.WEST) randomLocation.add(offsetZ, y, x);
        else randomLocation.add(x, y, offsetZ);
        log("Random: LEFT:" + x + " UP: " + y + " DIRECTION: " + direction + " | " + randomLocation);
        return randomLocation;
    }
    public Vector divideVector(Vector vector, double divisor) {
        double quotientX = vector.getX() / divisor;
        double quotientY = vector.getY() / divisor;
        double quotientZ = vector.getZ() / divisor;
        return new Vector(quotientX, quotientY, quotientZ);
    }
    public Location randomizeLocation(@Nullable BlockFace face) { particleLocation = face == null ? getRandomLocation() : getRandomLocation(face); PlaySound(); return particleLocation; }
    private final Random random = new Random();
    private void PlaySound() { particleLocation.getWorld().playSound(particleLocation, sound, soundVolume, soundPitch); }
    public int randomize(int max) { return (int) randomize(0, max); }
    public float randomize(float max) { return randomize(-max, max); }
    public float randomize(float min, float max) { return min + random.nextFloat() * (max - min); }
    public void display(Player player) { player.spawnParticle(particle, particleLocation.clone().add(offsetX, offsetY, offsetZ), particleAmount, dustOptions); /*log("Spawned particles @ " + particleLocation);*/ }
}
