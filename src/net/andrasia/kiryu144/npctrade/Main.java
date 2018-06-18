package net.andrasia.kiryu144.npctrade;

import net.andrasia.kiryu144.npctrade.commands.TradeNPC;
import net.andrasia.kiryu144.npctrade.tradeconfig.Trade;
import net.andrasia.kiryu144.npctrade.tradeconfig.TradeConfig;
import net.andrasia.kiryu144.npctrade.tradeconfig.TradeConfigManager;
import net.andrasia.kiryu144.npctrade.traits.Trader;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Plugin instance;

    public void onEnable() {
        instance = this;

        /* Initialize Citizens2 API */
        if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
        }

        /* Initialize Vault API */
        if(getServer().getPluginManager().getPlugin("Vault") == null || !getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            getLogger().log(Level.SEVERE, "Vault not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
        }
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        /* Register traits */
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(Trader.class));

        /* Register commands */
        this.getCommand("tradenpc").setExecutor(new TradeNPC());

        /* Testing purposes only */
        TradeConfig config = new TradeConfig();
        config.addTrade(new Trade(new ItemStack(Material.MELON), 20, 10), 0);
        config.addTrade(new Trade(new ItemStack(Material.PUMPKIN), 220, 10), 1);
        config.addTrade(new Trade(new ItemStack(Material.DIAMOND), 22350, 1140), 4);
        config.addTrade(new Trade(new ItemStack(Material.EMERALD), 1520, 1051), 5);
        config.generateInventory();
        TradeConfigManager.addTradeConfig("testing", config);
    }

}




















