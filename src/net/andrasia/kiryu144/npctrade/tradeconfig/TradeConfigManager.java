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

    public void trade(boolean buy, boolean allAtOnce, Trade trade, Player p){
        double funds = Main.economy.getBalance(p);
        double maxBuyItems = Math.floor(funds / trade.getBuyPrice()); /* Maximum amount of items the player can buy with his current funds */
        ItemStack item = trade.getItem();

        if(buy && maxBuyItems > 0){
            item.setAmount(1);
            if(allAtOnce){
                item.setAmount((maxBuyItems <= 64) ? (int) maxBuyItems : 64);
            }
            p.getInventory().addItem(item);
            Main.economy.withdrawPlayer(p, item.getAmount()*trade.getBuyPrice());
            p.sendMessage(String.format(Main.messageConfig.getYamlConfiguration().getString("trade_buy_success"), Main.formatFunds(item.getAmount()*trade.getBuyPrice())));
        }else if(!buy){ /* Sell */
            for(int i = 0; i < p.getInventory().getSize(); i++){
                ItemStack pitem = p.getInventory().getItem(i);
                if(pitem != null && pitem.isSimilar(item)){
                    double sellAmount = (allAtOnce) ? pitem.getAmount() : 1;
                    pitem.setAmount(pitem.getAmount() - (int) sellAmount);
                    Main.economy.depositPlayer(p, sellAmount*trade.getSellPrice());
                    p.sendMessage(String.format(Main.messageConfig.getYamlConfiguration().getString("trade_sell_success"), Main.formatFunds(sellAmount * trade.getSellPrice())));
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory topInventory = event.getWhoClicked().getOpenInventory().getTopInventory();
        TradeConfig config = tradeConfigInvis.get(topInventory);
        if(config != null){ /* Found config involving current inventory */
            event.setCancelled(true);
            if(event.getRawSlot() < config.getInventory().getSize()){ /* Slot is inside the trading thingy */
                Trade trade = config.getTrade(event.getRawSlot());
                if(trade != null) { /* Found trade that was clicked on */
                    if(event.isLeftClick() || event.isRightClick()){
                        this.trade(event.isLeftClick(), event.isShiftClick(), trade, (Player) event.getWhoClicked());
                    }
                }
            }
        }
    }

}















