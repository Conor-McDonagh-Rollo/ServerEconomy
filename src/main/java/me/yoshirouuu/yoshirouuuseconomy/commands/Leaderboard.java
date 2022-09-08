package me.yoshirouuu.yoshirouuuseconomy.commands;

import me.yoshirouuu.yoshirouuuseconomy.yoshirouuuseconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;

public class Leaderboard  implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("yeconomy.leaderboard")) {

                HashMap<String, Integer> bals = new HashMap<>();
                bals.putAll(yoshirouuuseconomy.GetBalances());
                List<String> keys = new ArrayList<>();
                for (String key : bals.keySet())
                {
                    keys.add(key);
                }
                List<String> sortedString = new ArrayList<>();
                List<Integer> sortedInteger = new ArrayList<>();
                int found = 0;
                int toFind = 10;
                if (bals.size() < 10) toFind = bals.size();
                while(found < toFind)
                {
                    String largest = keys.get(0);
                    for (int i = 0; i < keys.size(); i++)
                    {
                        if (bals.get(keys.get(i)) > bals.get(largest))
                        {
                            largest = keys.get(i);
                        }
                    }
                    sortedString.add(largest);
                    sortedInteger.add(bals.get(largest));
                    keys.remove(largest);
                    found++;
                }
                System.out.println(sortedInteger.size());
                int order = 1;
                p.sendMessage(ChatColor.GOLD + "------Leaderboard------");
                for (int i = 0; i < sortedString.size(); i++)
                {
                    p.sendMessage(ChatColor.GOLD + "(" + order + ") " + yoshirouuuseconomy.GetCurrency() +
                            sortedInteger.get(i) + " - " + sortedString.get(i));
                    order++;
                }
                p.sendMessage(ChatColor.GOLD + "-----------------------");
            }
        }
        return true;
    }
}
