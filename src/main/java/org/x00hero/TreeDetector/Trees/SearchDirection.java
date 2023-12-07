package org.x00hero.TreeDetector.Trees;

import org.bukkit.block.BlockFace;

public enum SearchDirection {
    NORTH(0, 0, -1),
    EAST(1, 0, 0),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH_UP(NORTH, UP),
    EAST_UP(EAST, UP),
    SOUTH_UP(SOUTH, UP),
    WEST_UP(WEST, UP),
    NORTH_EAST(NORTH, EAST),
    NORTH_WEST(NORTH, WEST),
    SOUTH_EAST(SOUTH, EAST),
    SOUTH_WEST(SOUTH, WEST),
    WEST_SOUTH_WEST(WEST, SOUTH_WEST),
    WEST_NORTH_WEST(WEST, NORTH_WEST),
    EAST_NORTH_EAST(EAST, NORTH_EAST),
    EAST_SOUTH_EAST(EAST, SOUTH_EAST);
    private final int modX;
    private final int modY;
    private final int modZ;
    SearchDirection(final int modX, final int modY, final int modZ) {
        this.modX = modX;
        this.modY = modY;
        this.modZ = modZ;
    }
    SearchDirection(BlockFace face) {
        this.modX = face.getModX();
        this.modY = face.getModY();
        this.modZ = face.getModZ();
    }
    SearchDirection(SearchDirection direction, SearchDirection direction2) {
        this.modX = direction.getModX() + direction2.getModX();
        this.modY = direction.getModY() + direction2.getModY();
        this.modZ = direction2.getModZ() + direction2.getModZ();
    }
    public int getModX() { return modX; }
    public int getModY() { return modY; }
    public int getModZ() { return modZ; }
}
