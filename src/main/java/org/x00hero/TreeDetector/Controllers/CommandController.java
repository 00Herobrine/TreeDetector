package org.x00hero.TreeDetector.Controllers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.x00hero.TreeDetector.Main;

public class CommandController implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.equals(Bukkit.getConsoleSender())) return false;
        Player player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("treedetector")) {
            if(args.length == 0) return true;
            switch(args[0]) {
                case "reload", "rl" -> Main.Reload();
            }
            return true;
        }
        return false;
    }
}