package skywolf46.EvolvingTools.Data;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skywolf46.EvolvingTools.EvolvingTools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ItemEvolvingData {
    private String evolve;
    private EvolvingData evolveData = null;
    private int level = 1;
    private int exp = 0;
    private int loreLine = -1;
    private ItemStack item;

    public ItemEvolvingData(ItemStack item) {
        this.item = item;
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> text = item.getItemMeta().getLore();
            for (int i = 0; i < text.size(); i++) {
                Matcher mat = EvolvingTools.getEvolvingPattern().matcher(ChatColor.stripColor(text.get(i)));
                if (mat.find()) {
                    loreLine = i;
                    evolve = mat.group("Evolver");
                    level = Integer.parseInt(mat.group("Lv"));
                    exp = Integer.parseInt(mat.group("exp"));
                    evolveData = EvolvingTools.getEvolveData(evolve);
                    break;
                }
            }
        }
    }

    public void updateItem() {
        if (loreLine == -1)
            return;
        if (evolveData == null)
            return;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        StringBuilder progress = new StringBuilder();
        long per = evolveData.getMaxExp(level);
        if (per == -1 || evolveData.getMaxExp(level) == -1)
            progress.append("§e|||||||");
        else {
            per /= 7;
            long text = Math.min(7, this.exp / per);
            progress.append("§c");
            for (int i = 0; i < text; i++)
                progress.append("|");
            progress.append("§7");
            for (int i = (int) text; i < 7; i++)
                progress.append("|");
            progress.append("§f");
        }
        lore.set(loreLine,
                EvolvingTools.getFormat()
                        .replace("<EvolveName>", evolve)
                        .replace("<Lv>", String.valueOf(level))
                        .replace("<exp>", String.valueOf(exp))
                        .replace("<maxExp>", evolveData.getMaxExp(level) == -1 || evolveData.getMaxExp(level + 1) == -1 ? "∞" : String.valueOf(evolveData.getMaxExp(level)))
                        .replace("<progressBar>", progress.toString())
        );
        meta.setLore(lore);
        item.setItemMeta(meta);
    }


    public void setExp(Player p,int exp) {
        this.exp = Math.max(0, exp);
        checkExp(p);
    }

    public void addExp(Player p,int exp) {
        setExp(p,this.exp + exp);
    }


    public void checkExp(Player p) {
        if (evolveData == null)
            return;
        while (evolveData.getMaxExp(level) != -1 && evolveData.getMaxExp(level + 1) != -1 && evolveData.getMaxExp(level) <= exp) {
            exp -= evolveData.getMaxExp(level++);
            List<String> str = evolveData.getMessage("Level up to " + level);
            if (str != null)
                for (String n : str)
                    p.sendMessage(n);
            else {
                str = evolveData.getMessage("Level up");
                if (str != null)
                    for (String n : str)
                        p.sendMessage(n);
            }
            evolveData.loopExtension(p,item, this);
        }
        updateItem();
    }


    public long getExp() {
        return this.exp;
    }

    public int getLevel() {
        return this.level;
    }

    public EvolvingData getEvolveData() {
        return this.evolveData;
    }

    public boolean isEvolveItem() {
        return this.evolve != null && this.evolveData != null;
    }


    public void setEvolveData(Player p,String evData) {
        this.evolveData = EvolvingTools.getEvolveData(evData);
        checkExp(p);
    }
}
