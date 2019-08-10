package net.andrasia.kiryu144.npctrade.tradeconfig;

import dev.lone.itemsadder.api.ItemsAdder;
import net.andrasia.kiryu144.kiryulib.KiryuLib;
import net.andrasia.kiryu144.npctrade.NPCTrade;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Trade {
    private ItemStack item;
    private ItemStack displayItem;
    private String displayName;
    private int slot;
    private float buyPrice;
    private float sellPrice;

    public Trade(ConfigurationSection section, String displayName){
        this.displayName = displayName;

        item = KiryuLib.DeserializeItemStack(section.getConfigurationSection("result"));
        buyPrice = (float) section.getDouble("price.buy", 0.0d);
        sellPrice = (float) section.getDouble("price.sell", 0.0d);
        slot = section.getInt("slot", 0);

        this.displayItem = generateDisplayItem();
    }

    public static String FormatFunds(float value){
        return String.format("%.2f", value);
    }

    public static String CreatePriceDescriptor(float buyprice, float sellprice){
        if(buyprice > 0 && sellprice > 0){
            return String.format("§r(§2%s§r | §e%s§r)", FormatFunds(buyprice), FormatFunds(sellprice));
        }

        if(buyprice > 0){
            return String.format("§r(§2%s§r)", FormatFunds(buyprice));
        }

        if(sellprice > 0){
            return String.format("§r(§e%s§r)", FormatFunds(sellprice));
        }

        return "()";
    }

    protected ItemStack generateDisplayItem() {
        ItemStack item = this.item.clone();

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r" + displayName + " " + CreatePriceDescriptor(buyPrice, sellPrice));
        item.setItemMeta(meta);

        return item;
    }

    public int getSlot() {
        return slot;
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

    public boolean isBuyable() {
        return this.buyPrice > 0;
    }

    public boolean isSellable() {
        return this.sellPrice > 0;
    }

    public String getDisplayName() {
        return displayName;
    }
}
