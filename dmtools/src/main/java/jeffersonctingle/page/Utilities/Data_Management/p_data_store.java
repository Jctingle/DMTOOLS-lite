package jeffersonctingle.page.Utilities.Data_Management;

import org.bukkit.entity.LivingEntity;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class p_data_store extends JavaPlugin {
    public static void storeValueInt(LivingEntity e, String label, int value){
        NamespacedKey key = new NamespacedKey("5etools", label);
        PersistentDataContainer container = e.getPersistentDataContainer();
        container.set(key, PersistentDataType.INTEGER, value);
    }
    public static void storeValueString(LivingEntity e, String label, String value){
        NamespacedKey key = new NamespacedKey("5etools", label);
        PersistentDataContainer container = e.getPersistentDataContainer();
        container.set(key, PersistentDataType.STRING, value);
    }
}
