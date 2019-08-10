package net.andrasia.kiryu144.npctrade.tradeconfig;

import net.andrasia.kiryu144.kiryulib.gui.GuiSlot;
import net.andrasia.kiryu144.kiryulib.gui.GuiView;
import net.andrasia.kiryu144.kiryulib.gui.SwitchGuiSlot;
import net.andrasia.kiryu144.npctrade.Messages;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class TradePage {
    public static HashMap<String, TradePage> instances = new HashMap<>();
    private GuiView gui;

    public TradePage(String name){
        gui = new GuiView(9*6);
        instances.put(name, this);
    }

    public void setTrade(Trade trade){
        gui.setSlot(trade.getSlot(), new TradingSlot(trade));
    }

    public void setTrade(int slot, Trade trade){
        gui.setSlot(slot, new TradingSlot(trade));
    }

    public void setTrade(int x, int y, Trade trade){
        gui.setSlot(y*9 + x, new TradingSlot(trade));
    }

    public void setGuiItem(int slot, ItemStack item){
        gui.setSlot(0, new GuiSlot(item));
    }

    public void setInformationItem(int slot, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Messages.trader_information);
        item.setItemMeta(meta);
        gui.setSlot(slot, new GuiSlot(item));
    }

    public void addPageButton(int slot, String page, ItemStack itemStack){
        gui.setSlot(slot, new SwitchTradeSlot(itemStack, page));
    }

    public void open(Player p){
        gui.open(p);
        p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 3.0f, 0.5f);
    }

    public void update() {
        gui.update();
    }
}
