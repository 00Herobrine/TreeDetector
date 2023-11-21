package org.x00hero.TreeDetector.Events.Tree;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.x00hero.TreeDetector.Components.TreeZone;

public class TreeZoneHitEvent extends Event {
    public static HandlerList handlerList = new HandlerList();
    private final TreeZone zone;
    private final Player player;
    public TreeZoneHitEvent(TreeZone zone, Player player) {
        this.zone = zone;
        this.player = player;
        zone.hit();
    }

    public TreeZone getZone() { return zone; }
    public Player getPlayer() { return player; }

    @Override @NonNull
    public HandlerList getHandlers() { return handlerList; }
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
