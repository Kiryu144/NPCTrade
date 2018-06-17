package net.andrasia.kiryu144.npctrade;

import net.andrasia.kiryu144.npctrade.commands.TradeNPC;
import net.andrasia.kiryu144.npctrade.traits.Trader;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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

        /* Register traits */
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(Trader.class));

        /* Register commands */
        this.getCommand("tradenpc").setExecutor(new TradeNPC());
    }

}
