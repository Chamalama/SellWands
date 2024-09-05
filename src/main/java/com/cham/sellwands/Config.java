package com.cham.sellwands;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Config {

    private final Plugin plugin;

    public Config(Plugin plugin) {

        plugin.getDataFolder().mkdirs();
        plugin.saveDefaultConfig();

        this.plugin = plugin;
    }

    public String getWandName() {
        return plugin.getConfig().getString("name");
    }

    public Material getWandMaterial() {

        try {
            return Material.valueOf(plugin.getConfig().getString("material"));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().severe("The material provided in the config is not a valid material.");
        }

        return null;
    }

    public String getWandSellMessage() {
        return plugin.getConfig().getString("messages.sell");
    }

    public String getWandBreakMessage() {
        return plugin.getConfig().getString("messages.break");
    }

    public String getWandNothingToSellMessage() {
        return plugin.getConfig().getString("messages.nothing");
    }

    public String getWandBreakdownMessage() {
        return plugin.getConfig().getString("messages.breakdown");
    }

    public List<String> getWandLore() {
        return plugin.getConfig().getStringList("lore");
    }
}
