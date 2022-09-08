package me.yoshirouuu.yoshirouuuseconomy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import javax.naming.Name;

public class GrandExchangeClick implements Listener {

    @EventHandler
    public void onInvClick(InventoryClickEvent event)
    {
        if (event.getView().getTitle().equalsIgnoreCase("Grand Exchange"))
        {
            if (event.getView().getTopInventory().equals(event.getClickedInventory()))
            {
                Player p = (Player) event.getWhoClicked();
                ItemStack currentItem = event.getCurrentItem();
                if (currentItem == null)
                {
                    System.out.println("current item null: 28");
                    event.setCancelled(true);
                    return;
                }
                ItemMeta meta = currentItem.getItemMeta();
                if (meta == null)
                {
                    System.out.println("meta null: 35");
                    event.setCancelled(true);
                    return;
                }
                String name = meta.getDisplayName();
                int pageNo = yoshirouuuseconomy.currentGEPage;

                if (name.equals(" "))
                {
                    return;
                }
                else if (name.contains("Page "))
                {
                    return;
                }
                else if (name.equalsIgnoreCase("<-- Prev"))
                {
                    pageNo--;
                    p.performCommand("ge " + pageNo);
                    return;
                }
                else if (name.equalsIgnoreCase("Next -->"))
                {
                    pageNo++;
                    p.performCommand("ge " + pageNo);
                    return;
                }

                NamespacedKey key = new NamespacedKey(yoshirouuuseconomy.Get(), "Index");
                PersistentDataContainer data = meta.getPersistentDataContainer();
                if (data.has(key, PersistentDataType.INTEGER))
                {
                    int index = data.get(key, PersistentDataType.INTEGER);
                    yoshirouuuseconomy.TransactionGE(p, index);
                    p.closeInventory();
                }


            }
            event.setCancelled(true);
        }
    }
}
