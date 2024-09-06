package com.cham.sellwands;

import com.cham.sellwands.commands.WandCommand;
import com.cham.sellwands.listener.WandListener;
import com.earth2me.essentials.Essentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SellWandPlugin extends JavaPlugin {

    private Economy eco;
    private Essentials essentials;
    private Config config;

    public Essentials getEssentials() {
        return essentials;
    }

    public Economy getEconomy() {
        return eco;
    }

    @Override
    public void onEnable() {
        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

        if (essentials == null) {
            getLogger().severe("Essentials not found in plugins folder, disabling Sell Wands.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        initVault();

        getCommand("givewand").setExecutor(new WandCommand());

        new WandListener(this);

        config = new Config(this);

        new SellWandFactory(this);


    }

    public Config getConfiguration() {
        return config;
    }

    private void initVault() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Failed to locate an economy provider.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            getLogger().severe("Failed to locate an economy provider.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        getLogger().info("Vault plugin detected, Sell Wands initializing");
        eco = registeredServiceProvider.getProvider();
    }

}
