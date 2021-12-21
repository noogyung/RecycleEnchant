package me.noogs.rechant;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import static org.bukkit.Bukkit.getLogger;

public class rcCommands implements CommandExecutor {

    Plugin plugin = Rechant.getPlugin(Rechant.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PluginDescriptionFile pdf = plugin.getDescription();

        if (sender instanceof Player p){

            if(args.length > 0){
                String inputCommand = args[0];
                switch (inputCommand){
                    case "commands" :
                        p.sendMessage("/rc commands, /rc ver, /rc help");
                        break;
                    case "ver" :
                        p.sendMessage("ReChant Version : " + ChatColor.GREEN + pdf.getVersion());
                        break;
                    case "help":
                        p.sendMessage(plugin.getConfig().getString("help"));
                    default:
                        p.sendMessage(plugin.getConfig().getString("whatsCommands"));
                        break;
                }
            }
            else{
                p.sendMessage(plugin.getConfig().getString("needCommandArg"));
                p.sendMessage("/rc commands");
            }

        }
        else {
            getLogger().info(plugin.getConfig().getString("notPlayer"));
        }

        return true;
    }
}
