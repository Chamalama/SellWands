package com.cham.sellWands.Util;

import com.cham.sellWands.SellWand;
import com.cham.sellWands.SellWands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetWand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(commandSender instanceof Player p) {
            if(p.hasPermission("admin.get.wand")) {
                p.getInventory().addItem(SellWand.SELLWAND.buildWand(SellWands.getSellWands().getFileBuilder().getWandUses()));
            }
        }
        return false;
    }
}
