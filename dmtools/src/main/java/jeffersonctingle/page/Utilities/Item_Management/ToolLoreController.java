package jeffersonctingle.page.Utilities.Item_Management;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

import jeffersonctingle.page.Utilities.Data_Management.p_data_retrieval;
//will need to import moveable
public class ToolLoreController{
    //cleanse function
    public List<String> loreTag = new ArrayList<String>(){{add("5ETool: Mover");}};
    //mob selection function
    public static void update_mover_lore_in_inventory(Player player, LivingEntity mobForStats) {
        List<String> newLore = add_mob_stats_to_lore(mobForStats);
        ItemStack item_to_update = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = item_to_update.getItemMeta();
        if (meta != null) {
            meta.setLore(newLore);
            meta.setDisplayName("Mover");
            item_to_update.setItemMeta(meta);
        }
        update_mover_item(player, item_to_update);
        // Update the item in the player's inventory
        
    }
    public static List<String> add_mob_stats_to_lore(LivingEntity mob_for_stats){
        ArrayList<String> populated_lore = new ArrayList<>();
        populated_lore.add("5ETool: Mover");
        populated_lore.add(ChatColor.GOLD + "Name of Selected Mob: " + ChatColor.WHITE + mob_for_stats.getName());
        String tempAC = "";
        populated_lore.add(ChatColor.GOLD + "Current Health: " + ChatColor.WHITE + mob_for_stats.getHealth());
        tempAC = Integer.toString(p_data_retrieval.get_int_value_by_key(mob_for_stats, "ac"));
        populated_lore.add(ChatColor.GOLD + "Current AC: " + ChatColor.WHITE + tempAC);
        populated_lore.add(ChatColor.GOLD + "Current TempHP: " + ChatColor.WHITE + mob_for_stats.getAbsorptionAmount());
        return populated_lore;
    }
    public static void update_mover_item(Player player, ItemStack item_to_update){
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            // Check if the item is not null
            if (item != null) {
                ItemMeta metadata = item.getItemMeta();
                // Check if the item meta is not null
                if (metadata != null && metadata.hasLore()) {
                    // Get the lore and check if it contains the specified string
                    List<String> lore = item_to_update.getItemMeta().getLore();
                    
                    if (lore != null && lore.contains("5ETool: Mover")) {
                        // Remove the item by setting the slot to null
                        inv.setItem(i, item_to_update);
                    }
                }
            }
        }
    }
}
