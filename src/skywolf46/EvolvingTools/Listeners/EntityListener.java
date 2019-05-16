package skywolf46.EvolvingTools.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import skywolf46.EvolvingTools.Data.ItemEvolvingData;

public class EntityListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void entityDamageEv(EntityDamageByEntityEvent e) {
        if(e.isCancelled())
            return;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            ItemStack[] itemArray = new ItemStack[]{
                    p.getInventory().getHelmet(),
                    p.getInventory().getChestplate(),
                    p.getInventory().getLeggings(),
                    p.getInventory().getBoots(),
                    p.getInventory().getItemInMainHand()
            };
            for (ItemStack item : itemArray) {
                ItemEvolvingData evolve = new ItemEvolvingData(item);
                if (evolve.isEvolveItem()) {
                    if (evolve.getEvolveData().isListeningEvent("HitPlayer")) {
                        evolve.addExp(p,1);
                    }
                    if (evolve.getEvolveData().isListeningEvent("DamagePlayer")) {
                        evolve.addExp(p, (int) e.getFinalDamage());
                    }
                }
            }
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            ItemStack[] itemArray = new ItemStack[]{
                    p.getInventory().getHelmet(),
                    p.getInventory().getChestplate(),
                    p.getInventory().getLeggings(),
                    p.getInventory().getBoots(),
                    p.getInventory().getItemInMainHand()
            };
            for (ItemStack item : itemArray) {
                ItemEvolvingData evolve = new ItemEvolvingData(item);
                if (evolve.isEvolveItem()) {
                    if (evolve.getEvolveData().isListeningEvent("Hitted")) {
                        evolve.addExp(p,1);
                    }
                    if (evolve.getEvolveData().isListeningEvent("Damaged")) {
                        evolve.addExp(p, (int) e.getFinalDamage());
                    }
                }
            }
        }
    }

}
