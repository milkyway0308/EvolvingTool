package skywolf46.EvolvingTools.Command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import skywolf46.CommandAnnotation.API.MinecraftAbstractCommand;
import skywolf46.CommandAnnotation.Data.CommandArgument;
import skywolf46.EvolvingTools.EvolvingTools;

public class ReloadEvolveCommand extends MinecraftAbstractCommand {
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
        EvolvingTools.reloadEvolveData(p);
        return false;
    }

    @Override
    public int getCommandPriority() {
        return 0;
    }
}
