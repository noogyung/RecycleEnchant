package me.noogs.rechant;

import me.noogs.rechant.listeners.RecycleEnchantListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Rechant extends JavaPlugin {

    private static Rechant plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Rechant Plugin has started.");

        // Set Instance
        plugin = this;

        // Get Config.yml
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Version Command
        getCommand("rc").setExecutor(new rcCommands());

        //anvil Event
        getServer().getPluginManager().registerEvents(new RecycleEnchantListener(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Rechant Plugin has stopped.");
    }

    // Set Instance
    public static Rechant getPlugin(){
        return plugin;
    }

}

