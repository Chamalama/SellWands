package com.cham.sellWands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SellWand {

    public static SellWand SELLWAND = new SellWand();

    private SellWands PLUGIN = SellWands.getSellWands();

    private NamespacedKey wandKey = new NamespacedKey(SellWands.getSellWands(), "SELL_WAND");
    private NamespacedKey idKey = new NamespacedKey(SellWands.getSellWands(), "ID");
    private NamespacedKey usesKey = new NamespacedKey(SellWands.getSellWands(), "USES");


    public ItemStack buildWand(int uses) {
        ItemStack stack = new ItemStack(Material.valueOf(PLUGIN.getFileBuilder().getWandMaterial()));
        ItemMeta meta = stack.getItemMeta();

        List<String> metaLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', PLUGIN.getFileBuilder().getWandName()));
        List<String> lore = PLUGIN.getFileBuilder().getWandLore();

        for(String s : lore) {
            metaLore.add(ChatColor.translateAlternateColorCodes('&', s).replace("%wand_uses%", String.valueOf(uses)));
        }

        meta.setLore(metaLore);

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(wandKey, PersistentDataType.STRING, "WAND");
        pdc.set(idKey, PersistentDataType.INTEGER, new Random().nextInt());
        pdc.set(usesKey, PersistentDataType.INTEGER, uses);

        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(new NamespacedKey(SellWands.getSellWands(), "ATT"), 0, AttributeModifier.Operation.ADD_NUMBER));
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
        return stack;
    }

    public boolean isSellWand(ItemStack stack) {
        return stack.hasItemMeta() && stack.getItemMeta().getPersistentDataContainer().has(wandKey);
    }

    public NamespacedKey getIdKey() {
        return idKey;
    }

    public NamespacedKey getUsesKey() {
        return usesKey;
    }

    public NamespacedKey getWandKey() {
        return wandKey;
    }

    public int getCurrentUsages(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if(meta == null) return 0;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(pdc.has(wandKey)) {
            return pdc.get(wandKey, PersistentDataType.INTEGER);
        }
        return 0;
    }

    public ItemStack getUpdatedWand(Player player, ItemStack currentWand) {
        int currentUses = currentWand.getItemMeta().getPersistentDataContainer().get(usesKey, PersistentDataType.INTEGER);
        currentUses-=1;
        if(currentUses >= 1) {
            return buildWand(currentUses);
        }else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', SellWands.getSellWands().getFileBuilder().getWandBreakMessage()));
            return null;
        }
    }

}
