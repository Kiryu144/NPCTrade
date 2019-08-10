package net.andrasia.kiryu144.npctrade.traits;

import net.andrasia.kiryu144.npctrade.tradeconfig.TradePage;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;

@TraitName("trader")
public class Trader extends Trait {
    protected ArrayList<String> trades = new ArrayList<>();

    public Trader() {
        super("trader");
    }

    public void addTrade(String trade){
        trades.add(trade);
    }

    public void removeTrade(String trade){
        trades.remove(trade);
    }

    public void clearTrades(){
        trades.clear();
    }

    public ArrayList<String> getTrades(){
        return trades;
    }

    @Override
    public void load(DataKey key) {
        trades = (ArrayList<String>) key.getRaw("trades");
    }

    @Override
    public void save(DataKey key) {
        key.setRaw("trades", trades);
    }

    @EventHandler
    public void click(NPCRightClickEvent event){
        if(event.getNPC() != this.getNPC()){
            return;
        }

        if(trades.isEmpty()){
            event.getClicker().sendMessage("§cNo trade config! Add using §b/tradenpc add <config>");
            return;
        }

        if(!TradePage.instances.containsKey(trades.get(0))){
            event.getClicker().sendMessage("§cUnknown trading config '§2" + trades.get(0) + "§c'. Remove using §b/tradenpc remove <config>");
            return;
        }

        TradePage config = TradePage.instances.get(trades.get(0));
        config.open(event.getClicker());
    }
}























