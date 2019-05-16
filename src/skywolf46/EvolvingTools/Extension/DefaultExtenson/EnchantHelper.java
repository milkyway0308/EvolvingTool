package skywolf46.EvolvingTools.Extension.DefaultExtenson;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skywolf46.EvolvingTools.Data.ItemEvolvingData;
import skywolf46.EvolvingTools.Extension.AttributeExtension;

import java.util.HashMap;
import java.util.Map;

public class EnchantHelper extends AttributeExtension {
    private HashMap<Enchantment,Integer> data = new HashMap<>();
    private HashMap<Enchantment,Integer> set = new HashMap<>();

    @Override
    public AttributeExtension createExtension(ConfigurationSection cs) {
        EnchantHelper helper = new EnchantHelper();
        if(cs.isList("Action")){
            for(String n : cs.getStringList("Action"))
                helper.addEnchant(n);
        }else if(cs.isString("Action")){
            helper.addEnchant(cs.getString("Action"));
        }
        return helper;
    }

    private void addEnchant(String n){
        try {
            String[] split = n.split(" ");
            if(split.length != 3)
                return;
            Enchantment ench = Enchantment.getByName(split[1].toUpperCase());
            if(ench == null)
                return;
            int parsed = Integer.parseInt(split[2]);
            if(parsed <= 0)
                return;
            if(parsed >= 65535)
                parsed = 65534;
            switch (split[0].toLowerCase()){
                case "add":
                    data.put(ench,parsed);
                    break;
                case "subtract":
                    data.put(ench,-parsed);
                    break;
                case "set":
                    set.put(ench,parsed);
                    break;
            }
        }catch (Exception ex){

        }
    }
    @Override
    public void levelUp(Player p, ItemStack lv, ItemEvolvingData tool) {
        ItemMeta itemEnchant = lv.getItemMeta();
        for(Map.Entry<Enchantment,Integer> evolve : data.entrySet()){
            int nextLv = itemEnchant.getEnchantLevel(evolve.getKey()) + evolve.getValue();
            nextLv = Math.min(65534,Math.max(0,nextLv));
            if(nextLv == 0)
                itemEnchant.removeEnchant(evolve.getKey());
            else
                itemEnchant.addEnchant(evolve.getKey(),nextLv,true);
        }

        for(Map.Entry<Enchantment,Integer> evolve : set.entrySet()){
            int nextLv = itemEnchant.getEnchantLevel(evolve.getKey()) + evolve.getValue();
            if(nextLv == 0)
                itemEnchant.removeEnchant(evolve.getKey());
            else
                itemEnchant.addEnchant(evolve.getKey(),nextLv,true);
        }
        lv.setItemMeta(itemEnchant);
    }

    @Override
    public void listen(ItemStack tool) {

    }
}
