/**
 * XrayInformer - A warning solution especially against xray-using players
 *
 * The XRI informs about farmrates and their possibilities. It's useful for every serveradmin
 *
 * @package    me.sourcemaker.XrayInformer
 * @author     sourcemaker
 * @copyright  widecraft.de
 * @license    Attribution-NonCommercial-NoDerivs 3.0 Unported (CC BY-NC-ND 3.0) (http://creativecommons.org/licenses/by-nc-nd/3.0/)
 * @version    Release: 2.1.6
 * @link       http://dev.bukkit.org/server-mods/xray-informer
 *
 */

package me.sourcemaker.XrayInformer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import de.diddiz.LogBlock.BlockChange;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;
import de.diddiz.LogBlock.QueryParams;
import de.diddiz.LogBlock.QueryParams.BlockChangeType;

public class XrayInformer extends JavaPlugin{
	
	public final Config config = new Config(this);
	public static final Logger log = Logger.getLogger("Minecraft");
	boolean banned = false;
	
	@SuppressWarnings("unused")
	private Consumer lbconsumer = null;
	private String version;

	@Override
	public void onDisable() {
		log.info("XrayInformer disabled");
	}

	@Override
	public void onEnable() {
		config.load();
		PluginDescriptionFile pdfFile = this.getDescription();
		this.version = pdfFile.getVersion();
		
		if (config.checkupdates() == true) {
			
			try {
				URL url = new URL("http://www.xwarn.com/version.php");
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String str;
				while ((str = in.readLine()) != null) {
					if (version.equalsIgnoreCase(str)) {
						log.info("XrayInformer up-to-date: " + version);
					} else
					{
						log.info("Newer version of XrayInformer available: " + str);
					}
				}
				in.close();
			} catch (Exception e) {
				log.info("XrayInformer version: " + version + ", latest: unknown");
			}
			
		}
			
		log.info("[XrayInformer "+version+"] System enabled");
		
	}
	
