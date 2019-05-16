package skywolf46.EvolvingTools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import skywolf46.CommandAnnotation.API.MinecraftAbstractCommand;
import skywolf46.CommandAnnotation.CommandAnnotation;
import skywolf46.EvolvingTools.Command.ReloadEvolveCommand;
import skywolf46.EvolvingTools.Command.SetEvolveCommand;
import skywolf46.EvolvingTools.Data.EvolvingData;
import skywolf46.EvolvingTools.Extension.DefaultExtenson.EnchantHelper;
import skywolf46.EvolvingTools.Listeners.BlockListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class EvolvingTools extends JavaPlugin {
    private static EvolvingTools inst;
    private static Pattern evolvingPattern;
    private static String format;
    private static HashMap<String, EvolvingData> evolves = new HashMap<>();


    @Override
    public void onEnable() {
        inst = this;
        Bukkit.getConsoleSender().sendMessage("§9EvolvingTools §7| §fStarting plugin");
        Bukkit.getPluginManager().registerEvents(new BlockListener(),this);
        new EnchantHelper().registerExtension("Enchant Helper");
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveResource("config.yml", true);
            reloadConfig();
        }
        CommandAnnotation.forceInit(this);
        MinecraftAbstractCommand.builder()
                .add(new SetEvolveCommand())
                .command("/addevolve")
                .complete();

        MinecraftAbstractCommand.builder()
                .add(new ReloadEvolveCommand())
                .command("/reloadevolve")
                .complete();
        format = getConfig().getString("Evolving format", "&9<EvolveName> &7| &cLevel &4<Lv> (<exp>/<currentExp>) &f[&a<progressBar>&f]");
        format = ChatColor.translateAlternateColorCodes('&',format);
        evolvingPattern = Pattern.compile(
                replaceForPattern(ChatColor.stripColor(format))
                        .replace("\\<EvolveName\\>", "(?<Evolver>.*?)")
                        .replace("\\<Lv\\>", "(?<Lv>\\d+)")
                        .replace("\\<exp\\>", "(?<exp>\\d+){1,19}")
                        .replace("\\<maxExp\\>", "(?<Nothing>.*?)")
                        .replace("\\<progressBar\\>", "(?<Serious>.*?)")
        );
        reloadEvolveData();
        Bukkit.getConsoleSender().sendMessage("§9EvolvingTools §7| §fEvolution of tool has just started.");

    }


    public static EvolvingTools getInstance() {
        return inst;
    }

    public static void reloadEvolveData() {
        evolves.clear();
        File folder = new File(inst.getDataFolder(), "EvolutionTypes");
        if (!folder.exists()) {
            folder.mkdirs();
            inst.saveResource("EvolutionTypes/EnchantGrowingPickaxe.yml",true);
        }
        for(File f : folder.listFiles()){
            String fileName = f.getName().substring(0, f.getName().length() - 4);
            try {
                if(!f.getName().endsWith(".yml"))
                    continue;
                YamlConfiguration load = YamlConfiguration.loadConfiguration(f);
                evolves.put(fileName,new EvolvingData(load));
                Bukkit.getConsoleSender().sendMessage("§9EvolvingTools §7| §7Loaded evolving type " + fileName);
            }catch (Exception ex){
                ex.printStackTrace();
                Bukkit.getConsoleSender().sendMessage("§9EvolvingTools §7| §cFailed to load evolving type " + fileName);
            }
        }
    }

    public static EvolvingData getEvolveData(String ev) {
        return evolves.get(ev);
    }

    public static String getFormat() {
        return format;
    }

    public static Pattern getEvolvingPattern() {
        return evolvingPattern;
    }

    public static final String replaceForPattern(String pattern) {
        return pattern.replace("|", "\\|")
                .replace("$", "\\$")
                .replace("&", "\\&")
                .replace("*", "\\*")
                .replace("-", "\\-")
                .replace("+", "\\+")
                .replace("/", "\\/")
                .replace("<", "\\<")
                .replace(">", "\\>")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("[", "\\[")
                .replace("]", "\\]");
    }

    public static List<String> getEvolvingDataList() {
        return new ArrayList<>(evolves.keySet());
    }
}
