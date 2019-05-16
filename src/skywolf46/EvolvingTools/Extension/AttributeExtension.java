package skywolf46.EvolvingTools.Extension;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import skywolf46.EvolvingTools.Data.ItemEvolvingData;

import java.util.HashMap;

public abstract class AttributeExtension {
    private static HashMap<String, AttributeExtension> attrExts = new HashMap<>();

    public abstract AttributeExtension createExtension(ConfigurationSection cs);

    public abstract void levelUp(Player p, ItemStack lv, ItemEvolvingData tool);

    public abstract void listen(ItemStack tool);

    public final void registerExtension(String name) {
        if (attrExts.containsKey(name))
            throw new IllegalStateException("Attribute of same name exists");
        attrExts.put(name, this);
    }

    public static AttributeExtension createNewExtension(String name,ConfigurationSection section){
        if(!attrExts.containsKey(name))
            return null;
        return attrExts.get(name).createExtension(section);
    }
}
