package me.noogs.rechant;

import me.noogs.rechant.listeners.RecycleEnchantListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Rechant extends JavaPlugin {

    FileConfiguration config;
    File cfile;

    private static Rechant plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Rechant Plugin has started.");

        // Set Instance
        plugin = this;

        // Get Config.yml
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        cfile = new File(getDataFolder(),"config.yml");

        //Command
        getCommand("rc").setExecutor(new rcCommands());

        //anvil Event
        getServer().getPluginManager().registerEvents(new RecycleEnchantListener(),this);

    }

    public void setupConfig() {
        File resetFile = new File(getDataFolder(), "RESET.FILE");

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

