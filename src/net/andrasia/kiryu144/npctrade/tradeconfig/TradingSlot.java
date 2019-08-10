package net.andrasia.kiryu144.npctrade.tradeconfig;

import dev.lone.itemsadder.api.ItemsAdder;
import net.andrasia.kiryu144.kiryulib.gui.GuiSlot;
import net.andrasia.kiryu144.kiryulib.gui.GuiView;
import net.andrasia.kiryu144.npctrade.Messages;
import net.andrasia.kiryu144.npctrade.NPCTrade;
import net.andrasia.kiryu144.npctrade.tradeconfig.Trade;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class TradingSlot extends GuiSlot {
    protected Trade trade;

    public TradingSlot(Trade trade){
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }

    @Override
    public ItemStack getItemStack() {
        return trade.getDisplayItem();
    }

    @Override
    public void onClick(GuiView view, Player clicker, ClickType clickType, ItemStack itemStack) {
        switch (clickType){
            case LEFT:          Buy(trade, clicker, false);  break;
            case SHIFT_LEFT:    Buy(trade, clicker, true);   break;
            case RIGHT:         Sell(trade, clicker, false); break;
            case SHIFT_RIGHT:   Sell(trade, clicker, true);  break;
        }
    }

    private static void Buy(Trade trade, Player p, boolean max){
        ItemStack item = trade.getItem().clone();
        int amount = (max) ? (int) (Math.floor(NPCTrade.economy.getBalance(p) / trade.getBuyPrice())) : 1;
        if(amount > item.getMaxStackSize()){
            amount = item.getMaxStackSize();
        }
        item.setAmount(amount);

        double cost = item.getAmount() * trade.getBuyPrice();

        if(NPCTrade.economy.getBalance(p) < cost){
            p.sendMessage(Messages.trade_buy_failure);
            return;
        }

        NPCTrade.economy.withdrawPlayer(p, cost);
        p.sendMessage(MessageFormat.format(Messages.trade_buy_success, amount, trade.getDisplayName(), Trade.FormatFunds((float) cost)));
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 3.0f, 0.5f);

        AddItemToPlayerInventorySafely(p, item);
    }

    private static void Sell(Trade trade, Player p, boolean max){
        ItemStack item = trade.getItem().clone();
        int slot = GetSlotMatchingItemStack(p, item);
        if(slot >= 0){
            ItemStack playerItem = p.getInventory().getItem(slot);
            int sellAmount = (max) ? playerItem.getAmount() : 1;

            NPCTrade.economy.depositPlayer(p, sellAmount * trade.getSellPrice());

            p.sendMessage(MessageFormat.format(Messages.trade_sell_success, sellAmount, trade.getDisplayName(), Trade.FormatFunds((float) sellAmount * trade.getSellPrice())));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 3.0f, 0.5f);
            playerItem.setAmount(playerItem.getAmount() - sellAmount);
        }
    }

    private static void AddItemToPlayerInventorySafely(Player p, ItemStack itemStack){
        HashMap<Integer, ItemStack> itemsLeft = p.getInventory().addItem(itemStack);

        for (Map.Entry<Integer, ItemStack> entry : itemsLeft.entrySet()) {
            ItemStack item = entry.getValue().clone();
            item.setAmount(entry.getKey());
            p.getLocation().getWorld().dropItem(p.getLocation(), item);
        }
    }

    private static int GetSlotMatchingItemStack(Player p, ItemStack itemStack){
        boolean isCustomItem = ItemsAdder.isCustomItem(itemStack);
        String customItemName = (isCustomItem) ? ItemsAdder.getCustomItemName(itemStack) : "";

        for(int slot = 0; slot < 36; ++slot){
            ItemStack itemInsideSlot = p.getInventory().getItem(slot);

            if(itemInsideSlot == null){
                continue;
            }

            if(isCustomItem && customItemName.equalsIgnoreCase(ItemsAdder.getCustomItemName(itemInsideSlot))){
                return slot;
            }

            if(itemInsideSlot.isSimilar(itemStack)){
                return slot;
            }
        }

        return -1;
    }
}
