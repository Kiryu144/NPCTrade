package net.andrasia.kiryu144.npctrade;

import net.andrasia.kiryu144.kiryulib.KiryuLib;
import net.andrasia.kiryu144.npctrade.commands.TradeNPC;
import net.andrasia.kiryu144.npctrade.tradeconfig.Trade;
import net.andrasia.kiryu144.npctrade.tradeconfig.TradePage;
import net.andrasia.kiryu144.npctrade.traits.Trader;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import java.util.logging.Level;

public class NPCTrade extends JavaPlugin {

    public static Plugin instance;
    public static Economy economy;

    public void onEnable() {
        instance = this;

        /* Initialize Citizens2 API */
        if(getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* Initialize Vault API */
        if(getServer().getPluginManager().getPlugin("Vault") == null || !getServer().getPluginManager().getPlugin("Vault").isEnabled()) {
            getLogger().log(Level.SEVERE, "Vault not found or not enabled!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);

        if(economyProvider == null){
            getLogger().log(Level.SEVERE, "No vault economy provider was found.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        economy = economyProvider.getProvider();

        /* Register traits */
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(Trader.class));

        /* Register commands */
        this.getCommand("tradenpc").setExecutor(new TradeNPC());

        /* Config */
        reloadConfigs();
    }

    public void reloadConfigs() {
        /* Messages */
        Messages.trade_buy_success = getConfig().getString("messages.buy.success");
        Messages.trade_buy_failure = getConfig().getString("messages.buy.failure");
        Messages.trade_sell_success = getConfig().getString("messages.sell.success");
        Messages.trade_sell_failure = getConfig().getString("messages.sell.failure");
        Messages.trader_information = getConfig().getStringList("messages.info");

        /* Trade configs */
        this.parseTradeConfigs();
    }

    public void parseTradeConfigs() {
        File dataFolder = new File(this.getDataFolder() + File.separator + "tradeconfigs");
        if(!dataFolder.exists()){
            dataFolder.mkdirs();
        }else{
            for(File file : dataFolder.listFiles()){
                if(file.getName().endsWith(".yml")){
                    YamlConfiguration configuration = new YamlConfiguration();
                    try {
                        configuration.load(file);
                    } catch (Exception ignored) {
                        getLogger().warning(String.format("Could not parse tradeconfig §e%s", file.getName()));
                        continue;
                    }

                    TradePage page = new TradePage(file.getName().replace(".yml", ""));

                    page.setGuiItem(configuration.getInt("settings.custom_gui_item.slot"), KiryuLib.DeserializeItemStack(configuration.getConfigurationSection("settings.custom_gui_item.item")));
                    page.setInformationItem(configuration.getInt("settings.information_item.slot"), KiryuLib.DeserializeItemStack(configuration.getConfigurationSection("settings.information_item.item")));

                    if(configuration.contains("settings.link")) {
                        for (String key : configuration.getConfigurationSection("settings.link").getKeys(false)) {
                            int slot = configuration.getInt("settings.link." + key + ".slot");
                            page.addPageButton(slot, key, KiryuLib.DeserializeItemStack(configuration.getConfigurationSection("settings.link." + key + ".item")));
                        }
                    }

                    for(String sectionKey : configuration.getConfigurationSection("trades").getKeys(false)){
                        ConfigurationSection section = configuration.getConfigurationSection("trades." + sectionKey);
                        page.setTrade(new Trade(section, sectionKey));
                    }
                    page.update();
                }
            }
        }
    }
}




















