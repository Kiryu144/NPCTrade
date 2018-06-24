package net.andrasia.kiryu144.npctrade.tradeconfig;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Trade {
    private ItemStack item;
    private ItemStack displayItem;
    private float buyPrice;
    private float sellPrice;

    /* When buy/sell is negative, it's not possible to sell/buy a specific item. */
    public Trade(ItemStack item, float buyPrice, float sellPrice){
        this.item = item;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.displayItem = generateDisplayItem();
    }

    private ItemStack generateDisplayItem() {
        ItemStack item = new ItemStack(this.item.getType(), 1, this.item.getDurability());
        ItemMeta meta = item.getItemMeta();

        String buySellMsg = "";
        if(buyPrice >= 0){
            buySellMsg += String.format("§aBuy: §b%.2f\n", buyPrice);
        }
        if(sellPrice >= 0){
            buySellMsg += String.format("§aSell: §b%.2f\n", sellPrice);
        }

        List<String> lore = new ArrayList<>();
        lore.add(buySellMsg);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getDisplayItem() {
        return displayItem.clone();
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public float getSellPrice() {
        return sellPrice;
    }
}
