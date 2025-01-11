package me.conormcdr.servereconomy;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GEItem {
    private ItemStack item;
    private String owner;
    private int price;
    private boolean sold;
    private int minutesLeft;

    public GEItem(ItemStack _item, int _price, String _owner, int _minutesLeft) {
        item = _item;
        price = _price;
        owner = _owner;
        minutesLeft = _minutesLeft;
        sold = false;
    }

    public GEItem(GEItem another) // copy constructor
    {
        this.item = new ItemStack(another.item);
        this.price = new Integer(another.price);
        this.owner = another.owner;
        this.minutesLeft = new Integer(another.minutesLeft);
        this.sold = new Boolean(another.sold);
    }

    public ItemStack GetItem() { return item; }
    public String GetOwner() { return owner; }
    public int GetPrice() { return price; }
    public int GetTime() { return minutesLeft; }
    public boolean GetSold() { return sold; }
    public void SetSold() { sold = true; }

    public boolean ReduceTimeLeft()
    {
        minutesLeft--;
        if (minutesLeft <= 0)
        {
            return true;
        }
        return false;
    }
}
