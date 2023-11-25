# TreeDetector
Detects if player hit a tree, that's about it.

# Code Usage
## Tree
A Tree is a new way of handling interactions with trees to an extent. Access a `List<Block> connectedLogs` & `List<Block> connectedLeaves` being able to modify trees much easier than before.
```java
public class Tree {
    public final Block initialBlock;
    public BlockFace initialFace;
    private String trunkType;
    private String leafType;
    private Block bottomTrunk, topTrunk, topLeaf, bottomLeaf;
    public Set<Block> connectedLogs = new HashSet<>();
    public Set<Block> connectedLeaves = new HashSet<>();
    // Mini-Game
    public TreeZone zone;
    public int zonesHit, zonesTotal, timesHit;
    // Performance Monitoring
    public int calls;
    public long created = System.currentTimeMillis();
    public long completed = -1;
}
```
They also contain a little Mini-Game and some performance monitoring stuff, see if I'm hogging up all your resources.

To get a tree it's as simple `Tree tree = TreeDetection.getTree(Block);` it will return `null` if the block is not a Log *or* Leaf block **OR** does not meet the criteria. 
## Events
        
### TreeHitEvent
This will be called whenever a player hits a tree that meets the **_leaves_** & **_log_** **Threshold** specified in the Config
```java
public class EventHandler() implements Listener {
    @EventHandler
    public onTreeHit(TreeHitEvent e) {
        Tree tree = e.getTree();
        Player player = e.getPlayer();
    }
}
```
### TreeZoneHitEvent

# Config
I feel as if the base variables should be fine enough but if you have any custom tree plugins/mods that may break functionality partially or entirely. If trees are enlarged tweak the maxSearch values as needed.
```yaml
Detection: # Raising the # for any of the maxSearch can improve detection at the cost of some performance.
  maxSearchCalls: 250 # total calls allowed per Tree detection, the more, the better but not always the answer
  maxSearchWidth: 5 # width radius for search
  maxSearchHeight: 20 # height radius for search
  leavesThreshold: 5 # required leaves required for a valid Tree
  logThreshold: 4 # required logs required for a valid Tree
  materialConsistency: false # if the leaves should match the log type
Zone:
  size: 1
  invisible: true
  entity: SLIME
  colliderName: "TREE-COLLIDER"
  sound: # Audio can be any sound in Minecraft's library
    audio: UI_LOOM_TAKE_RESULT
    pitch: 0.7
    volume: 0.2
  constraint:
    x: .5
    y: .5
    faceOffset: 0 # offset from the front of block.
  Particles: # updateRate means every X ticks update
    amount: 2
    updateRate: 5
    offsetX: 0
    offsetY: 0.25
    offsetZ: 0
    speed: 0.1
    size: 1
    type: REDSTONE
    options: # Required if particle has optional parameters ex redstone
      R: 255
      G: 127
      B: 127
```