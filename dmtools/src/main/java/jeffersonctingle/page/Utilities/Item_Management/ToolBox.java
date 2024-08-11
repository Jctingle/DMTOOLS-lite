package jeffersonctingle.page.Utilities.Item_Management;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import jeffersonctingle.page.App;



public class ToolBox implements CommandExecutor, Listener {
    @SuppressWarnings("unused")
    private App app;
    MultiTool toolbox = new MultiTool();

    public ToolBox(App app) {
        this.app = app;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = ((Player) sender);
            Inventory box = toolKit(p);
            p.openInventory(box);
            return true;
        } else {
            System.out.println("Cannot execute this command on the command line");
            return false;
        }
    }

    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }

    public Inventory toolKit(Player p) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "Tool Box");
        for (ItemStack Tool : toolbox.returnAllItems()) {
            if (!containsTool(p, Tool)) {
                inv.addItem(Tool);
            }
        }
        return inv;
    }

    private boolean containsTool(Player player, ItemStack tool) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
                List<String> lore = item.getItemMeta().getLore();
                if (lore != null && lore.contains(tool.getItemMeta().getLore().get(0))) {
                    return true;
                }
            }
        }
        return false;
    }
}

