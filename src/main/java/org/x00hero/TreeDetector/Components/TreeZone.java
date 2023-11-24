package org.x00hero.TreeDetector.Components;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.util.Consumer;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneHitEvent;

import java.util.Random;

import static org.x00hero.TreeDetector.Config.*;
import static org.x00hero.TreeDetector.Main.CallEvent;

public class TreeZone {
    //Particle.DustTransition dustOptions = new Particle.DustTransition(Color.fromRGB(255, 127, 127), Color.fromRGB(255, 255, 255), 1.0F);
    public final Location initialLocation;
    public Location particleLocation;
    private Slime slime = null; // hitZone & detection
    public final long timeCreated;
    public long expirationTime;
    public long timeHit;

    public TreeZone(Location initialLocation) {
        this.initialLocation = initialLocation;
        timeCreated = System.currentTimeMillis();
        randomizeLocation();
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
    public Location randomizeLocation() { /*log("Randomizing");*/ particleLocation = initialLocation.clone(); particleLocation.add(randomize(-0.5f, 0.5f), randomize(-.9f, 1.5f), .1f); return particleLocation; }
    public float randomizeVertical() { return randomize(0, 1.5f); }
    public float randomizeHorizontal() { return randomize(-0.5f, 0.5f); }
    private final Random random = new Random();
    public float randomize(float min, float max) { return min + random.nextFloat() * (max - min); }
    public void display(Player player) { player.spawnParticle(particle, particleLocation, particleAmount, offsetX, offsetY, offsetZ, speed, dustOptions); /*log("Spawned particles @ " + particleLocation);*/ }
}
