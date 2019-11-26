package pulsepvp_.mobspawnpoints.utils;



import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.BlockIterator;

import pulsepvp_.mobspawnpoints.Main;

public class MainUtils implements Listener{
    public static void SpawnMobsFonction(World world, Location location, EntityType mobtype,boolean isbaby, int nombre_de_mobs, int range) {
    	int mobspawn_counter = 0;
    	while(mobspawn_counter < nombre_de_mobs) {
    		int randomXinRange = (0 + (int)(Math.random() * (((2*range+1) - 0) + 1)))+ location.getBlockX()-range;
    		int randomYinRange = (0 + (int)(Math.random() * (((2*range+1) - 0) + 1)))+ location.getBlockY()-range;
    		int randomZinRange = (0 + (int)(Math.random() * (((2*range+1) - 0) + 1)))+ location.getBlockZ()-range;
    		if(world.getBlockAt(randomXinRange,randomYinRange,randomZinRange).getType() == Material.AIR) {
    			mobspawn_counter = mobspawn_counter + 1;
	    		Location mobloc = new Location(world,randomXinRange,randomYinRange,randomZinRange);
	    		world.spawnEntity(mobloc, mobtype);
    		}
    	}
    }
    public static final Block getTargetedBlock(Player player, Integer range) {
        BlockIterator bi= new BlockIterator(player, range);
        Block lastBlock = bi.next();
        while (bi.hasNext()) {
            lastBlock = bi.next();
            if (lastBlock.getType() == Material.AIR)
                continue;
            break;
        }
        return lastBlock;
    }
    
