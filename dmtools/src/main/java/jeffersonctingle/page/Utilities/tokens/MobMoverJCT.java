package jeffersonctingle.page.Utilities.tokens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import jeffersonctingle.page.App;
import jeffersonctingle.page.Utilities.Item_Management.MultiTool;
import jeffersonctingle.page.Utilities.Item_Management.ToolLoreController;

public class MobMoverJCT{
    @SuppressWarnings("unused")
    private App app;
    public MobMoverJCT(App app){
        this.app = app;
    }
    static MultiTool toolBox = new MultiTool();
    static Map<Player, Moveable> movingMob = new HashMap<Player, Moveable>();
    public static void deRegister(Player player){
        movingMob.remove(player);
        toolBox.cleanse_mover(player.getInventory());
        //do item lore scrubbing
    }
    public static void deregister_by_key(LivingEntity e){
        for(Entry<Player, Moveable> entry: movingMob.entrySet()) {

            // if give value is equal to value from entry
            // print the corresponding key
            if(entry.getValue().movingMobReturn() == e) {
              movingMob.remove(entry.getKey());
              toolBox.cleanse_mover(entry.getKey().getInventory());
              //do item lore scrubbing
            }
          }
    }
    public static void RTXEntitySelect(Player p) {
        //need to add logic to prevent multiple players from selecting the same token
        Location eyeLoc = p.getEyeLocation();
        World world = p.getWorld();
        Double maxDistance = 100.0;
        RayTraceResult rtxResult = world.rayTraceEntities(eyeLoc, eyeLoc.getDirection(), maxDistance, 0.1, (entity) -> (entity.getScoreboardTags().contains("token")));
        // 
        // TextDisplay clickedText = (TextDisplay) rtxResult.getHitEntity();
        if(rtxResult != null){
                LivingEntity clickedMob = (LivingEntity) rtxResult.getHitEntity();
                Moveable mover = new Moveable(clickedMob, clickedMob.getLocation(), false);
                movingMob.put(p, mover);
                p.sendMessage("[" + ChatColor.GOLD + "Mover" + ChatColor.WHITE + "]: " + clickedMob.getName() + " Selected, you can now right click a destination, or shift left click empty air to open the menu");
                ToolLoreController.update_mover_lore_in_inventory(p, clickedMob);
            }
        else{
            String mobName = (movingMob.containsKey(p) && movingMob.get(p).movingMobReturn() != null) ? 
                movingMob.get(p).movingMobReturn().getName() : "No Token";
            p.sendMessage("[" + ChatColor.GOLD + "Mover" + ChatColor.WHITE + "]: " + "Mover Failed, currently you have " + mobName + " selected");
        }
    }
    public static Moveable returnMovingMob(Player p){
        Moveable returner = movingMob.get(p);
        return returner;
    }
    public static void fireMovement(Player e1) {
            Player p = e1;
            if(movingMob.containsKey(p)) {
                    //mode dependant
                    if(movingMob.get(p).isCloning() == true){
                        Location eyeLoc = p.getEyeLocation();
                        World world = p.getWorld();
                        RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 35, FluidCollisionMode.NEVER, true);
                        if (rtxResult != null){
                            Location destination = rtxResult.getHitPosition().toLocation(world);
                            Vector targetDirection = p.getLocation().getDirection();
                            destination.setDirection(targetDirection);
                            destination.setPitch(0);
                            movingMob.get(p).movingMobReturn().copy(destination);
                        }
                    }
                    else if (movingMob.get(p).isCursor() == true){
                        Location eyeLoc = p.getEyeLocation();
                        World world = p.getWorld();
                        RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 3, FluidCollisionMode.NEVER, true);
                        if (rtxResult != null){
                            movingMob.get(p).movingMobReturn().teleport(rtxResult.getHitPosition().toLocation(world));
                        }
                        else{
                            Location rangedDistance = eyeLoc.add(eyeLoc.getDirection().multiply(3));
                            movingMob.get(p).movingMobReturn().teleport(rangedDistance);
                        }
                    }
                    else{
                        Location eyeLoc = p.getEyeLocation();
                        World world = p.getWorld();
                        RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 35, FluidCollisionMode.NEVER, true);
                        if (rtxResult != null){
                            Location destination = rtxResult.getHitPosition().toLocation(world);
                            Vector targetDirection = p.getLocation().getDirection();
                            destination.setDirection(targetDirection);
                            destination.setPitch(0);
                            movingMob.get(p).movingMobReturn().teleport(destination);
                        }
                    }

            }
        }
    //this can be moved to a new file called MoverMenu or something
    public static void menu(Player p){
            if(movingMob.containsKey(p)) {
                if(p.isSneaking()){
                    p.openInventory(remoteGui(p));
                }
                else if(p.getOpenInventory().getTitle().equals("Mover Remote")){
                    p.openInventory(remoteGui(p));
                }
            }
    }
    private static ItemStack createButtonItem(Material material, String displayName, ArrayList<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        if (lore != null && !lore.isEmpty()) {
            itemMeta.setLore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    public static Inventory remoteGui(Player invoker) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.BARREL, "Mover Remote");
    
        // Create buttons
        inv.setItem(0, createButtonItem(Material.LIME_CONCRETE, "Move one block up", new ArrayList<>()));
        inv.setItem(1, createButtonItem(Material.COMPASS, "Change Rotation +", new ArrayList<>(Collections.singletonList("Rotates the mob to face the player"))));
        inv.setItem(2, createButtonItem(Material.SPYGLASS, "Change Rotation -", new ArrayList<>(Collections.singletonList("Rotates the mob to face away from the player"))));
        inv.setItem(3, createButtonItem(Material.RED_CONCRETE, "Move one block down", new ArrayList<>()));
    
        // Toggle Target Mode button (button 5)
        Material targetModeMaterial = movingMob.get(invoker).isCursor() ? Material.CHORUS_FLOWER : Material.CHORUS_FRUIT;
        String targetModeTitle = movingMob.get(invoker).isCursor() ? "Short Range" : "Long Range";
        ArrayList<String> targetModeLore = new ArrayList<>();
        targetModeLore.add("Current: " + targetModeTitle);
        
        // Set Material.CHORUS_FRUIT as the default material if isCursor() is false
        if (!movingMob.get(invoker).isCursor()) {
            targetModeMaterial = Material.CHORUS_FRUIT;
        }
        inv.setItem(4, createButtonItem(targetModeMaterial, "Toggle Target Mode", targetModeLore));
    
        // Remaining buttons
        inv.setItem(5, createButtonItem(Material.IRON_SWORD, "Swing Main Arm", new ArrayList<>()));
    
        if (movingMob.get(invoker).isSlime()) {
            inv.setItem(6, createButtonItem(Material.LEATHER, "Increase size by 1", new ArrayList<>()));
            inv.setItem(7, createButtonItem(Material.RABBIT_HIDE, "Decrease size by 1", new ArrayList<>()));
        }
    
        if (movingMob.get(invoker).isAgeable()) {
            inv.setItem(8, createButtonItem(Material.EGG, "Toggle Age State", new ArrayList<>()));
        }
    
        inv.setItem(9, createButtonItem(Material.GLASS, "Toggle Visibility", new ArrayList<>()));
    
        if (movingMob.get(invoker).isDog()) {
            inv.setItem(10, createButtonItem(Material.REDSTONE_BLOCK, "Toggle Angry", new ArrayList<>()));
        }
        
        inv.setItem(11, createButtonItem(Material.BRUSH, 
         (movingMob.get(invoker).isCloning() ? "Disable" : "Enable") + " Duplication", 
         new ArrayList<>()));

        return inv;
    }
    



}
