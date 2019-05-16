package skywolf46.EvolvingTools.Extension.DefaultExtenson;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import skywolf46.EvolvingTools.Data.ItemEvolvingData;
import skywolf46.EvolvingTools.Extension.AttributeExtension;

public class TestExtension extends AttributeExtension {
    static {
        new TestExtension().registerExtension("TestExtension");
    }

    @Override
    public AttributeExtension createExtension(ConfigurationSection cs) {
        return new TestExtension();
    }

    @Override
    public void levelUp(Player p, ItemStack lv, ItemEvolvingData tool) {
        p.sendMessage("Congratulation, your tool evolved.");
    }

    @Override
    public void listen(ItemStack tool) {

    }
}
