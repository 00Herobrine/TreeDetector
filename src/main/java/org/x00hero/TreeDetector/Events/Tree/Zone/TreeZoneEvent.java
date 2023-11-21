package org.x00hero.TreeDetector.Events.Tree.Zone;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.x00hero.TreeDetector.Components.Tree;
import org.x00hero.TreeDetector.Components.TreeZone;

import java.util.Locale;

public class TreeZoneEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private final Tree tree;
    private final TreeZone zone;
    private final Player player;
    public TreeZoneEvent(Tree tree, TreeZone zone, Player player) {
        this.tree = tree;
        this.zone = zone;
        this.player = player;
    }

    public Tree getTree() { return tree; }
    public TreeZone getZone() { return zone; }
    public Player getPlayer() { return player; }

    public long getTimeStarted() { return tree.zone.timeCreated; }
    public int getTotalZones() { return tree.zonesTotal; }
    public int getZonesHit() { return tree.zonesHit; }
    public int getTimesHit() { return tree.timesHit; }
    public int getTimesMissed() { return getTimesHit() - getZonesHit(); }
    public double getHitPercent() { return Double.parseDouble(String.format(Locale.US, "%.2f", ((double) getZonesHit() / getTimesHit()) * 100)); }

    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() { return handlerList; }
}
