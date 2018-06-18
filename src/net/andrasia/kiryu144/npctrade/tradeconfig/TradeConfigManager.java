package net.andrasia.kiryu144.npctrade.tradeconfig;

import org.bukkit.event.Listener;

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

}
