package com.cham.sellWands;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBuilder {

    private final SellWands sellWands;
    private final File dataFolder;
    private File wandFile;

    public FileBuilder(SellWands sellWands) {
        this.sellWands = sellWands;
        this.dataFolder = sellWands.getDataFolder();
    }

    public void writeDefaultWand() {
        wandFile = new File(dataFolder, "wands.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(wandFile);

        if (!wandFile.exists()) {
            config.set("Wand Name", "&6&lSell Wand");
            config.set("Wand Material", Material.STICK.name());
            config.set("Wand Lore", new ArrayList<String>());
            config.set("Wand Uses", 5);
            config.set("Wand Sell Message", "");
            config.set("Wand Break Message", "");
            try {
                config.save(wandFile);
            } catch (IOException e) {
                sellWands.getLogger().info("Error building default wand yml file...");
            }
        }
        sellWands.getLogger().info("Successfully generated wands.yml file");
    }

    public String getWandName() {
        return YamlConfiguration.loadConfiguration(wandFile).getString("Wand Name");
    }

    public String getWandMaterial() {
        return YamlConfiguration.loadConfiguration(wandFile).getString("Wand Material");
    }

    public String getWandSellMessage() {
        return YamlConfiguration.loadConfiguration(wandFile).getString("Wand Sell Message");
    }

    public String getWandBreakMessage() {
        return YamlConfiguration.loadConfiguration(wandFile).getString("Wand Break Message");
    }

    public int getWandUses() {
        return YamlConfiguration.loadConfiguration(wandFile).getInt("Wand Uses");
    }

    public List<String> getWandLore() {
        return (List<String>) YamlConfiguration.loadConfiguration(wandFile).getList("Wand Lore");
    }

    public File getWandFile() {
        return wandFile;
    }
}
