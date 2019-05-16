package skywolf46.EvolvingTools.Command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import skywolf46.CommandAnnotation.API.MinecraftAbstractCommand;
import skywolf46.CommandAnnotation.Data.CommandArgument;
import skywolf46.CommandAnnotation.Data.CommandData;
import skywolf46.EvolvingTools.Data.EvolvingData;
import skywolf46.EvolvingTools.Data.ItemEvolvingData;
import skywolf46.EvolvingTools.EvolvingTools;

import java.util.ArrayList;
import java.util.List;

public class SetEvolveCommand extends MinecraftAbstractCommand {
    @Override
    public boolean onCommand(CommandArgument commandArgument) {
        Player p = commandArgument.get(Player.class);
        if (p == null) {
            commandArgument.get(CommandSender.class)
                    .sendMessage("§9Evolving Tools §7| §cPlayer only command.");
            return false;
        } else if (!p.hasPermission("evolve.tools.admin") && !p.isOp()) {
            p.sendMessage("§9Evolving Tools §7| §cPermission denied.");
            return false;
        }
        ItemStack itemInHand = p.getInventory().getItemInMainHand();
        if (itemInHand == null || itemInHand.getType() == Material.AIR)
            p.sendMessage("§9Evolving Tools §7| §cMust have to hold item.");
        else {
            CommandData data = commandArgument.get(CommandData.class);
            if (data.length() <= 0)
                p.sendMessage("§9Evolving Tools §7| §cNot enough parameter : " + data.getCommand() + " <Evolving Type Name>");
            else {
                ItemEvolvingData ie = new ItemEvolvingData(itemInHand);
                if(ie.isEvolveItem()){
                    p.sendMessage("§9Evolving Tools §7| §cCannot apply evolve to item : Item already evolved");
                }
                EvolvingData dat = EvolvingTools.getEvolveData(data.getCommandArgument(0, data.length()));
                if (dat == null)
                    p.sendMessage("§9Evolving Tools §7| §cEvolve type §f" + data.getCommandArgument(0, data.length()) + " §c not registered.");
                else {
                    ItemMeta meta = itemInHand.getItemMeta();
                    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                    lore.add(EvolvingTools.getFormat()
                            .replace("<EvolveName>", data.getCommandArgument(0, data.length()))
                            .replace("<Lv>", String.valueOf(1))
                            .replace("<exp>", String.valueOf(0))
                            .replace("<maxExp>", dat.getMaxExp(1) == -1 || dat.getMaxExp(2) == -1 ? "∞" : String.valueOf(dat.getMaxExp(1)))
                            .replace("<progressBar>", "§7|||||||||||||||§f")
                    );
                    meta.setLore(lore);
                    itemInHand.setItemMeta(meta);
                    dat.getExtensions(1).forEach((ext) -> {
                        ext.levelUp(p, itemInHand, new ItemEvolvingData(itemInHand));
                    });
                    p.sendMessage("§9Evolving Tools §7| §aApplied evolution to item.");
                }
            }
        }

        return false;
    }

    @Override
    public int getCommandPriority() {
        return 0;
    }

    @Override
    public void editCompletion(String[] commands, List<String> complete, String lastArgument) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < commands.length; i++)
            if (i == 1)
                sb.append(commands[i]);
            else
                sb.append(" ").append(commands[i]);
        sb.append(lastArgument);
        String n = sb.toString();
        for (String n4 : EvolvingTools.getEvolvingDataList()) {
            if (n.startsWith(n4))
                complete.add(n4);
        }
    }
}