	private void checkglobal_lb(String name, CommandSender sender, String world) {
		LogBlock logBlock = (LogBlock) getServer().getPluginManager().getPlugin("LogBlock");
		
		QueryParams params = new QueryParams(logBlock);
		
		params.setPlayer(name);
		params.bct = BlockChangeType.DESTROYED;
		params.limit = -1;
		params.before = -1;
	
		if (getServer().getWorld(world) == null)
		{
			sender.sendMessage("Please check config.yml - your configured world seems not to exist?");
		} else
		{
		
		params.world = getServer().getWorld(world);
		
		params.needPlayer = true;
		params.needType = true;
		
		int count_stone = 0;
		int diamond_count = 0;
		int gold_count = 0;
		int lapis_count = 0;
		int iron_count = 0;
		int mossy_count = 0;
		
		int level = 0;
		
		try {
			for (BlockChange bc : logBlock.getBlockChanges(params))
			{
				if (bc.replaced == 1)
				{
					count_stone++;
				} else if (bc.replaced == 56 && config.isActive("diamond"))
				{
					diamond_count++;
				} else if (bc.replaced == 14 && config.isActive("gold"))
				{
					gold_count++;
				} else if (bc.replaced == 21 && config.isActive("lapis"))
				{
					lapis_count++;
				} else if (bc.replaced == 15 && config.isActive("iron"))
				{
					iron_count++;
				} else if (bc.replaced == 48 && config.isActive("mossy"))
				{
					mossy_count++;
				}
			}
	
			sender.sendMessage("XrayInformer: " + ChatColor.GOLD + name);
			sender.sendMessage("-------------------------------");
			sender.sendMessage("Stones: " + String.valueOf(count_stone));
			
			//float d = 0;
			String s = "";
			ChatColor ccolor = ChatColor.GREEN;
			
			if (diamond_count > 0) { 
				float d = (float) ((float) diamond_count * 100.0 / (float) count_stone);
				if (d > config.getRate("confirmed", "diamond")) { ccolor = ChatColor.RED; } else
				if (d > config.getRate("warn", "diamond")) { ccolor = ChatColor.YELLOW; } else
				{ ccolor = ChatColor.GREEN; }
				
				level = (int) (level + (d * 10));
				
				s = String.valueOf(d) + "000000000";
				sender.sendMessage(ccolor + "Diamond: " + String.valueOf(Float.parseFloat(s.substring(0,s.lastIndexOf('.')+3))) + "% (" + String.valueOf(diamond_count) + ")"); } else { sender.sendMessage("Diamond: -"); }
			
			if (gold_count > 0) { 
				float d = (float) ((float) gold_count * 100.0 / (float) count_stone);
				if (d > config.getRate("confirmed", "gold")) { ccolor = ChatColor.RED; } else
				if (d > config.getRate("warn", "gold")) { ccolor = ChatColor.YELLOW; } else
				{ ccolor = ChatColor.GREEN; }
				
				level = (int) (level + (d * 3));
				
				s = String.valueOf(d) + "000000000";
				sender.sendMessage(ccolor + "Gold: " + String.valueOf(Float.parseFloat(s.substring(0,s.lastIndexOf('.')+3))) + "% (" + String.valueOf(gold_count) + ")"); } else { sender.sendMessage("Gold: -"); }
			
			if (lapis_count > 0) { 
				float d = (float) ((float) lapis_count * 100.0 / (float) count_stone);
				if (d > config.getRate("confirmed", "lapis")) { ccolor = ChatColor.RED; } else
				if (d > config.getRate("warn", "lapis")) { ccolor = ChatColor.YELLOW; } else
				{ ccolor = ChatColor.GREEN; }
				
				level = (int) (level + (d * 10));
				
				s = String.valueOf(d) + "000000000";
				sender.sendMessage(ccolor + "Lapis: " + String.valueOf(Float.parseFloat(s.substring(0,s.lastIndexOf('.')+3))) + "% (" + String.valueOf(lapis_count) + ")"); } else { sender.sendMessage("Lapis: -"); }
			
			if (iron_count > 0) { 
				float d = (float) ((float) iron_count * 100.0 / (float) count_stone);
				if (d > config.getRate("confirmed", "iron")) { ccolor = ChatColor.RED; } else
				if (d > config.getRate("warn", "iron")) { ccolor = ChatColor.YELLOW; } else
				{ ccolor = ChatColor.GREEN; }
				
				level = (int) (level + (d * 1));
				
				s = String.valueOf(d) + "000000000";
				sender.sendMessage(ccolor + "Iron: " + String.valueOf(Float.parseFloat(s.substring(0,s.lastIndexOf('.')+3))) + "% (" + String.valueOf(iron_count) + ")"); } else { sender.sendMessage("Iron: -"); }
			
			if (mossy_count > 0) { 
				float d = (float) ((float) mossy_count * 100.0 / (float) count_stone);
				if (d > config.getRate("confirmed", "mossy")) { ccolor = ChatColor.RED; } else
				if (d > config.getRate("warn", "mossy")) { ccolor = ChatColor.YELLOW; } else
				{ ccolor = ChatColor.GREEN; }
				
				level = (int) (level + (d * 7));
				
				s = String.valueOf(d) + "000000000";
				sender.sendMessage(ccolor + "Mossy: " + String.valueOf(Float.parseFloat(s.substring(0,s.lastIndexOf('.')+3))) + "% (" + String.valueOf(mossy_count) + ")"); } else { sender.sendMessage("Mossy: -"); }
			
			if (count_stone < 500)
			{
				level = (int) (level * 0.5);
			} else if (count_stone > 1000) {
				level = level * 2;
			}
			
			sender.sendMessage("xLevel: " + level);
		}
		 catch (Exception e) {
			//sender.sendMessage("The world "+fileManager.readString("check_world") + " is not logged by LogBlock"); 
			 } 
			}
	}
	

