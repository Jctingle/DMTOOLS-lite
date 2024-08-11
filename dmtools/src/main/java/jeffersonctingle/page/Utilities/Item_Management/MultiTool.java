package jeffersonctingle.page.Utilities.Item_Management;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MultiTool {
  private ArrayList<Material> allItems = new ArrayList<Material>();
  private ArrayList<ItemStack> boxItems = new ArrayList<ItemStack>();

  public MultiTool() {
      allItems.add(this.mobMover().getType());
      boxItems.add(this.mobMover());
  }

  public ItemStack mobMover() {
      ItemStack pointerItem = new ItemStack(Material.TOTEM_OF_UNDYING);
      ItemMeta pointerMeta = pointerItem.getItemMeta();
      pointerMeta.setDisplayName("Mover");
      List<String> lore = new ArrayList<>();
      lore.add("5ETool: Mover");
      // NamespacedKey key = new NamespacedKey("5ETools", "tooltype");
      pointerMeta.setLore(lore);
      // pointerMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "mover");
      pointerItem.setItemMeta(pointerMeta);

      return pointerItem;
  }
 public void removeItemsWithLore(Inventory inventory, String loreToRemove) {
    // Iterate through all slots in the inventory
    for (int i = 0; i < inventory.getSize(); i++) {
        ItemStack item = inventory.getItem(i);

        // Check if the item is not null
        if (item != null) {
            ItemMeta meta = item.getItemMeta();

            // Check if the item meta is not null
            if (meta != null && meta.hasLore()) {
                // Get the lore and check if it contains the specified string
                List<String> lore = meta.getLore();
                if (lore != null && lore.contains(loreToRemove)) {
                    // Remove the item by setting the slot to null
                    inventory.setItem(i, null);
                }
            }
        }
    }
}
public void cleanse_mover(Inventory inventory) {
  // Iterate through all slots in the inventory
  for (int i = 0; i < inventory.getSize(); i++) {
      ItemStack item = inventory.getItem(i);

      // Check if the item is not null
      if (item != null) {
          ItemMeta meta = item.getItemMeta();

          // Check if the item meta is not null
          if (meta != null && meta.hasLore()) {
              // Get the lore and check if it contains the specified string
              List<String> lore = meta.getLore();
              if (lore != null && lore.contains("5ETool: Mover")) {
                  // Remove the item by setting the slot to null
                  inventory.setItem(i, mobMover());
              }
          }
      }
  }
}
  public ArrayList<ItemStack> returnAllItems() {
      return this.boxItems;
  }
}
