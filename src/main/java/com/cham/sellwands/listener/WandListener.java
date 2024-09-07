package com.cham.sellwands.listener;

import com.cham.sellwands.SellWandFactory;
import com.cham.sellwands.SellWandPlugin;
import com.cham.sellwands.util.Chat;
import com.cham.sellwands.wrapper.BreakdownEntry;
import com.earth2me.essentials.Essentials;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class WandListener implements Listener {

    private final SellWandPlugin plugin;

    private final Essentials essentials;

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    public WandListener(SellWandPlugin plugin) {
        this.plugin = plugin;
        this.essentials = plugin.getEssentials();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        ItemStack stack = event.getItem();
n
        if (event.getHand() != EquipmentSlot.HAND
                || stack == null
                || !SellWandFactory.get().isSellWand(stack)
                || block == null)
            return;

        final int uses = SellWandFactory.get().getCurrentUsages(stack);

        if (uses <= 0) {
            stack.setAmount(0);
            return;
        }

        if (!(block.getState() instanceof Container chest) || player.isSneaking()) return;

        final Inventory inventory = chest.getInventory();

        event.setCancelled(true);

        double totalPrice = 0;

        final Map<Material, BreakdownEntry> breakdownMap = new HashMap<>();

        for (ItemStack itemStack : inventory) {
            if (itemStack == null || essentials.getWorth().getPrice(essentials, itemStack) == null || essentials.getWorth().getPrice(essentials, itemStack).doubleValue() <= 0)
                continue;

            final double price = essentials.getWorth().getPrice(essentials, itemStack).doubleValue() * itemStack.getAmount();

            breakdownMap.putIfAbsent(itemStack.getType(), new BreakdownEntry());

            breakdownMap.get(itemStack.getType()).addAmount(itemStack.getAmount());
            breakdownMap.get(itemStack.getType()).addValue(price);

            totalPrice += price;
            itemStack.setAmount(0);
        }

        if (totalPrice == 0) {
            player.sendMessage(Chat.translate(plugin.getConfiguration().getWandNothingToSellMessage()));
            return;
        }

        plugin.getEconomy().depositPlayer(player, totalPrice);
        player.sendMessage(Chat.translate(plugin.getConfiguration().getWandSellMessage()
                .replace("%value%", decimalFormat.format(totalPrice))
                .replace("%uses%", NumberFormat.getInstance().format(uses - 1))
        ));

        // Handles Breakdown message
        if (!plugin.getConfiguration().getWandBreakdownMessage().isBlank()) {
            breakdownMap.forEach((material, breakdownEntry) ->
                    player.sendMessage(Chat.translate(plugin.getConfiguration().getWandBreakdownMessage()
                            .replace("%value%", decimalFormat.format(breakdownEntry.getValue()))
                            .replace("%material%", Chat.capitalize(material.toString().replace("_", " ")))
                            .replace("%amount%", NumberFormat.getInstance().format(breakdownEntry.getAmount()))
                    )));

        }

        if (uses - 1 == 0) {
            stack.setAmount(0);
            player.sendMessage(Chat.translate(plugin.getConfiguration().getWandBreakMessage()));
        }

        SellWandFactory.get().setUses(stack, uses - 1);

    }
}
