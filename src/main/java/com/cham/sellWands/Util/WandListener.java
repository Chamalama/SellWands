package com.cham.sellWands.Util;

import com.cham.sellWands.SellWand;
import com.cham.sellWands.SellWands;
import com.earth2me.essentials.Essentials;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.text.CompactNumberFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class WandListener implements Listener {

    private Essentials essentials = SellWands.getSellWands().essentials;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack stack = player.getInventory().getItem(EquipmentSlot.HAND);

        if(event.getHand() != EquipmentSlot.HAND) return;

        if(!SellWand.SELLWAND.isSellWand(stack)) return;

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(block == null) return;

        if(block.getState() instanceof Container chest) {
            Inventory inventory = chest.getInventory();
            if(player.isSneaking()) {
                BigDecimal initialVal = BigDecimal.ZERO;
                for(int i = 0; i < inventory.getSize(); i++) {
                    ItemStack inventoryItem = inventory.getItem(i);
                    if (inventoryItem != null && essentials.getWorth().getPrice(essentials, inventoryItem).doubleValue() > 0) {
                        BigDecimal itemPrice = essentials.getWorth().getPrice(essentials, inventoryItem);
                        BigDecimal amountPrice = itemPrice.multiply(new BigDecimal(inventoryItem.getAmount()));
                        initialVal = initialVal.add(amountPrice);
                        inventory.setItem(i, null);
                    }
                }
                if(initialVal.longValue() > 0) {
                    SellWands.getSellWands().ECON.depositPlayer(player, initialVal.doubleValue());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', SellWands.getSellWands().getFileBuilder().getWandSellMessage().replace("%value%", decimalFormat.format(initialVal.doubleValue()))));
                    player.getInventory().setItem(EquipmentSlot.HAND, SellWand.SELLWAND.getUpdatedWand(player, stack));
                }
            }
        }
    }
}
