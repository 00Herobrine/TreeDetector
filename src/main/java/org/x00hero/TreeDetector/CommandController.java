package org.x00hero.TreeDetector;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandController implements CommandExecutor, Listener {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.equals(Bukkit.getConsoleSender())) return false;
        Player player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("treedetector")) {
            if(args.length == 0) return true;
            switch(args[0]) {
                case "reload" -> Main.Reload();
            }
            return true;
        }
        return false;
    }
}