package org.x00hero.TreeDetector.Test;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneHitEvent;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneMissEvent;

public class TreeTest implements Listener {

    @EventHandler
    public void onZoneHit(TreeZoneHitEvent e) {
        e.getPlayer().sendMessage("Hit " + e.getZonesHit() + " / " + e.getTimesHit() + " (" + e.getHitPercent() + "%)");
    }

    @EventHandler
    public void onZoneMiss(TreeZoneMissEvent e) {
        e.getPlayer().sendMessage("Missed " + e.getTimesMissed() + "/" + e.getTimesHit() + " (" + e.getHitPercent() + "%)");
    }
}
