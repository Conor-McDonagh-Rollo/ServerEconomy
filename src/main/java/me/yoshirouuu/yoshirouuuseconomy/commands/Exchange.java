package me.yoshirouuu.yoshirouuuseconomy.commands;

import me.yoshirouuu.yoshirouuuseconomy.yoshirouuuseconomy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Exchange implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("yeconomy.exchange")) {

                Integer value = yoshirouuuseconomy.GetExchangeIndex().get(p.getInventory().getItemInMainHand().getType());
                if (value != null)
                {
                    value *= p.getInventory().getItemInMainHand().getAmount();
                    p.getInventory().setItemInMainHand(null);
                    yoshirouuuseconomy.AddFunds(p.getName(), value.intValue());
                    p.sendMessage(ChatColor.GREEN + "You have just exchanged your item(s) for " + yoshirouuuseconomy.GetCurrency() + value);
                }
                else
                {
                    p.sendMessage(ChatColor.RED + "This item is unable to be exchanged.");
                }
            }
        }
        return true;
    }
}
