package com.cham.sellWands;

import com.cham.sellWands.Util.GetWand;
import com.cham.sellWands.Util.WandListener;
import com.earth2me.essentials.Essentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SellWands extends JavaPlugin {

    public static SellWands sellWands;

    public Economy ECON;

    public Essentials essentials;

    public FileBuilder fileBuilder;

    @Override
    public void onEnable() {
        sellWands = this;
        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        if(essentials == null) {
            getLogger().severe("Essentials not found in plugins folder, disabling Sell Wands.");
            return;
        }
        if(initVault()) {
            fileBuilder = new FileBuilder(this);
            fileBuilder.writeDefaultWand();
            registerCommands();
            registerListeners();
        }
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {
        getCommand("getwand").setExecutor(new GetWand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new WandListener(), this);
    }

    public static SellWands getSellWands() {
        return sellWands;
    }

    public FileBuilder getFileBuilder() {
        return fileBuilder;
    }

    private boolean initVault() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().info("Vault plugin not detected, can't initialize " + this.getName());
            return false;
        }
        RegisteredServiceProvider<Economy> registeredServiceProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if(registeredServiceProvider == null) {
            return false;
        }
        getLogger().info("Vault plugin detected, Sell Wands initializing");
        ECON = registeredServiceProvider.getProvider();
        return ECON != null;
    }

}
