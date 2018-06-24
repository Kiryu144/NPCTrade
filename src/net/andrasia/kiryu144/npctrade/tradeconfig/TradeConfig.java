package net.andrasia.kiryu144.npctrade.tradeconfig;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class TradeConfig {
    private ArrayList<Trade> trades;
    private Inventory inventory;

    public TradeConfig() {
        trades = new ArrayList<>();

        /* Initialize with size of big chest */
        for(int i = 0; i < 6*9; i++) {
            trades.add(null);
        }

        /* Create fitting inventory */
        inventory = Bukkit.createInventory(null, 6*9);
    }

    public void addTrade(Trade trade, int slot){
        trades.set(slot, trade);
    }

    public void generateInventory(){
        inventory = Bukkit.createInventory(null, 6*9, "");
        for(int i = 0; i < 6*9; i++) {
            Trade trade = trades.get(i);
            if(trade != null) {
                inventory.setItem(i, trade.getDisplayItem());
            }
        }
    }

    public void openInventory(Player p){
        p.openInventory(inventory);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Trade getTrade(int rawSlot){
        return trades.get(rawSlot);
    }
}
