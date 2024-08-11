package jeffersonctingle.page.Utilities.Data_Management;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class p_data_retrieval extends JavaPlugin{
    public static int get_int_value_by_key(LivingEntity entity, String key_string) {
        NamespacedKey key = new NamespacedKey("5etools", key_string);
        PersistentDataContainer container = entity.getPersistentDataContainer();
        return container.getOrDefault(key, PersistentDataType.INTEGER, -1); // Default value is -1 if key is not found
    }
    public static String get_string_value_by_key(LivingEntity entity, String key_string) {
        NamespacedKey key = new NamespacedKey("5etools", key_string);
        PersistentDataContainer container = entity.getPersistentDataContainer();
        return container.get(key, PersistentDataType.STRING); // Default value is -1 if key is not found
    }
}
