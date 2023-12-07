package org.x00hero.TreeDetector.Controllers;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.x00hero.TreeDetector.Trees.Types.Interactive.InteractiveTree;
import org.x00hero.TreeDetector.Trees.Types.Tree;
import org.x00hero.TreeDetector.Trees.Types.Interactive.TreeZone;
import org.x00hero.TreeDetector.Trees.Events.Zone.TreeZoneExpiredEvent;
import org.x00hero.TreeDetector.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.x00hero.TreeDetector.Config.expirationCheckRate;
import static org.x00hero.TreeDetector.Config.particleUpdateRate;
import static org.x00hero.TreeDetector.Main.CallEvent;

public class ActivityController {
    private static HashMap<UUID, InteractiveTree> activeTrees = new HashMap<>();
    public static List<Integer> schedulers = new ArrayList<>();

    public static void ExpireCheck() {
       int id = Bukkit.getScheduler().scheduleSyncRepeatingTask( Main.plugin, () -> {
            for(UUID uuid : activeTrees.keySet()) {
                InteractiveTree tree = activeTrees.get(uuid);
                if(tree.zone == null) continue;
                Player player = Bukkit.getPlayer(uuid);
                if(player == null || tree.collapsed) setInactive(uuid);
                else if(tree.zone.isExpired()) setExpired(player, tree, tree.zone);
            }
        }, 0L, expirationCheckRate);
        schedulers.add(id);
    }

    public static void ParticleCheck() {
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask( Main.plugin, () -> {
            for(UUID uuid : activeTrees.keySet()) {
                InteractiveTree tree = activeTrees.get(uuid);
                if(tree.zone == null) continue;
                Player player = Bukkit.getPlayer(uuid);
                tree.displayParticle(player);
                /*if(player == null || tree.collapsed) setInactive(uuid);
                else if(tree.zone.isExpired()) setExpired(player, tree, tree.zone);*/
            }
        }, 0L, particleUpdateRate);
        schedulers.add(id);
    }

    public static boolean isActive(Player player) { return isActive(player.getUniqueId()); }
    public static boolean isActive(UUID uuid) { return activeTrees.containsKey(uuid) && !activeTrees.get(uuid).zone.isExpired(); }

    public static InteractiveTree getTree(Player player) { return getTree(player.getUniqueId()); }
    public static InteractiveTree getTree(UUID uuid) { return activeTrees.get(uuid); }

    public static InteractiveTree StartGame(Player player, BlockFace blockFace, Tree tree) { InteractiveTree interactiveTree = new InteractiveTree(tree); interactiveTree.startGame(player, blockFace); return interactiveTree; }
    public static void setActive(Player player, InteractiveTree tree) { setActive(player.getUniqueId(), tree); }
    public static void setActive(UUID uuid, InteractiveTree result) { activeTrees.put(uuid, result); }
    public static void setInactive(Player player) { setInactive(player.getUniqueId()); }
    public static void setInactive(UUID uuid) { activeTrees.get(uuid).zone.endSlime(); activeTrees.remove(uuid); }
    public static void setExpired(Player player, InteractiveTree tree, TreeZone zone) { setInactive(player); CallEvent(new TreeZoneExpiredEvent(tree, zone, player)); }
}
