package me.yoshirouuu.yoshirouuuseconomy.commands;

import me.yoshirouuu.yoshirouuuseconomy.yoshirouuuseconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Balance implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            if (p.hasPermission("yeconomy.balance"))
            {
                int balance = yoshirouuuseconomy.GetBalance(p.getName());
                String currency = yoshirouuuseconomy.GetCurrency();
                p.sendMessage(ChatColor.GREEN + "Your current balance is " + currency + balance);
            }
            else
            {
                p.sendMessage(ChatColor.RED + "You do not have permission.");
            }
        }

        return true;
    }

}
