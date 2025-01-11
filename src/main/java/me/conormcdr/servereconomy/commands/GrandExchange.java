package me.conormcdr.servereconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.conormcdr.servereconomy.GEItem;
import me.conormcdr.servereconomy.servereconomy;

import java.util.ArrayList;
import java.util.List;

public class GrandExchange implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            if (p.hasPermission("seconomy.ge"))
            {
                int pageNo = 0;
                if (args.length > 0)
                {
                    if (args[0] != null)
                    {
                        try {
                            pageNo = Integer.parseInt(args[0]);
                        } catch (NumberFormatException e)
                        {
                            pageNo = 0;
                        }
                    }
                }
                if (pageNo < 0) pageNo = 0;

                Inventory gui = Bukkit.createInventory(p, 54, "Grand Exchange");

                String currency = servereconomy.GetCurrency();

                // PAGE FILLER ///////////////////////////////////////////////////
                ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta fillermeta = filler.getItemMeta();
                fillermeta.setDisplayName("");
                filler.setItemMeta(fillermeta);
                /////////////////////////////////////////////////////////////////

                ItemStack[] slots = new ItemStack[54];

                for (int i = 0; i < slots.length; i++)
                {
                    slots[i] = filler;
                }

                List<GEItem> list = servereconomy.GetGE();

                int index = 10; // 2nd row 2nd col
                int listIndex = 0;
                if (pageNo - 1 >= 0)
                {
                    if (28 * pageNo >= list.size() - 1)
                    {
                        if ((28 * pageNo) - 28 >= list.size() - 1)
                        {
                            int lastpage = (int) ((float)list.size() / 28f);
                            pageNo = lastpage;
                        }
                        else
                        {
                            pageNo--;
                        }
                    }
                    listIndex += 28 * pageNo;

                }

                // PAGE NUMBER & NAV ///////////////////////////////////////
                ItemStack page = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta pageMeta = page.getItemMeta();
                pageMeta.setDisplayName("Page " + pageNo);
                page.setItemMeta(pageMeta);
                slots[4] = page;

                ItemStack pagenext = new ItemStack(Material.LIME_CONCRETE);
                ItemMeta pagenextMeta = pagenext.getItemMeta();
                pagenextMeta.setDisplayName("Next -->");
                pagenext.setItemMeta(pagenextMeta);
                slots[50] = pagenext;

                ItemStack pageprev = new ItemStack(Material.LIME_CONCRETE);
                ItemMeta pageprevMeta = pageprev.getItemMeta();
                pageprevMeta.setDisplayName("<-- Prev");
                pageprev.setItemMeta(pageprevMeta);
                slots[48] = pageprev;
                ////////////////////////////////////////////////////////////

                if (list.size() > 0)
                {
                    outerloop:
                    for (int i = 0; i < 4; i++)
                    {
                        for (int j = 0; j < 7; j++)
                        {
                            GEItem geitem = new GEItem(list.get(listIndex));
                            slots[index] = geitem.GetItem();
                            if (geitem.GetSold()) slots[index] = new ItemStack(Material.BARRIER);
                            ItemMeta meta = slots[index].getItemMeta();
                            if (geitem.GetSold()) meta.setDisplayName(ChatColor.RED + "SOLD");
                            meta.getPersistentDataContainer().set(new NamespacedKey(servereconomy.Get(),
                                    "Index"), PersistentDataType.INTEGER, listIndex);
                            List<String> lore = new ArrayList<>();
                            lore.add("Owner: " + geitem.GetOwner());
                            if (geitem.GetTime() >= 60)
                                lore.add("Time Remaining: " + (int)((float)geitem.GetTime() / 60f) + "H");
                            else
                                lore.add("Time Remaining: " + geitem.GetTime() + "M");
                            lore.add("Cost: " + currency + geitem.GetPrice());
                            meta.setLore(lore);
                            slots[index].setItemMeta(meta);

                            if (listIndex + 1 == list.size()) break outerloop;
                            listIndex++;
                            index++; // for each of the columns
                        }
                        index += 2; // next row 2nd col
                    }
                }


                gui.setContents(slots);

                servereconomy.currentGEPage = pageNo;

                p.openInventory(gui);
            }
        }

        return true;
    }

}
