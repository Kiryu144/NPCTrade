package net.andrasia.kiryu144.npctrade.traits;

import net.andrasia.kiryu144.npctrade.Main;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.event.EventHandler;

@TraitName("trader")
public class Trader extends Trait {
    protected String tradeName = null;

    public Trader() {
        super("trader");
    }

    public void setTradeName(String trade){
        tradeName = trade;
    }

    public void load(DataKey key) {
        tradeName = key.getString("tradeName");
    }

    public void save(DataKey key) {
        key.setString("tradeName", tradeName);
    }

    @EventHandler
    public void click(NPCRightClickEvent event){
        if(event.getNPC() == this.getNPC()){
            event.getClicker().sendMessage(String.valueOf(tradeName));
        }
    }
}























