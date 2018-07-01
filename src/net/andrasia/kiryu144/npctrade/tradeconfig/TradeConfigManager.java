package net.andrasia.kiryu144.npctrade.tradeconfig;

import net.andrasia.kiryu144.kiryucore.console.KiryuLogger;
import net.andrasia.kiryu144.npctrade.Main;
import net.milkbowl.vault.Vault;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import net.andrasia.kiryu144.kiryucore.util.InventoryUtils;

import java.util.HashMap;

public class TradeConfigManager implements Listener {
    private static HashMap<String, TradeConfig> tradeConfigs = new HashMap<>();
    private static HashMap<Inventory, TradeConfig> tradeConfigInvis = new HashMap<>();

    public TradeConfigManager() {}

    public static void addTradeConfig(String name, TradeConfig config){
        if(tradeConfigs.containsKey(name.toLowerCase())){
            KiryuLogger.error("Tradeconfig " + name.toLowerCase() + " already exists. Overwriting!");
        }else{
            KiryuLogger.info(String.format("Tradeconfig §b%s§r added", name));
        }
        tradeConfigs.put(name.toLowerCase(), config);
        tradeConfigInvis.put(config.getInventory(), config);
    }

    public static TradeConfig getTradeConfig(String name){
        return tradeConfigs.get(name.toLowerCase());
    }

    public static void buy(Trade trade, Player p, boolean max){
        ItemStack item = trade.getItem().clone();
        item.setAmount((max) ? (int) (Math.floor(Main.economy.getBalance(p) / trade.getBuyPrice())) : 1); //Sets amount to 1, except max is true, then the maximum amount the player has money for
        if(item.getAmount() > 64){
            item.setAmount(64);
        }

        Main.economy.withdrawPlayer(p, item.getAmount() * trade.getBuyPrice());
        p.sendMessage(String.format(Main.messageConfig.getYamlConfiguration().getString("trade_buy_success"), Main.formatFunds(item.getAmount() * trade.getBuyPrice())));

        InventoryUtils.safelyAddItemStack(p, item);
    }

    public static void sell(Trade trade, Player p, boolean max){
        ItemStack item = trade.getItem().clone();
        int slot = InventoryUtils.getSlotMatchingItemStack(p.getInventory(), item);
        if(slot >= 0){
            ItemStack playerItem = p.getInventory().getItem(slot);
            int sellAmount = (max) ? playerItem.getAmount() : 1;

            Main.economy.depositPlayer(p, sellAmount * trade.getSellPrice());
            p.sendMessage(String.format(Main.messageConfig.getYamlConfiguration().getString("trade_sell_success"), Main.formatFunds(sellAmount * trade.getSellPrice())));
            playerItem.setAmount(playerItem.getAmount() - sellAmount);

        }
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event){
        Inventory topInventory = event.getWhoClicked().getOpenInventory().getTopInventory();
        TradeConfig config = tradeConfigInvis.get(topInventory);
        if(config != null){ /* Found config involving current inventory */
            event.setCancelled(true);
            if(event.getRawSlot() < config.getInventory().getSize()){ /* Slot is inside the trading thingy */
                Trade trade = config.getTrade(event.getRawSlot());
                if(trade != null) { /* Found trade that was clicked on */
                    if(event.getClick() == ClickType.MIDDLE){ /* Trying to prevent some bug where the inventory of the player gets spammes with items */
                        return;
                    }
                    if(event.isLeftClick() && trade.isBuyable()){
                        buy(trade, (Player) event.getWhoClicked(), event.isShiftClick());
                    }else if(event.isRightClick() && trade.isSellable()){
                        sell(trade, (Player) event.getWhoClicked(), event.isShiftClick());
                    }
                }
            }
        }
    }

    public static void dump() {
        tradeConfigs.clear();
        tradeConfigInvis.clear();
    }

}















