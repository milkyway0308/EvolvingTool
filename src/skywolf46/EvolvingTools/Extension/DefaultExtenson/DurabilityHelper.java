package skywolf46.EvolvingTools.Extension.DefaultExtenson;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import skywolf46.EvolvingTools.Data.ItemEvolvingData;
import skywolf46.EvolvingTools.Extension.AttributeExtension;

public class DurabilityHelper extends AttributeExtension {
    private int value = 0;
    // 0 = Add
    // 1 = Subtract
    // 2 = Set
    private int mode = 0;
    @Override
    public AttributeExtension createExtension(ConfigurationSection cs) {
        DurabilityHelper helper = new DurabilityHelper();
        if(cs.isString("Action")){
            String[] split = cs.getString("Action").split(" ");
            if(split.length != 2)
                return helper;
            try {
                helper.value = Math.max(0,Integer.parseInt(split[1]));
            }catch (Exception ex){
                return helper;
            }
            switch (split[0].toLowerCase()){
                case "add":
                    helper.mode = 0;
                    break;
                case "subtract":
                    helper.mode = 1;
                    break;
                case "set":
                    helper.mode = 2;
                    break;
            }
        }
        return helper;
    }

    @Override
    public void levelUp(Player p, ItemStack lv, ItemEvolvingData tool) {
        Material mat = lv.getType();
        int max = mat.getMaxDurability();
        int current = lv.getDurability();
        if(max <= 0)
            return;
        switch (mode){
            case 0:
                current = Math.min(Short.MAX_VALUE,Math.max(0,current - value));
                break;
            case 1:
                current = Math.max(0,Math.min(max,current + value));
                break;
            case 2:
                current = Math.min(Short.MAX_VALUE,Math.max(0,value));
                break;
        }
        lv.setDurability((short) current);
    }

    @Override
    public void listen(ItemStack tool) {

    }
}
