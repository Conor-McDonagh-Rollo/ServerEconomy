package me.yoshirouuu.yoshirouuuseconomy.commands;

import me.yoshirouuu.yoshirouuuseconomy.yoshirouuuseconomy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Pay implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            if (p.hasPermission("yeconomy.pay"))
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

                    int balance = yoshirouuuseconomy.GetBalance(p.getName());

                    if (payAmount > balance)
                    {
                        p.sendMessage(ChatColor.RED + "Insufficient funds.");
                        return true;
                    }

                    String payerName = p.getName();
                    String payeeName = payee.getName();

                    int tax = yoshirouuuseconomy.GetPaymentTax();
                    int amountTaxed = 0;
                    if (tax > 0)
                    {
                        amountTaxed = (int)(((float)payAmount / 100f) * (float)tax);
                    }
                    payAmount -= amountTaxed;

                    yoshirouuuseconomy.PayPlayer(payerName, payeeName, payAmount);

                    String currency = yoshirouuuseconomy.GetCurrency();
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
