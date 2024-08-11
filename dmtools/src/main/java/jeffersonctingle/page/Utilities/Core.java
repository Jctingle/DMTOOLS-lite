package jeffersonctingle.page.Utilities;

// import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.meta.ItemMeta;

import jeffersonctingle.page.App;
import jeffersonctingle.page.Utilities.Item_Management.MultiTool;
import jeffersonctingle.page.Utilities.tokens.MobMoverJCT;



public class Core implements CommandExecutor,Listener {   
    @SuppressWarnings("unused")
    private App app;
    public Core(App app){
        this.app = app;
    }
    @SuppressWarnings("unused")
    private MultiTool toolBox = new MultiTool();
    // GENERALIZED CORE, HANDLES EVENT FIRING FOR SAFE EVENTS WITHOUT REPEATING LOGIC
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        //update tool in inventory if possible
    }
    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent e) {
        //deregister from any plugin roster players
        MobMoverJCT.deRegister(e.getPlayer());
        //save cache of the player object which will also contain their spell grimoire.
        //spellcasting will be cleaned up
    }
    //event handler for death of a mob that is attached via mobmover
    public boolean check_hand_for_system_tool(ItemMeta im){
        if(im != null && im.hasLore() == true && im.getLore().get(0).contains("5ETool:")) {
        return true;
                }
        else{
            return false;
        }
    }
    // @EventHandler
    // public void onMobDeletion(EntityRemoveEvent e){
    //     if(e.getEntity == )
    // }
    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent e) {
                if(e.getHand() == EquipmentSlot.HAND && check_hand_for_system_tool(e.getPlayer().getInventory().getItemInMainHand().getItemMeta())){
                    e.setCancelled(true);
                    switch(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                    default:
                        break;
                    }
                }
    }
    @EventHandler
    public void onFKey(PlayerSwapHandItemsEvent e) {
                if (check_hand_for_system_tool(e.getPlayer().getInventory().getItemInMainHand().getItemMeta())){
                    e.setCancelled(true);
                    switch(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                    case "Mover":
                        MobMoverJCT.RTXEntitySelect(e.getPlayer());
                        break;
                    default:
                        break;
                    }
                }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        //This bundle of if statements is to protect bad situations from interacting with each other
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(e.getHand() == EquipmentSlot.HAND && check_hand_for_system_tool(e.getPlayer().getInventory().getItemInMainHand().getItemMeta())) {
                    //code here
                    e.setCancelled(true);
                    switch(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                        case "Mover":
                            MobMoverJCT.fireMovement(e.getPlayer());
                            break;
                    }
                }
             }
            else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(e.getHand() == EquipmentSlot.HAND && check_hand_for_system_tool(e.getPlayer().getInventory().getItemInMainHand().getItemMeta())) {
                    //code here
                    e.setCancelled(true);
                    switch(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                        case "Mover":
                            MobMoverJCT.menu(e.getPlayer());
                            break;
                    }

                }
            }
        }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        InventoryView view = e.getView();
        if (!view.getTitle().equals("Mover Remote")) return;
        else{
            Integer buttonClicked = e.getRawSlot();
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(e.getWhoClicked().getName());
            // p.sendMessage("click registered");
            // p.sendMessage(buttonClicked.toString());\
            //this should become a whole public class at some point, just a switchboard <- beautiful pun
            switch (buttonClicked){
                case 0:
                    MobMoverJCT.returnMovingMob(p).moveTo();
                    break;
                case 1:
                    MobMoverJCT.returnMovingMob(p).faceMe(p);
                    break;
                case 2:
                    MobMoverJCT.returnMovingMob(p).faceAway(p);
                    break;
                case 3:
                    MobMoverJCT.returnMovingMob(p).moveToB();
                    break;

                case 4:
                if (MobMoverJCT.returnMovingMob(p).isCursor()){
                    MobMoverJCT.returnMovingMob(p).cursorBool();
                    MobMoverJCT.menu(p);
                    break;

                }
                else{
                    MobMoverJCT.returnMovingMob(p).cursorBool();
                    MobMoverJCT.menu(p);
                    break;
                }
                case 5:
                    MobMoverJCT.returnMovingMob(p).swingArm();
                    break;
                case 6:
                        MobMoverJCT.returnMovingMob(p).sizeUp();
                    break;
                case 7:
                    MobMoverJCT.returnMovingMob(p).sizeDown();
                    break;
                case 8:
                    MobMoverJCT.returnMovingMob(p).toggleGrowth();
                    break;
                case 9:
                    MobMoverJCT.returnMovingMob(p).toggleInvis();
                    break;
                case 10:
                    MobMoverJCT.returnMovingMob(p).toggleDogAngry();
                    break;
                case 11:
                    MobMoverJCT.returnMovingMob(p).enableDuplication();
                    break;
                default:
                    p.sendMessage("Don't even worry, nothing dangerous happened :^)");
            }
        }
    }
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        if (!e.getView().getTitle().equals("Mover Remote")) return;
        e.setCancelled(true);
    }
}

