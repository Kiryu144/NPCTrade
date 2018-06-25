package net.andrasia.kiryu144.npctrade.tradeconfig;

import net.andrasia.kiryu144.npctrade.Main;
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

        List<String> lore = new ArrayList<>();
        if(buyPrice >= 0){
            lore.add(String.format(Main.messageConfig.getYamlConfiguration().getString("trade_buy_price"), Main.formatFunds(buyPrice)));
        }
        if(sellPrice >= 0){
            lore.add(String.format(Main.messageConfig.getYamlConfiguration().getString("trade_sell_price"), Main.formatFunds(sellPrice)));
        }
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
