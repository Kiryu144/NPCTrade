package net.andrasia.kiryu144.npctrade.commands;

import net.andrasia.kiryu144.npctrade.NPCTrade;
import net.andrasia.kiryu144.npctrade.traits.Trader;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TradeNPC implements CommandExecutor {

    private static NPC GetTradeNPC(CommandSender sender){
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);

        if(npc == null){ //No npc selected
            sender.sendMessage("§cNo NPC selected! Select with §6/npc select");
            return null;
        }

        if(!npc.hasTrait(Trader.class)){ //Does not have Trader trait
            sender.sendMessage("§cTrader Trait not found! §aAttaching ..");
            npc.addTrait(Trader.class);
            sender.sendMessage("§aTrader Trait is now attached!");
        }

        return npc;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 2 && args[0].equalsIgnoreCase("add")) { /* /tradenpc add <tradeconfig> */
            String config = args[1].toLowerCase();
            NPC npc = GetTradeNPC(sender);

            if(npc == null){
                return true;
            }

            /* Adding the trade name */
            npc.getTrait(Trader.class).addTrade(config);

            sender.sendMessage(String.format("§aAdded config '§6%s§a'.", config));
            return true;
        }

        if(args.length == 2 && args[0].equalsIgnoreCase("remove")) { /* /tradenpc remove <tradeconfig> */
            String config = args[1].toLowerCase();
            NPC npc = GetTradeNPC(sender);

            if(npc == null){
                return true;
            }

            /* Removing the trade name */
            npc.getTrait(Trader.class).removeTrade(config);

            sender.sendMessage(String.format("§aRemoved config '§6%s§a'.", config));
            return true;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("clear")) { /* /tradenpc clear*/
            NPC npc = GetTradeNPC(sender);

            if(npc == null){
                return true;
            }

            /* Removing the trade name */
            npc.getTrait(Trader.class).clearTrades();

            sender.sendMessage("§aRemoved all configs.");
            return true;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("list")) { /* /tradenpc list*/
            NPC npc = GetTradeNPC(sender);

            if(npc == null){
                return true;
            }

            /* Listing the trades */
            sender.sendMessage("§aConfigs:");
            for(String trade : npc.getTrait(Trader.class).getTrades()){
                sender.sendMessage(String.format("§6-> '§2%s§6'", trade));
            }

            return true;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("reload")){ /* /tradeconfig reload */
            NPCTrade.instance.reloadConfig();
            NPCTrade.instance.getLogger().info("Reloaded configs.");
            if(sender instanceof Player){
                sender.sendMessage("§2§lReloaded configs!");
            }
            return true;
        }

        /* If no proper command is given */
        sender.sendMessage("§bCommands:");
        sender.sendMessage("§bUsage: §6/tradenpc add <tradeconfig>");
        sender.sendMessage("§bUsage: §6/tradenpc remove <tradeconfig>");
        sender.sendMessage("§bUsage: §6/tradenpc clear");
        sender.sendMessage("§bUsage: §6/tradenpc list");
        sender.sendMessage("§bUsage: §6/tradenpc reload");
        return true;
    }
}
