package me.conormcdr.servereconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.conormcdr.servereconomy.servereconomy;

import java.util.UUID;

public class Pay implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            if (p.hasPermission("seconomy.pay"))
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

                    if (payAmount <= 0)
                    {
                        p.sendMessage(ChatColor.RED + "Invalid amount.");
                        return true;
                    }

                    int balance = servereconomy.GetBalance(p.getName());

                    if (payAmount > balance)
                    {
                        p.sendMessage(ChatColor.RED + "Insufficient funds.");
                        return true;
                    }

                    String payerName = p.getName();
                    String payeeName = payee.getName();

                    int tax = servereconomy.GetPaymentTax();
                    int amountTaxed = 0;
                    if (tax > 0)
                    {
                        amountTaxed = (int)(((float)payAmount / 100f) * (float)tax);
                    }
                    payAmount -= amountTaxed;

                    servereconomy.PayPlayer(payerName, payeeName, payAmount);

                    String currency = servereconomy.GetCurrency();
                    if (amountTaxed > 0)
                    {
                        p.sendMessage(ChatColor.GREEN + "Successfully paid " + currency + payAmount +
                                " to " + payeeName + ChatColor.RED + " (Tax: " + currency + amountTaxed + ")");
                    }
                    else
                    {
                        p.sendMessage(ChatColor.GREEN + "Successfully paid " + currency + payAmount + " to " + payeeName);
                    }
                    if (payee.isOnline())
                    {
                        payee.getPlayer().sendMessage(ChatColor.GREEN + payerName + " has sent you " + currency + payAmount);
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