    public static void SyntaxMsg(Player player) {
    	player.sendMessage(Main.prefix + ChatColor.RED + "Argument(s) : [Name] [MobType] [SpawnAlways : true/false] [Amount] [Timer] [Radius]");
    }
    @SuppressWarnings("unused")
	public static boolean isNumeric(String strNum) {
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    public static void Every1Minutes() {
    	Main plugin = Main.getInstance();
    	if ((plugin.getConfig().getString("Spawners") == null)){ return;}
		ArrayList<String> list_name_spawners = new ArrayList<String>(plugin.getConfig().getConfigurationSection("Spawners").getKeys(false));
		for(int i =0; i < list_name_spawners.size(); i=i+1) {
			if (Main.timer_spawners.get(list_name_spawners.get(i)) == 1) {
				
				World world = Bukkit.getWorld(String.valueOf(plugin.getConfig().getString("Spawners." + list_name_spawners.get(i) + ".Location.world")));
				Location loc = new Location(world , (plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Location.x")),
						(plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Location.y"))  ,          
						(plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Location.z")));
				
				if(plugin.getConfig().getBoolean("Spawners." + list_name_spawners.get(i) + ".Spawn always") && (Bukkit.getOnlinePlayers().size() > 0)) {
					EntityType mobtypetest = EntityType.valueOf((plugin.getConfig().getString("Spawners." + list_name_spawners.get(i) + ".Mob type")));
					int number_to_spawn = (plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Amount spawned each time"));
					int range = (plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Range"));
					SpawnMobsFonction(world,loc,mobtypetest,false, number_to_spawn,range);
					Main.timer_spawners.replace((String.valueOf(list_name_spawners.get(i))), (plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Time til next respawn")));
			
				} else if(isAPlayerNearLoc(loc) && !(plugin.getConfig().getBoolean("Spawners." + list_name_spawners.get(i) + ".Spawn always"))){
					EntityType mobtypetest = EntityType.valueOf((plugin.getConfig().getString("Spawners." + list_name_spawners.get(i) + ".Mob type")));
					int number_to_spawn = (plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Amount spawned each time"));
					int range = (plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Range"));
					SpawnMobsFonction(world,loc,mobtypetest,false, number_to_spawn,range);
					Main.timer_spawners.replace((String.valueOf(list_name_spawners.get(i))), (plugin.getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Time til next respawn")));
				}
			
			}
			else {
				int new_timer = Main.timer_spawners.get((String.valueOf(list_name_spawners.get(i)))) -1;
				Main.timer_spawners.replace((String.valueOf(list_name_spawners.get(i))), new_timer);

			}
		}

    }
      public static void paginateHelpList(Player player, ArrayList<String> list, int page, int countAll)
      {
    	  final int contentLinesPerPage = 5;
          int totalPageCount = 1;
     
          if((list.size() % contentLinesPerPage) == 0)
          {
            if(list.size() > 0)
            {
                totalPageCount = list.size() / contentLinesPerPage;
            }     
          }
          else
          {
            totalPageCount = (list.size() / contentLinesPerPage) + 1;
          }
     
          if(page <= totalPageCount)
          {
        	  player.sendMessage(ChatColor.YELLOW + ">" + ChatColor.STRIKETHROUGH + "--------------------------------------------" + ChatColor.RESET + ChatColor.YELLOW + "<");
        	  player.sendMessage(ChatColor.YELLOW + "                   Mob Spawn Points - " +  ChatColor.GOLD + "LIST");
        	  player.sendMessage(ChatColor.YELLOW + "                           Page (" + String.valueOf(page) + " of " + totalPageCount + ")");
        	  player.sendMessage(" ");
     
            if(list.isEmpty())
            {
            	player.sendMessage(ChatColor.RED + "         No spawners set yet !");
            }
            else
            {
                int i = 0, k = 0;
                page--;
     
                for (String entry : list)
                {
                  k++;
                  if ((((page * contentLinesPerPage) + i + 1) == k) && (k != ((page * contentLinesPerPage) + contentLinesPerPage + 1)))
                  {
                      i++;
                      player.sendMessage(entry);
                  }
                }
            }
      	  player.sendMessage(" ");
    	  player.sendMessage(ChatColor.GRAY + "     use \"/mobspawn list [Page]\" to go next page(s)");
      	  player.sendMessage(ChatColor.YELLOW + ">" + ChatColor.STRIKETHROUGH + "--------------------------------------------" + ChatColor.RESET + ChatColor.YELLOW + "<");
          }
          else
          {
        	  player.sendMessage(Main.prefix + ChatColor.RED + "There are only " + ChatColor.WHITE + totalPageCount + ChatColor.RED + " pages!");
          }
      }
      public static void HelpMessage(Player player) {
    	  player.sendMessage(ChatColor.YELLOW + ">" + ChatColor.STRIKETHROUGH + "--------------------------------------------" + ChatColor.RESET + ChatColor.YELLOW + "<");
    	  player.sendMessage(ChatColor.YELLOW + "     Mob Spawn Points - " +  ChatColor.GOLD + "SUBCOMMANDS");  
    	  player.sendMessage(" ");
    	  player.sendMessage(ChatColor.DARK_GRAY + "»" + ChatColor.RESET + ChatColor.GOLD + " Set → " + ChatColor.RESET + ChatColor.YELLOW + "Set a spawner at the block you're lookink at");
    	  player.sendMessage(ChatColor.DARK_GRAY + " " + ChatColor.RESET + ChatColor.YELLOW + " Argument(s) : " + ChatColor.RESET + ChatColor.GRAY + "[Name] [MobType] [SpawnAlways : true/false] [Amount] [Timer] [Radius]");
    	  player.sendMessage(ChatColor.DARK_GRAY + "»" + ChatColor.RESET + ChatColor.GOLD + " List → " + ChatColor.RESET + ChatColor.YELLOW + "List all spawners");
    	  player.sendMessage(ChatColor.DARK_GRAY + " " + ChatColor.RESET + ChatColor.YELLOW + " Argument(s) : " + ChatColor.RESET + ChatColor.GRAY + "(Page)");
    	  player.sendMessage(ChatColor.DARK_GRAY + "»" + ChatColor.RESET + ChatColor.GOLD + " Delete → " + ChatColor.RESET + ChatColor.YELLOW + "List all spawners");
    	  player.sendMessage(ChatColor.DARK_GRAY + " " + ChatColor.RESET + ChatColor.YELLOW + " Argument(s) : " + ChatColor.RESET + ChatColor.GRAY + "[Name]");
    	  player.sendMessage(ChatColor.DARK_GRAY + "»" + ChatColor.RESET + ChatColor.GOLD + " Mobs → " + ChatColor.RESET + ChatColor.YELLOW + "List all available mob types");
    	  player.sendMessage(ChatColor.YELLOW + ">" + ChatColor.STRIKETHROUGH + "--------------------------------------------" + ChatColor.RESET + ChatColor.YELLOW + "<");

    	  
    	  
      }
      public static boolean isAPlayerNearLoc( Location origin) {
    	  for (Player p : Bukkit.getOnlinePlayers()) {
    		if (p.getLocation().distance(origin) < 20) {
    		return true;
    		}
    	  }
    	  return false;
}
}