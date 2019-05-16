package skywolf46.EvolvingTools.Listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import skywolf46.EvolvingTools.Data.ItemEvolvingData;

public class BlockListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void ev(BlockBreakEvent e) {
        if(e.isCancelled())
            return;
        ItemStack itemInHand = e.getPlayer().getItemInHand();
        if (itemInHand != null && itemInHand.getType() != Material.AIR) {
            ItemEvolvingData ied = new ItemEvolvingData(itemInHand);
            if (ied.isEvolveItem()) {
                if(!ied.getEvolveData().isListeningEvent("BlockBreak"))
                    return;
                int cool = e.getPlayer().getCooldown(itemInHand.getType());
                e.getPlayer().setCooldown(itemInHand.getType(),0);
                ied.addExp(e.getPlayer(),1);
                e.getPlayer().setCooldown(itemInHand.getType(),cool);
            }
        }
    }
}
