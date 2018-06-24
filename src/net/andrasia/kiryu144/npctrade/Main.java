package net.andrasia.kiryu144.npctrade;

import net.andrasia.kiryu144.kiryucore.KiryuCore;
import net.andrasia.kiryu144.kiryucore.config.YamlConfig;
import net.andrasia.kiryu144.kiryucore.console.KiryuLogger;
import net.andrasia.kiryu144.kiryucore.util.KiryuSimpleSerialization;
import net.andrasia.kiryu144.npctrade.commands.TradeNPC;
import net.andrasia.kiryu144.npctrade.tradeconfig.Trade;
import net.andrasia.kiryu144.npctrade.tradeconfig.TradeConfig;
import net.andrasia.kiryu144.npctrade.tradeconfig.TradeConfigManager;
import net.andrasia.kiryu144.npctrade.traits.Trader;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.io.File;

import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Plugin instance;
    public static Economy economy;

    public static YamlConfig messageConfig;

    public void onEnable() {
        instance = this;

        /* Initialize KiryuCore */
        KiryuCore.init(this, ChatColor.AQUA);

        /* Initialize Citizens2 API */
        if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled!");
            getServer().getPluginManager().disablePlugin(this);
        }

        /* Initialize Vault API */
        if(getServer().getPluginManager().getPlugin("Vault") == null || !getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            getLogger().log(Level.SEVERE, "Vault not found or not enabled!");
            getServer().getPluginManager().disablePlugin(this);
        }
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        economy = economyProvider.getProvider();


        /* Register traits */
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(Trader.class));

        /* Register commands */
        this.getCommand("tradenpc").setExecutor(new TradeNPC());

        /* Register EventHandlers */
        this.getServer().getPluginManager().registerEvents(new TradeConfigManager(), this);

        /* Config */
        //messages
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("trade_buy_success", "§c%s §bhave been withdrawn from your account!");
        yamlConfiguration.set("trade_sell_success", "§2%s §bhave been deposited to your account!");
        messageConfig = new YamlConfig("messages.yml");
        if(messageConfig.copyDefaults(yamlConfiguration)){
            messageConfig.save();
        }

        //tradeconfigs
        this.parseTradeConfigs();


        /* Testing purposes only */
        TradeConfig config = new TradeConfig();
        config.addTrade(new Trade(new ItemStack(Material.MELON), 20, 10), 0);
        config.addTrade(new Trade(new ItemStack(Material.PUMPKIN), 220, 10), 1);
        config.addTrade(new Trade(new ItemStack(Material.DIAMOND), 22350, 1140), 4);
        config.addTrade(new Trade(new ItemStack(Material.EMERALD), 1520, 1051), 5);
        config.generateInventory();
        TradeConfigManager.addTradeConfig("testing", config);
    }

    public void parseTradeConfigs() {
        File dataFolder = new File(this.getDataFolder() + "\\tradeconfigs");
        if(!dataFolder.exists()){
            dataFolder.mkdirs();
        }else{
            for(File file : dataFolder.listFiles()){
                if(file.getName().endsWith(".yml")){
                    YamlConfiguration configuration = new YamlConfiguration();
                    try {
                        configuration.load(file);
                    } catch (Exception ignored) { /* pass */}

                    TradeConfig config = new TradeConfig();
                    for(String resultItem : configuration.getKeys(false)){
                        ItemStack item = KiryuSimpleSerialization.getSimpleItemStack(resultItem);
                        int slot = configuration.getInt(String.format("%s.slot", resultItem));
                        float buyPrice = (float) configuration.getDouble(String.format("%s.buy", resultItem));
                        float sellPrice = (float) configuration.getDouble(String.format("%s.sell", resultItem));
                        config.addTrade(new Trade(item, buyPrice, sellPrice), slot);
                    }
                    config.generateInventory();
                    TradeConfigManager.addTradeConfig(file.getName().replace(".yml", ""), config);
                }
            }
        }
    }

    public static String formatFunds(double amount){
        return String.format("%.2f", amount);
    }

}




















