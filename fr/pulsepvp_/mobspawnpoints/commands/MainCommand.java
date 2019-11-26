package pulsepvp_.mobspawnpoints.commands;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import pulsepvp_.mobspawnpoints.Main;
import pulsepvp_.mobspawnpoints.utils.MainUtils;


public class MainCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args) {
    	Player player = (Player) sender;
    	if(!player.isOp()) {player.sendMessage(Main.prefix + ChatColor.RED + "You don't have permissions"); return true;}
    	if(args.length == 0) {MainUtils.HelpMessage((Player) sender); return true;}
    	Main plugin = Main.getInstance();
    	if(args[0].equalsIgnoreCase("set")) { //START OF "SET"
	    	if(sender instanceof Player) {
	    		if(args.length == 7) {		
	    			if(args[1] instanceof String && args[2] instanceof String && MainUtils.isNumeric(args[4]) && MainUtils.isNumeric(args[5]) && MainUtils.isNumeric(args[6]) && (args[3].equalsIgnoreCase("true")||args[3].equalsIgnoreCase("false"))) {
	    			if(!plugin.getConfig().isSet("Spawners." + args[1])) {
				    	EntityType mobtype = null;	 
				    	try{
				    	    mobtype = EntityType.valueOf(args[2].toUpperCase());
				    	}catch(IllegalArgumentException exp){
				    	   player.sendMessage(Main.prefix + ChatColor.RED + "This mob type is not available");
				    	   player.sendMessage(Main.prefix + ChatColor.GRAY + "(use \"/mobspawn mobs\" to list all available mob types");
				    	   return true;
				    	}
				    	
				    	if(Integer.parseInt (args [4]) == 0) { player.sendMessage(Main.prefix + ChatColor.RED + "'Amount' must be at least "+ ChatColor.GRAY +ChatColor.BOLD  +"1" + ChatColor.RESET + ChatColor.RED +" Mob"); return true;}
				    	if(Integer.parseInt (args [5]) == 0) { player.sendMessage(Main.prefix + ChatColor.RED + "'Timer' must be at least "+ ChatColor.GRAY + ChatColor.BOLD  +"1" + ChatColor.RESET + ChatColor.RED +" Minutes");return true;}
				    	if(Integer.parseInt (args [6]) == 0) { player.sendMessage(Main.prefix + ChatColor.RED + "'Radius' must be at least "+ ChatColor.GRAY + ChatColor.BOLD +"1" + ChatColor.RESET + ChatColor.RED +" Block");return true;}
						Main.configsave(args[1].toLowerCase(),MainUtils.getTargetedBlock(player, 5).getLocation(), mobtype, Boolean.parseBoolean(args[3]),Integer.parseInt (args [4]), Integer.parseInt (args [5]), Integer.parseInt (args [6]));
						Main.timer_spawners.put(args[1].toLowerCase(), Integer.parseInt (args [5]));
						player.sendMessage(Main.prefix + ChatColor.GREEN + "Spawner successfully created and added to config.yml !");
					}
	    			else {
	    				player.sendMessage(Main.prefix + ChatColor.RED + "This spawner already exist");
	    			}
	    		} 
	    		else {
	    		MainUtils.SyntaxMsg((Player) sender);
	    		return true;
	    		}
	    		}
	    		else {
	    		MainUtils.SyntaxMsg((Player) sender);
	    		} //END else of args number verification
	    	}
    	} // END OF "SET"
    	if(args[0].equalsIgnoreCase("list")) {
    			if (!plugin.getConfig().isSet("Spawners.")){player.sendMessage(Main.prefix + ChatColor.RED + "No spawners to load..."); return true;} 
	    		ArrayList<String> list_name_spawners = new ArrayList<String>(plugin.getConfig().getConfigurationSection("Spawners").getKeys(false));
	    		ArrayList<String> list = new ArrayList<String>();
				for(int i =0; i < list_name_spawners.size(); i=i+1) {
					list.add(ChatColor.DARK_GRAY + "»  " + ChatColor.GREEN + (String.valueOf(list_name_spawners.get(i)) + " : "+ ChatColor.GOLD + " X: " + ChatColor.YELLOW+  String.valueOf(plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Location.x")) + ChatColor.GOLD + " Y: " + ChatColor.YELLOW + String.valueOf(plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Location.y")) + ChatColor.GOLD + " Z: " + ChatColor.YELLOW + String.valueOf(plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Location.z"))));
				}
				if(args.length == 1) {
					MainUtils.paginateHelpList(player, list,1, 5);
				} else if(args.length == 2 && MainUtils.isNumeric(args[1])) {
					MainUtils.paginateHelpList(player, list,Integer.parseInt (args [1]), 5);
				}
    		}
    	if(args[0].equalsIgnoreCase("delete")) {
    		if(args.length == 2 && args[1] instanceof String) {
    			if(plugin.getConfig().isSet("Spawners." + args[1].toLowerCase())) {
    				plugin.getConfig().set(("Spawners." + args[1].toLowerCase()), null);
    				plugin.saveConfig();
    				Main.timer_spawners.remove(args[1]);
    				player.sendMessage(Main.prefix + ChatColor.GRAY + ChatColor.BOLD + args[1] + ChatColor.RESET + ChatColor.GREEN + " Successfully deleted !");
    				}
    			else {
    	    		player.sendMessage(Main.prefix + ChatColor.RED + "Please specify a valid spawner name");
    	    		player.sendMessage(Main.prefix + ChatColor.GRAY + "(use \"/mobspawn list\" to list all spawners)");
    			}
    		}
    		else {
    			player.sendMessage(Main.prefix + ChatColor.RED + "Argument(s) : [Name]");
	    		player.sendMessage(Main.prefix + ChatColor.GRAY + "(use \"/mobspawn list\" to list all spawners)");
    		}
    	}
    	else if(!args[0].equalsIgnoreCase("test") && !args[0].equalsIgnoreCase("delete") && !args[0].equalsIgnoreCase("mobs") && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("set") || args.length == 0 ){
    		MainUtils.HelpMessage((Player) sender);
    	}
    	if(args[0].equalsIgnoreCase("mobs")) {
    		sender.sendMessage(Main.prefix + ChatColor.GREEN + "Available mobs list " + ChatColor.GRAY + "» "  + ChatColor.GREEN +   Main.mobs_list);
    	}
    	if(args[0].equalsIgnoreCase("timer-loop-test")) {
    		if(!player.isOp()) return true;
    		MainUtils.Every1Minutes();
    		player.sendMessage(Main.prefix + ChatColor.RED + "DEBUG : 1 Minute removed to the timer...");
    	}
	    	return true;
  }
}
    

