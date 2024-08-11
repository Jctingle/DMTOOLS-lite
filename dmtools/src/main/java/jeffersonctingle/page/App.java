package jeffersonctingle.page;

import org.bukkit.plugin.java.JavaPlugin;

import jeffersonctingle.page.Utilities.Core;
import jeffersonctingle.page.Utilities.dmNotes;
import jeffersonctingle.page.Utilities.Item_Management.ToolBox;
import jeffersonctingle.page.Utilities.tokens.Equipmentmanager;
import jeffersonctingle.page.Utilities.tokens.Interact;
import jeffersonctingle.page.Utilities.tokens.Mobicon;

public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("DMTools-lite Loaded");

        dmNotes notery = new dmNotes(this);
        this.getCommand("dnote").setExecutor(notery);
        getServer().getPluginManager().registerEvents(notery, this);

        Mobicon mobi = new Mobicon(this);
        this.getCommand("mobi").setExecutor(mobi);
        getServer().getPluginManager().registerEvents(mobi, this); 

        Interact inti = new Interact(this);
        this.getCommand("interact").setExecutor(inti);
        getServer().getPluginManager().registerEvents(inti, this); 

        Equipmentmanager equip = new Equipmentmanager(this);
        this.getCommand("equip").setExecutor(equip);
        getServer().getPluginManager().registerEvents(equip, this); 

        ToolBox toolCore = new ToolBox(this);
        this.getCommand("tools").setExecutor(toolCore);
        getServer().getPluginManager().registerEvents(toolCore, this);

        Core systemCore = new Core(this);
        this.getCommand("game").setExecutor(systemCore);
        getServer().getPluginManager().registerEvents(systemCore, this); 

    }
    @Override
    public void onDisable() {
        getLogger().info("See you again, SpigotMC!");
    }
}