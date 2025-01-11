package me.conormcdr.servereconomy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import me.conormcdr.servereconomy.commands.*;

import java.util.*;
import java.util.HashMap;

public final class servereconomy extends JavaPlugin implements Listener {

    private static servereconomy plugin;

    private static HashMap<String, Integer> playerBalances = new HashMap<>();
    private static HashMap<Material, Integer> exchangeIndex = new HashMap<>();
    private static List<GEItem> geItems = new ArrayList<>();
    private static List<Map.Entry<String, ItemStack>> geItemsToReturn = new ArrayList<>();

    private static List<String> loggedPlayers = new ArrayList<>();

    private static String currency;
    private static int payTax;
    private static int minutes = -1;

    public static int currentGEPage = 0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        if (!config.contains("Balances"))
        {
            System.out.println("Config appears to be missing or broken... Creating a new one...");
            List<String> comments = new ArrayList<>();
            comments.add("WARNING");
            comments.add("Do not modify anything other than the values as it could result in data loss.");
            config.setComments("", comments);
            config.set("Currency", "€");
            config.set("PaymentTaxPercent", 0);
            config.set("Balances.Test", 0);
            config.set("Exchange.DIAMOND", 250);

            GEItem item = new GEItem(new ItemStack(Material.POISONOUS_POTATO), 999999, "GOD", 0);
            config.set("geItems." + 0 + ".ItemStack", item.GetItem());
            config.set("geItems." + 0 + ".Owner", item.GetOwner());
            config.set("geItems." + 0 + ".Price", item.GetPrice());
            config.set("geItems." + 0 + ".RemainingTime", item.GetTime());

            config.set("geItemsToReturn." + 0 + ".ItemStack", item.GetItem());
            config.set("geItemsToReturn." + 0 + ".Owner", item.GetOwner());

            saveConfig();
        }
        loadConfig();

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new GrandExchangeClick(), this);

        getCommand("balance").setExecutor(new Balance());
        getCommand("pay").setExecutor(new Pay());
        getCommand("addfunds").setExecutor(new AddFunds());
        getCommand("setfunds").setExecutor(new SetFunds());
        getCommand("grandexchange").setExecutor(new GrandExchange());
        getCommand("exchange").setExecutor(new Exchange());
        getCommand("sellhand").setExecutor(new AddItemToGE());
        getCommand("leaderboard").setExecutor(new Leaderboard());

        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimer(this, () -> {
            for (int i = 0; i < geItems.size(); i++)
            {
                if (geItems.get(i).ReduceTimeLeft())
                {
                    ReturnItemFromGE(i);
                }
            }
        }, 20L /*<-- the initial delay */, 20L * 60L /*<-- the interval */);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        truncateConfig();
        FileConfiguration config = getConfig();
        playerBalances.forEach((key, value) -> {
            config.set("Balances." + key, value);
        });
        int item_i = 0;
        for (int i = 0; i < geItems.size(); i++) {
            GEItem item = geItems.get(i);
            if (item.GetSold()) continue;
            config.set("geItems." + item_i + ".ItemStack", item.GetItem());
            config.set("geItems." + item_i + ".Owner", item.GetOwner());
            config.set("geItems." + item_i + ".Price", item.GetPrice());
            config.set("geItems." + item_i + ".RemainingTime", item.GetTime());
            item_i++;
        }
        for (int i = 0; i < geItemsToReturn.size(); i++) {
            Map.Entry<String, ItemStack> item = geItemsToReturn.get(i);
            config.set("geItemsToReturn." + i + ".ItemStack", item.getValue());
            config.set("geItemsToReturn." + i + ".Owner", item.getKey());
        }
        saveConfig();
    }

    private void truncateConfig()
    {
        FileConfiguration config = getConfig();

        if (config.contains("geItems.0"))
        {
            int index = 1;
            while (config.get("geItems." + index) != null)
            {
                config.set("geItems." + index, null);
                index++;
            }
        }
        if (config.contains("geItemsToReturn.0"))
        {
            int index = 1;
            while (config.get("geItemsToReturn." + index) != null)
            {
                config.set("geItemsToReturn." + index, null);
                index++;
            }
        }
        saveConfig();
    }

    private void loadConfig()
    {
        FileConfiguration config = getConfig();

        for (String player : config.getConfigurationSection("Balances").getKeys(false))
        {
            System.out.println("Loaded balance for " + player + ".");
            if (player.equalsIgnoreCase("Test")) continue;
            playerBalances.put(player, config.getInt("Balances." + player));
        }

        for (String material : config.getConfigurationSection("Exchange").getKeys(false))
        {
            exchangeIndex.put(Material.getMaterial(material), config.getInt("Exchange." + material));
        }

        if (config.contains("geItems.0"))
        {
            int index = 0;
            while (config.get("geItems." + index) != null)
            {
                ItemStack is = config.getItemStack("geItems." + index + ".ItemStack");
                int price = config.getInt("geItems." + index + ".Price");
                String owner = config.getString("geItems." + index + ".Owner");
                int MinutesLeft = config.getInt("geItems." + index + ".RemainingTime");
                GEItem item = new GEItem(is, price, owner, MinutesLeft);
                geItems.add(item);
                index++;
            }
        }
        if (config.contains("geItemsToReturn.0"))
        {
            int index = 0;
            while (config.get("geItemsToReturn." + index) != null)
            {
                ItemStack is = config.getItemStack("geItemsToReturn." + index + ".ItemStack");
                String owner = config.getString("geItemsToReturn." + index + ".Owner");
                Map.Entry<String, ItemStack> item =
                        new AbstractMap.SimpleEntry<String, ItemStack>(owner, is);
                geItemsToReturn.add(item);
                index++;
            }
        }
        currency = config.getString("Currency");
        payTax = config.getInt("PaymentTaxPercent");
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (!getConfig().contains("Balances." + event.getPlayer().getName()))
        {
            playerBalances.put(event.getPlayer().getName(), 0);
            System.out.println(event.getPlayer().getName() + " was put in playerBalances... Value:" + playerBalances.get(event.getPlayer().getName()));
        }

        List<Map.Entry<String, ItemStack>> indexToDelete = new ArrayList<>();
        for (int i = 0; i < geItemsToReturn.size(); i++)
        {
            if (event.getPlayer().getName().equalsIgnoreCase(geItemsToReturn.get(i).getKey()))
            {
                if (event.getPlayer().getInventory().firstEmpty() != -1)
                {
                    event.getPlayer().getInventory().addItem(geItemsToReturn.get(i).getValue());
                    indexToDelete.add(geItemsToReturn.get(i));
                }
            }
        }
        if (indexToDelete.size() > 0) {
            geItemsToReturn.removeAll(indexToDelete);
        }

        if (!loggedPlayers.contains(event.getPlayer().getName()))
        {
            AddFunds(event.getPlayer().getName(), 100);
            event.getPlayer().sendMessage(ChatColor.GOLD + "Thank you for playing, your balance increased by " + currency + "100");
            loggedPlayers.add(event.getPlayer().getName());
        }
    }

    public static void PayPlayer(String payer, String payee, int amount)
    {
        int balance = playerBalances.get(payee);
        balance += amount;
        playerBalances.replace(payee, balance);

        balance = playerBalances.get(payer);
        balance -= amount;
        playerBalances.replace(payer, balance);
    }

    public static void AddFunds(String payee, int amount)
    {
        int balance = playerBalances.get(payee);
        balance += amount;
        playerBalances.replace(payee, balance);
    }

    public static boolean AddItemStackToGE(Player p, ItemStack is, int cost)
    {
        GEItem item = new GEItem(is, cost, p.getName(), 60 * 24 * 7);
        if (item.GetItem() != null)
        {
            geItems.add(item);
            return true;
        }

        return false;
    }

    public static boolean ReturnItemFromGE(int i)
    {
        if (geItems.get(i).GetSold())
        {
            return false;
        }
        String owner = geItems.get(i).GetOwner(); // Find seller
        geItems.get(i).SetSold(); // Item is now sold so nobody makes mistake

        Map.Entry<String,ItemStack> entry =
                new AbstractMap.SimpleEntry<String, ItemStack>(owner, geItems.get(i).GetItem());
        geItemsToReturn.add(entry);

        geItems.remove(geItems.get(i));

        return true;
    }

    public static boolean TransactionGE(Player p, int index)
    {
        GEItem item = geItems.get(index);
        int cost = item.GetPrice();
        int balance = GetBalance(p.getName());
        if (item.GetSold())
        {
            p.sendMessage(ChatColor.RED + "This item appears to already be sold... Maybe next time.");
            return false;
        }
        else if (balance >= cost)
        {
            SetFunds(p.getName(), balance - cost); // I lose my money
            String owner = item.GetOwner(); // Find seller
            AddFunds(owner, cost); // Seller gets their money
            item.SetSold(); // Item is now sold so nobody makes mistake
            p.getInventory().addItem(item.GetItem()); // Add item to my inventory


            ItemMeta itemMeta = item.GetItem().getItemMeta();
            String itemName = itemMeta.getDisplayName();
            if (itemName == "") itemName = item.GetItem().getType().name();
            p.sendMessage(ChatColor.GREEN + "Successfully purchased " + itemName + "x" + item.GetItem().getAmount() + " from " + owner);

            Player op = Bukkit.getPlayer(owner);
            if (op != null)
            {
                op.sendMessage(ChatColor.GREEN + "Successfully sold " + itemName + "x" + item.GetItem().getAmount() + " to " + p.getName() + " for " + currency + cost);
            }
            return true;
        }
        else
        {
            p.sendMessage(ChatColor.RED + "Insufficient funds.");
            return false;
        }
    }

    public static void AddGEItem(GEItem item) { geItems.add(item); }

    public static void SetFunds(String payee, int amount)
    {
        playerBalances.replace(payee, amount);
    }

    public static int GetBalance(String name) { return playerBalances.get(name); }

    public static String GetCurrency() { return currency; }
    public static int GetPaymentTax() { return payTax; }

    public static List<GEItem> GetGE() { return geItems; }

    public static HashMap<String, Integer> GetBalances() { return playerBalances; }
    public static HashMap<Material, Integer> GetExchangeIndex() { return exchangeIndex; }
    public static servereconomy Get() { return plugin; }
}
