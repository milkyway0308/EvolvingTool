package skywolf46.EvolvingTools.Data;

import com.sun.xml.internal.ws.api.message.AttachmentEx;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import skywolf46.EvolvingTools.Extension.AttributeExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvolvingData {
    private HashMap<String, Long> levels = new HashMap<>();
    private HashMap<String, List<AttributeExtension>> extentions = new HashMap<>();
    private List<String> events = new ArrayList<>();
    private HashMap<String, List<String>> msg = new HashMap<>();

    public EvolvingData(ConfigurationSection cs) {
        ConfigurationSection section = cs.getConfigurationSection("Levels");
        for (String n : section.getKeys(false)) {
            ConfigurationSection sec = section.getConfigurationSection(n);

            try {
                levels.put(n, Long.parseLong(sec.getString("Exp")));
            } catch (Exception ex) {
                levels.put(n, 0L);
                ex.printStackTrace();
            }
            if (sec.isConfigurationSection("Attribute")) {
                ConfigurationSection attr = sec.getConfigurationSection("Attribute");
                List<AttributeExtension> atExt = new ArrayList<>();
                for (String n4 : attr.getKeys(false)){
                    if (attr.isConfigurationSection(n4)) {
                        AttributeExtension ex = AttributeExtension.createNewExtension(n4, attr.getConfigurationSection(n4));
                        if (ex != null)
                            atExt.add(ex);
                    }
                }
                extentions.put(n, atExt);
            }
        }
        if (cs.isConfigurationSection("Settings")) {
            section = cs.getConfigurationSection("Settings");
            if (section.isList("Events"))
                events.addAll(section.getStringList("Events"));
            if (section.isConfigurationSection("Messages")) {
                ConfigurationSection msg = section.getConfigurationSection("Messages");
                for (String n : msg.getKeys(false)) {
                    if (!msg.isList(n))
                        continue;
                    List<String> list = new ArrayList<>();
                    msg.getStringList(n)
                            .forEach((str) -> {
                                list.add(ChatColor.translateAlternateColorCodes('&', str));
                            });
                    this.msg.put(n, list);
                }
            }
        }
    }

    public long getMaxExp(int lv) {
        return levels.getOrDefault("Level " + lv, -1L);
    }

    public boolean isListeningEvent(String ev) {
        return this.events.contains(ev);
    }

    public void loopExtension(Player p,ItemStack item, ItemEvolvingData itemEvolvingData) {
        extentions.getOrDefault("Level " + itemEvolvingData.getLevel(), new ArrayList<>())
                .forEach((ext) -> {
                    ext.levelUp(p,item, itemEvolvingData);
                });
    }

    public List<AttributeExtension> getExtensions(int lv) {
        return extentions.getOrDefault("Level " + lv, new ArrayList<>());
    }

    public List<String> getMessage(String msg) {
        return this.msg.get(msg);
    }
}
