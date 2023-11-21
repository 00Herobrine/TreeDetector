package org.x00hero.TreeDetector.Components;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import java.util.Random;

import static org.x00hero.TreeDetector.Main.log;

public class TreeGame {
    private float verticalVariance, horizontalVariance;
    private Particle particle = Particle.REDSTONE;
    Particle.DustTransition dustOptions = new Particle.DustTransition(Color.fromRGB(255, 127, 127), Color.fromRGB(255, 255, 255), 1.0F);
    int particleCount = 2;
    double offsetX = 0;
    double offsetY = .2f;
    double offsetZ = 0;
    double speed = 0.1;
    public final Location initialLocation;
    public Location particleLocation;
    private Slime slime = null; // hitZone & detection

    public TreeGame(Location initialLocation) {
        this.initialLocation = initialLocation;
        randomizeLocation();
    }

    public void spawnSlime() {
        if(particleLocation.getWorld() == null) return;
        if(slime != null) slime.damage(10000);
        slime = (Slime) particleLocation.getWorld().spawnEntity(particleLocation, EntityType.SLIME);
        slime.setInvisible(true);
        slime.setCustomName("TREE-COLLIDER");
        slime.setCustomNameVisible(false);
        slime.setInvulnerable(true);
        slime.setSilent(true);
        slime.setLootTable(null);
        slime.setAI(false);
        slime.setSize(0);
    }
    public void endSlime() {
        slime.setLootTable(null);
        slime.remove();
        slime = null;
    }
    public Location randomizeLocation() { log("Randomizing"); particleLocation = initialLocation; particleLocation.add(randomize(-0.5f, 0.5f), randomize(-.9f, 1.5f), .1f); return particleLocation; }
    public void randomizeVertical() { verticalVariance = randomize(0, 1.5f); }
    public void randomizeHorizontal() { horizontalVariance = randomize(0, 0.5f); }
    private final Random random = new Random();
    public float randomize(float min, float max) { return min + random.nextFloat() * (max - min); }
    public void display(Player player) { player.spawnParticle(particle, particleLocation, particleCount, offsetX, offsetY, offsetZ, speed, dustOptions); log("Spawned particles @ " + particleLocation); }

}
