package me.conormcdr.servereconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.conormcdr.servereconomy.servereconomy;

public class AddItemToGE implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            if (p.hasPermission("seconomy.geadd"))
            {
                if (args.length >= 1)
                {
                    int cost = 9999;
                    if (args[0] != null)
                    {
                        try {
                            cost = Integer.parseInt(args[0]);
                        } catch (NumberFormatException e)
                        {
                            cost = 9999;
                        }
                    }

                    ItemStack item = p.getInventory().getItemInMainHand();

                    if (servereconomy.AddItemStackToGE(p, item, cost))
                    {
                        p.getInventory().setItemInMainHand(null);
                        p.sendMessage(ChatColor.GOLD + "Your item has been safely shipped to the Grand Exchange. If unsold, the item will return to you in a week.");
                    }
                }
                else
                {
                    p.sendMessage(ChatColor.RED + "Usage: /sellhand <Cost>");
                }
            }
        }

        return true;
    }
}
