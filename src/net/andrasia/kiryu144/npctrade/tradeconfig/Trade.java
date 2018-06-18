package net.andrasia.kiryu144.npctrade.tradeconfig;

import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Trade {
    private ItemStack item;
    private float buyPrice;
    private float sellPrice;

    public Trade(ItemStack item, float buyPrice, float sellPrice){
        this.item = item;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public ItemStack getItem() {
        return item;
    }

    public ItemStack getShopItem() {
        ItemStack item = new ItemStack(this.item.getType(), 1, this.item.getDurability());
        ItemMeta meta = item.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add(String.format("§aBuy: §b%.2f §aSell: §b%.2f", buyPrice, sellPrice));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public float getSellPrice() {
        return sellPrice;
    }
}
