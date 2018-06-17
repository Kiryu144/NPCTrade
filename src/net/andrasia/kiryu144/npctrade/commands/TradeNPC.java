package net.andrasia.kiryu144.npctrade.commands;

import net.andrasia.kiryu144.npctrade.traits.Trader;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TradeNPC implements CommandExecutor {
    /* /tradenpc set <tradeconfig> */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 2 && args[0].equalsIgnoreCase("set")) {
            String config = args[1].toLowerCase();
            NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);

            if(npc == null){ //No npc selected
                sender.sendMessage("§cNo NPC selected! Select with §6/npc select");
                return true;
            }

            if(!npc.hasTrait(Trader.class)){ //Does not have Trader trait
                sender.sendMessage("§cTrader Trait not found! §aAttaching ..");
                npc.addTrait(Trader.class);
                sender.sendMessage("§aTrader Trait is now attached!");
            }

            /* Settting the trade name */
            npc.getTrait(Trader.class).setTradeName(config);

            sender.sendMessage(String.format("§b%s\' §atrader type is now set to §6'%s'!", npc.getName(), config));
            return true;
        }

        /* If no proper command is given */
        sender.sendMessage("§bUsage: §6/tradenpc set <tradeconfig>");
        return true;
    }
}
