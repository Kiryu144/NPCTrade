package net.andrasia.kiryu144.npctrade.tradeconfig;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class TradeConfigManager implements Listener {
    private static HashMap<String, TradeConfig> tradeConfigs = new HashMap<>();

    public TradeConfigManager() {}

    public static void addTradeConfig(String name, TradeConfig config){
        tradeConfigs.put(name.toLowerCase(), config);
    }

    public static TradeConfig getTradeConfig(String name){
        return tradeConfigs.get(name.toLowerCase());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        /* Basic filtering, hoping for some performance gainzzz */
        if(event.getClickedInventory().getType() == InventoryType.CHEST){
            Inventory topInventory = event.getWhoClicked().getOpenInventory().getTopInventory();

        }
    }

}
