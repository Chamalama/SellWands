package com.cham.sellwands;

import com.cham.sellwands.util.Chat;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.NumberFormat;
import java.util.Objects;
import java.util.UUID;

public class SellWandFactory {

    private static SellWandFactory instance;
    private final SellWandPlugin plugin;

    private final NamespacedKey wandKey;
    private final NamespacedKey idKey;
    private final NamespacedKey usesKey;


    private final AttributeModifier attributeModifier;

    private final NumberFormat numberFormat = NumberFormat.getInstance();
    private ItemStack baseWand;

    public SellWandFactory(SellWandPlugin plugin) {
        SellWandFactory.instance = this;

        this.plugin = plugin;

        this.wandKey = new NamespacedKey(plugin, "sell_wand");
        this.idKey = new NamespacedKey(plugin, "id");
        this.usesKey = new NamespacedKey(plugin, "uses");

        this.attributeModifier = new AttributeModifier(new NamespacedKey(plugin, "ATT"), 0, AttributeModifier.Operation.ADD_NUMBER);
    }

    public static SellWandFactory get() {
        return instance;
    }

    public ItemStack buildWand(int uses) {

        if (baseWand == null) {
            this.baseWand = new ItemStack(plugin.getConfiguration().getWandMaterial());

            this.baseWand.editMeta(meta -> {
                meta.getPersistentDataContainer().set(wandKey, PersistentDataType.BOOLEAN, true);

                meta.setUnbreakable(true);

                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, this.attributeModifier);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            });
        }


        ItemStack toReturn = baseWand.asOne();

        toReturn.editMeta(itemMeta -> {
            itemMeta.getPersistentDataContainer().set(usesKey, PersistentDataType.INTEGER, uses);
            itemMeta.getPersistentDataContainer().set(idKey, PersistentDataType.STRING, UUID.randomUUID().toString());
        });

        updateMeta(toReturn, uses);

        return toReturn;
    }

    public boolean isSellWand(ItemStack stack) {
        return stack.hasItemMeta() && stack.getItemMeta().getPersistentDataContainer().has(wandKey);
    }

    public int getCurrentUsages(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return 0;
        PersistentDataContainer persistentData = meta.getPersistentDataContainer();
        if (persistentData.has(usesKey))
            return Objects.requireNonNull(persistentData.get(usesKey, PersistentDataType.INTEGER));

        return 0;
    }

    public void updateMeta(ItemStack itemStack, int uses) {
        itemStack.editMeta(meta -> {
            meta.displayName(Chat.translate(plugin.getConfiguration().getWandName().replace("%uses%", numberFormat.format(uses))));
            meta.lore(Chat.translate(plugin.getConfiguration().getWandLore().stream().map(x -> x.replace("%uses%", numberFormat.format(uses))).toList()));
        });
    }

    public void setUses(ItemStack item, int uses) {
        item.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(usesKey, PersistentDataType.INTEGER, uses));

        updateMeta(item, uses);
    }

}
