package me.badbones69.crazyauctions;

import java.util.ArrayList;
import java.util.Set;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrazyAuctions {
	
	public static CrazyAuctions instance;
	
	public static CrazyAuctions getInstance() {
		if(instance == null){
			instance  = new CrazyAuctions();
		}
		return instance;
	}

	public ArrayList<ItemStack> getItems(Player player, Shop type){
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ConfigurationSection worldData = Main.settings.getWorldConfig(player.getWorld().getName());
		if (worldData == null)
			return items;
		if(worldData.contains("Items")){
			for(String i : worldData.getConfigurationSection("Items").getKeys(false)){
				if(worldData.getString("Items." + i + ".Seller").equalsIgnoreCase(player.getName())){
					if(worldData.getBoolean("Items." + i + ".Biddable")){
						if(type == Shop.BID){
							items.add(worldData.getItemStack("Items."+i+".Item").clone());
						}
					}else{
						if(type == Shop.SELL){
							items.add(worldData.getItemStack("Items."+i+".Item").clone());
						}
					}
				}
			}
		}
		return items;
	}
	
}