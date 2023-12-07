package org.x00hero.TreeDetector.Controllers;

import org.bukkit.Location;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.x00hero.TreeDetector.Main;
import org.x00hero.TreeDetector.Trees.TreeDetection;
import org.x00hero.TreeDetector.Trees.Types.Tree;

public class CommandController implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        World world = Bukkit.getServer().getWorlds().get(0);
        if(cmd.getName().equalsIgnoreCase("treedetector")) {
            if(args.length == 0) return false;
            if(args.length >= 3) {
                if(sender instanceof Player player) world = player.getWorld();
                if(args.length == 4) world = Bukkit.getWorld(args[3]);
                int x = Integer.parseInt(args[0]);
                int y = Integer.parseInt(args[1]);
                int z = Integer.parseInt(args[2]);
                Tree tree = TreeDetection.getTree(world.getBlockAt(new Location(world, x, y, z)));
                sender.sendMessage("Tree " + (tree != null ? "FOUND" : "==NULL") + " @ " + x + ", " + y + ", " + z + " in " + world);
            }
            switch(args[0]) {
                case "reload", "rl" -> { Main.Reload(); sender.sendMessage("Tree Detector reloaded."); }
            }
            return true;
        }
        return false;
    }
}