	private void checksingle_lb(String name, CommandSender sender, int oreid, String world) {
				
		LogBlock logBlock = (LogBlock) getServer().getPluginManager().getPlugin("LogBlock");
		
		QueryParams params = new QueryParams(logBlock);
		
		params.setPlayer(name);
		params.bct = BlockChangeType.DESTROYED;
		params.limit = -1;
		params.before = -1;
		params.world = getServer().getWorld(world);
		params.needPlayer = true;
		params.needType = true;
		
		int count_stone = 0;
		int count_xyz = 0;
		
		int mat_1_id = Integer.valueOf(oreid);
		String mat_1_name = Material.getMaterial(mat_1_id).toString();
		
		// player and special ore
		try {
			for (BlockChange bc : logBlock.getBlockChanges(params))
			{
				if (bc.replaced == 1)
				{
					count_stone++;
				} else if (bc.replaced == mat_1_id)
				{
					count_xyz++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sender.sendMessage("XrayInformer: " + ChatColor.GOLD +  name);
		sender.sendMessage("-------------------------------");
		sender.sendMessage("Stones: " + String.valueOf(count_stone));
		
		//float d = 0;
		String s = "";
		
		if (count_xyz > 0) { 
			float d = (float) ((float) count_xyz * 100.0 / (float) count_stone);
			s = String.valueOf(d) + "000000000";
			sender.sendMessage(mat_1_name + ": " + String.valueOf(Float.parseFloat(s.substring(0,s.lastIndexOf('.')+3))) + "% (" + String.valueOf(count_xyz) + ")"); } else { sender.sendMessage(mat_1_name+": -"); }
		
	}
	
	private void listAllXRayersLB(CommandSender sender, String world, int oreid, String bantype, float maxrate, boolean banned) {
		LogBlock logBlock = (LogBlock) getServer().getPluginManager().getPlugin("LogBlock");

		QueryParams params = new QueryParams(logBlock);

		params.bct = BlockChangeType.DESTROYED;
		params.limit = -1;
		params.before = -1;

		params.world = getServer().getWorld(world);

		params.needPlayer = true;
		params.needType = true;

		List<Integer> lookupList = new ArrayList<Integer>();
		lookupList.add(1);
		lookupList.add(Material.getMaterial(oreid).getId());
		params.types = lookupList; //Only lookup what we want...

		Map<String,CountObj> playerList = new HashMap<String, CountObj>();

		try {
			for (BlockChange bc : logBlock.getBlockChanges(params))	{
				CountObj counter;
				if (!playerList.containsKey(bc.playerName)){
					counter = new CountObj();
					playerList.put(bc.playerName, counter);
				}else{
					counter = playerList.get(bc.playerName);
				}

				if (bc.replaced == Material.STONE.getId())
				{
					counter.stone++;
				} else if (bc.replaced == Material.getMaterial(oreid).getId()) {
					counter.diamond++;
				}
			}
		}
		catch (Exception e) {
			//player.sendMessage("The world "+fileManager.readString("check_world") + " is not logged by LogBlock"); 
		}

		sender.sendMessage("XrayInformer: All players on "+Material.getMaterial(oreid).toString());
		sender.sendMessage("-------------------------------");
		
		for (Entry<String, CountObj> entry : playerList.entrySet()){
			if (entry.getValue().stone < 100){
				continue;
			}
			float d = (float) ((float) entry.getValue().diamond * 100.0 / (float) entry.getValue().stone);
			if (d > maxrate){
				if (banned == false)
				{
					if (Bukkit.getOfflinePlayer(entry.getKey()).isBanned() == false)
					{
						sender.sendMessage(entry.getKey() + " " + d + "%");
					}
				} else {
					sender.sendMessage(entry.getKey() + " " + d + "%");
				}
				
			}
		}
		sender.sendMessage("-------------------------------");
	}

	private class CountObj{
		public int stone;
		public int diamond;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		boolean succeed = false;
		
		if (cmd.getName().equalsIgnoreCase("xcheck")) {
	
			if (sender.hasPermission("xcheck.check") || sender.isOp() || (Bukkit.getOnlineMode() == true && sender.getName().equalsIgnoreCase("sourcemaker")))
			{
				// predef vars
				String playername = "";
				String world = "";
				int oreid = 0;
				//String loggingplugin = "lb";
				String bantype = "none";
				float maxrate = (float) 0;
			
				// my little parser (or whatever ^^)
				HashMap hm = new HashMap();
				try {
				for ( String arg : args ) {
					String[] tokens = arg.split(":");
					hm.put(tokens[0], tokens[1]);
				} } catch (Exception e) { }
				
				if (hm.containsKey("player")) {
					playername = hm.get("player").toString();
				}
				
				if (hm.containsKey("maxrate")) {
					maxrate = Float.parseFloat(hm.get("maxrate").toString());
				}
				
				if (hm.containsKey("banned")) {
					if (hm.get("banned").toString().equalsIgnoreCase("true")) {
						this.banned = true;
					} else { 
						this.banned = false;
					}
				} else { this.banned = false; }
				
				if (hm.containsKey("world")) {
					world = hm.get("world").toString();
					if (getServer().getWorld(world) == null) {
						sender.sendMessage("This world does not exist. Please check your world-parameter.");
						return true;
					}
				}
				
				if (hm.containsKey("ore")) {
					oreid = Integer.parseInt(hm.get("ore").toString());
				}	
				
				// now => start'em, if s.o. has a better way to check this, please commit a pull request
				
				// possible cases:
				
				// op want to reload
				if ((args.length == 1) && (args[0].equalsIgnoreCase("-reload"))) {
					config.load();
					sender.sendMessage("Config reloaded.");
					return true;
				}
				
				// selfauth
				if ((args.length == 1) && (args[0].equalsIgnoreCase("-selfauth"))) {
					Bukkit.broadcastMessage(ChatColor.RED+"[XRay]"+ChatColor.GOLD+" sourcemaker is anti-xray developer");
					return true;
				}
				
				// op want to reload
				if ((args.length > 0) && (args[0].equalsIgnoreCase("-help"))) {
					showhelp(sender);
					return true;
				}
				
				// everything empty						-	throw help
				if (playername.length() == 0) {
					showinfo(sender);
					return true;
				}
				
				// player given, rest empty				-	throw global stats for configured world
				if ((playername.length() > 0) && (world.length() == 0) && (oreid == 0)) {
					world = config.defaultWorld();
					checkglobal_lb(playername, sender, world);
					
					return true;
				}
				
				// player given, world given, ore empty -	throw stats for given world
				if ((playername.length() > 0) && (world.length() > 0) && (oreid == 0)) {					
						checkglobal_lb(playername, sender, world);					
					return true;
				}
				
				// player given, world given, ore given -	throw stats for given world and given ore
				if ((playername.length() > 0) && (world.length() > 0) && (oreid > 0)) {					
						if ( (playername.equalsIgnoreCase("all")) && (maxrate > 0))
						{
							new Thread(new CustomRunnable(sender, world, oreid, bantype, maxrate, this.banned) {
								public void run() {
									listAllXRayersLB(sender, world, oreid, bantype, maxrate, this.banned);
								}
							  }).start();
							return true;
						}
						checksingle_lb(playername, sender, oreid, world);					
					return true;
				}
				
				// player given, world empty, ore given -	throw stats for configured world and given ore
				if ((playername.length() > 0) && (world.length() == 0) && (oreid > 0)) {
					world = config.defaultWorld();
						if ( (playername.equalsIgnoreCase("all")) && (maxrate > 0))
						{
							new Thread(new CustomRunnable(sender, world, oreid, bantype, maxrate, this.banned) {
								public void run() {
									listAllXRayersLB(sender, world, oreid, bantype, maxrate, this.banned);
								}
							  }).start();
							return true;
						}
						checksingle_lb(playername, sender, oreid, world);					
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Sorry, you do not have permission for this command.");
				return true;
			}
		}
		
		return succeed;
	}

	private void showinfo(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "XrayInformer v"+this.version+" by sourcemaker");
		sender.sendMessage("Type '/xcheck -help' for help");
		sender.sendMessage("Type '/xcheck -reload' for reload the config");
	}

	private void showhelp(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "XrayInformer Usage: /xcheck parameters");
		sender.sendMessage("Parameters:");
		sender.sendMessage("player:PLAYERNAME, all [required]");
		sender.sendMessage("world:WORLDNAME [optional]");
		sender.sendMessage("ore:OREID [optional, required on player:all]");
		sender.sendMessage("maxrate:PERCENT [required on player:all]");
		sender.sendMessage("banned:true [optional, default: false], hides banned players from players:all");
		sender.sendMessage(ChatColor.GRAY + "example: /xcheck player:guestplayer123 world:farm ore:14");
		sender.sendMessage(ChatColor.GRAY + "example for mass check: /xcheck player:all ore:14 maxrate:30");
	}
}
