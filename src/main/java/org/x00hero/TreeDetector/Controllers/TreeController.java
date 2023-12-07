package org.x00hero.TreeDetector.Controllers;

import org.x00hero.TreeDetector.Trees.Types.Tree;

import java.util.HashMap;
import java.util.UUID;

public class TreeController {
    private static HashMap<UUID, Tree> ActiveTrees = new HashMap<>();
    public static UUID reigsterTree(Tree tree) {
        UUID id = UUID.randomUUID();
        registerTree(id, tree);
        return id;
    }

    public static void registerTree(UUID uuid, Tree tree) { ActiveTrees.put(uuid, tree); }
    public static void unregisterTree(String treeID) { unregisterTree(UUID.fromString(treeID)); }
    public static void unregisterTree(UUID treeID) { ActiveTrees.remove(treeID); }
}
