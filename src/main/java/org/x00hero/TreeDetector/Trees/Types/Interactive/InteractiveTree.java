package org.x00hero.TreeDetector.Trees.Types.Interactive;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Controllers.ActivityController;
import org.x00hero.TreeDetector.Trees.Events.Zone.TreeZoneMissEvent;
import org.x00hero.TreeDetector.Trees.Events.Zone.TreeZoneStartEvent;
import org.x00hero.TreeDetector.Trees.Types.Collapsable.CollapsableTree;
import org.x00hero.TreeDetector.Trees.Types.Tree;

import javax.annotation.Nullable;

import static org.x00hero.TreeDetector.Config.baseZones;
import static org.x00hero.TreeDetector.Config.zonesMod;
import static org.x00hero.TreeDetector.Main.CallEvent;

public class InteractiveTree extends CollapsableTree {
    // Mini-Game
    public TreeZone zone;
    public int zonesHit, zonesTotal, timesHit, requiredZones;

    public InteractiveTree(Tree tree) {
        super(tree);
    }

    public void hitZone(Player player) { zonesHit++; timesHit++; zone.hit(this, player); randomizeZone(); }
    public void missedZone(Player player) { timesHit++; CallEvent(new TreeZoneMissEvent(this, zone, player)); }
    public void randomizeZone() { randomizeZone(null); }
    public void randomizeZone(@Nullable BlockFace face) { if(collapsed) return; zone.randomizeLocation(face); zone.spawnSlime(); zone.updateExpiration(); zonesTotal++; }
    //public void displaySlime(Player player) { }
    public void displayZone(Player player) { /*displaySlime();*/ displayParticle(player); }
    public void displayParticle(Player player) { zone.display(player); }
    public void stopGame() { zone.endSlime(); }
    public void startGame(Player player, @Nullable BlockFace face) { startGame(player, face, baseZones + (getHeight() / zonesMod));}
    public void startGame(Player player, @Nullable BlockFace face, int requiredZones) {
        if(zone == null) zone = new TreeZone(initialBlock.getLocation(), face);
        if(ActivityController.isActive(player)) ActivityController.getTree(player).zone.endSlime();
        this.requiredZones = requiredZones;
        randomizeZone(face);
        displayZone(player);
        ActivityController.setActive(player, this);
        CallEvent(new TreeZoneStartEvent(this, zone, player));
    }

}
