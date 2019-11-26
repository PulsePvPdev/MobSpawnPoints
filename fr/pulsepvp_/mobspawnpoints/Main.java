package pulsepvp_.mobspawnpoints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import pulsepvp_.mobspawnpoints.commands.MainCommand;
import pulsepvp_.mobspawnpoints.utils.MainUtils;


public class Main extends JavaPlugin {
	
    private static ConsoleCommandSender console = Bukkit.getConsoleSender();
    private static Main instance;
    public static HashMap<String, Integer> timer_spawners = new HashMap<String, Integer>();
    public static String prefix = (ChatColor.GRAY + "[" + ChatColor.RESET + ChatColor.YELLOW + ChatColor.BOLD + "MobSpawnPoints" + ChatColor.RESET + ChatColor.GRAY + "] " );
    static String[] mobs = {"Bat", "Blaze", "Cat", "Cave_spider", "Chicken", "Cod", "Cow", "Creeper", "Dolphin", "Donkey", "Drowned", "Elder_guardian",
    		"Ender_dragon", "Enderman" , "Endermite", "Evoker", "Fox", "Ghast", "Giant", "Guardian", "Horse", "Husk", "Illusioner",
    		 "Iron_golem", "Llama", "Magma_cube", "Mooshroom", "Mule", "Ocelot", "Panda", "Parrot", "Phantom", "Pig", "Pillager", 
    		 "Polar_bear", "Pufferfish", "Rabbit", "Ravager", "Salmon", "Sheep", "Shulker", "Shulker_bullet", "Silverfish", "Skeleton", 
    		 "Skeleton_horse", "Slime", "Snow_golem", "Spider", "Squid", "Stray", "Trader_llama", "Tropical_fish", "Turtle", "Vex", 
    		 "Villager", "Vindicator", "Wandering_trader", "Witch", "Wither", "Wither", "Wither_Skeleton", "Wolf", "Zombie", 
    		 "Zombie_horse", "Zombie_pigman", "Zombie_villager"};
    public static ArrayList<String> mobs_list = new ArrayList<>(Arrays.asList(mobs));
    @Override
    public void onEnable() {
    	instance = this;
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        
        getCommand("mobspawn").setExecutor(new MainCommand());
        console.sendMessage(ChatColor.YELLOW + "[" + ChatColor.GOLD + "MobSpawnPoints" + ChatColor.YELLOW + "] " + ChatColor.GREEN + "Loading Spawners from config.yml ..." );
        if (!(getConfig().getString("Spawners") == null)) {
        ArrayList<String> list_name_spawners = new ArrayList<String>(getConfig().getConfigurationSection("Spawners").getKeys(false));
        for(int i =0; i < list_name_spawners.size(); i=i+1) {
        	timer_spawners.put(list_name_spawners.get(i),getConfig().getInt("Spawners." + list_name_spawners.get(i) + ".Time til next respawn"));
        }
        console.sendMessage(ChatColor.YELLOW + "[" + ChatColor.GOLD + "MobSpawnPoints" + ChatColor.YELLOW + "] " + ChatColor.GREEN + list_name_spawners.size() + " Spawners loaded successfully !" );
        } else {
        	console.sendMessage(ChatColor.YELLOW + "[" + ChatColor.GOLD + "MobSpawnPoints" + ChatColor.YELLOW + "] " + ChatColor.RED +  "No spawners found in the config.yml :(" );
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
        @Override
        public void run() {
        MainUtils.Every1Minutes();
        }
        }, 0, (long) (20*60));
    }
    
    
    @Override
    public void onDisable() {
    	this.saveConfig();
    }
    
    public static ConsoleCommandSender getConsole() {
        return console;
    }
    public static Main getInstance() {
        return instance;
    }
    public static void configsave(String name,Location location, EntityType mobtype, boolean spawnalways,Integer amount, Integer delay,Integer range) {
    	instance.getConfig().set("Spawners." + name + ".Location.world", location.getWorld().getName());
    	instance.getConfig().set("Spawners." + name + ".Location.x", location.getBlockX());
    	instance.getConfig().set("Spawners." + name + ".Location.y", location.getBlockY());
    	instance.getConfig().set("Spawners." + name + ".Location.z", location.getBlockZ());
    	instance.getConfig().set("Spawners." + name + ".Mob type", mobtype.toString());
    	instance.getConfig().set("Spawners." + name + ".Amount spawned each time", amount);
    	instance.getConfig().set("Spawners." + name + ".Range", range);
    	instance.getConfig().set("Spawners." + name + ".Time til next respawn", delay);
    	instance.getConfig().set("Spawners." + name + ".Spawn always", spawnalways);
    	instance.saveConfig();
    }

}
