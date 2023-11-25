package org.x00hero.TreeDetector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Components.Tree;
import org.x00hero.TreeDetector.Components.TreeZone;
import org.x00hero.TreeDetector.Events.Tree.Zone.TreeZoneExpiredEvent;

import java.util.HashMap;
import java.util.UUID;

import static org.x00hero.TreeDetector.Config.updateRate;
import static org.x00hero.TreeDetector.Main.CallEvent;

public class ActivityManager {
    private static HashMap<UUID, Tree> activeTrees = new HashMap<>();
    public static int schedulerID;

    public static void ExpireCheck() {
        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask( Main.plugin, () -> {
            for(UUID uuid : activeTrees.keySet()) {
                Tree tree = activeTrees.get(uuid);
                if(tree.zone == null) continue;
                Player player = Bukkit.getPlayer(uuid);
                if(player == null) setInactive(uuid);
                else if(tree.zone.isExpired()) setExpired(player, tree, tree.zone);
                else tree.displayParticle(player);
            }
        }, 0L, updateRate);
    }

    public static boolean isActive(Player player) { return isActive(player.getUniqueId()); }
    public static boolean isActive(UUID uuid) { return activeTrees.containsKey(uuid) && !activeTrees.get(uuid).zone.isExpired(); }

    public static Tree getTree(Player player) { return getTree(player.getUniqueId()); }
    public static Tree getTree(UUID uuid) { return activeTrees.get(uuid); }

    public static void setActive(Player player, Tree tree) { setActive(player.getUniqueId(), tree); }
    public static void setActive(UUID uuid, Tree result) { activeTrees.put(uuid, result); }
    public static void setInactive(Player player) { setInactive(player.getUniqueId()); }
    public static void setInactive(UUID uuid) { activeTrees.get(uuid).zone.endSlime(); activeTrees.remove(uuid); }
    public static void setExpired(Player player, Tree tree, TreeZone zone) { setInactive(player); CallEvent(new TreeZoneExpiredEvent(tree, zone, player)); }
    //public static void setExpired(UUID uuid) {  }
}
