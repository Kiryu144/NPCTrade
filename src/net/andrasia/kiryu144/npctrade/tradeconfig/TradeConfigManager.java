package net.andrasia.kiryu144.npctrade.tradeconfig;

import net.andrasia.kiryu144.npctrade.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class TradeConfigManager implements Listener {
    private static HashMap<String, TradeConfig> tradeConfigs = new HashMap<>();
    private static HashMap<Inventory, TradeConfig> tradeConfigInvis = new HashMap<>();

    public TradeConfigManager() {}

    public static void addTradeConfig(String name, TradeConfig config){
        if(tradeConfigs.containsKey(name.toLowerCase())){
            Main.instance.getLogger().warning("Tradeconfig " + name.toLowerCase() + " already exists. Overwriting!");
        }
        tradeConfigs.put(name.toLowerCase(), config);
        tradeConfigInvis.put(config.getInventory(), config);
    }

    public static TradeConfig getTradeConfig(String name){
        return tradeConfigs.get(name.toLowerCase());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory topInventory = event.getWhoClicked().getOpenInventory().getTopInventory();
        TradeConfig config = tradeConfigInvis.get(topInventory);
        if(config != null){
            event.setCancelled(true);
        }
    }
}
