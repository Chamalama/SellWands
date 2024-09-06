package com.cham.sellwands.commands;

import com.cham.sellwands.SellWandFactory;
import com.cham.sellwands.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WandCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!sender.hasPermission("admin.get.wand")) return false;

        if (args.length < 2) {
            sender.sendMessage(Chat.translate("&cIncorrect usage: /givewand <player> <usages>"));
            return false;
        }

        final Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(Chat.translate("&cThe user you provided is invalid or not online."));
            return false;
        }

        try {
            final int uses = Integer.parseInt(args[1]);
            target.getInventory().addItem(SellWandFactory.get().buildWand(uses));

        } catch (NumberFormatException e) {
            sender.sendMessage(Chat.translate("&cThe number you provided is invalid."));
        }

        return false;
    }
}
