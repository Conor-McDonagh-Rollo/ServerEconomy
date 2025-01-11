package me.conormcdr.servereconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.conormcdr.servereconomy.servereconomy;

public class SetFunds implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            if (p.hasPermission("seconomy.setfunds"))
            {
                if (args.length >= 2)
                {
                    OfflinePlayer payee = Bukkit.getOfflinePlayer(args[0]);
                    if (payee == null)
                    {
                        p.sendMessage(ChatColor.RED + "Could not find player.");
                        return true;
                    }

                    int payAmount;

                    try {
                        payAmount = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(ChatColor.RED + "Invalid amount.");
                        return true;
                    }

                    if (payAmount < 0)
                    {
                        p.sendMessage(ChatColor.RED + "Invalid amount.");
                        return true;
                    }

                    String payeeName = payee.getName();

                    servereconomy.SetFunds(payeeName, payAmount);

                    String currency = servereconomy.GetCurrency();

                    p.sendMessage(ChatColor.GREEN + "Successfully set " + payeeName + "'s account balance to " + currency + payAmount);
                    if (payee.isOnline())
                    {
                        payee.getPlayer().sendMessage(ChatColor.GREEN + "Your account balance has been set to " + currency + payAmount);
                    }
                }
                else
                {
                    p.sendMessage(ChatColor.RED + "Usage: /pay <Player> <Amount>");
                }
            }
            else
            {
                p.sendMessage(ChatColor.RED + "You do not have permission.");
            }
        }

        return true;
    }

}
