package net.andrasia.kiryu144.npctrade.tradeconfig;

import net.andrasia.kiryu144.kiryulib.gui.GuiSlot;
import net.andrasia.kiryu144.kiryulib.gui.GuiView;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SwitchTradeSlot extends GuiSlot {
    protected String destination;

    public SwitchTradeSlot(String destination){
        this.destination = destination;
    }

    public SwitchTradeSlot(ItemStack itemStack, String destination){
        super(itemStack);
        this.destination = destination;
    }

    @Override
    public void onClick(GuiView view, Player clicker, ClickType clickType, ItemStack itemStack) {
        TradePage.instances.get(destination).open(clicker);
    }
}
