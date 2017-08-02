package me.badbones69.crazyauctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class SettingsManager {

	static SettingsManager instance = new SettingsManager();

	public static SettingsManager getInstance() {
		return instance;
	}

	Plugin p;

	FileConfiguration config;
	File cfile;

	FileConfiguration data;
	File dfile;
	
	FileConfiguration msg;
	File mfile;

	private final Map<String, ConfigurationSection> worldConfigMap = new HashMap<>();

	public ConfigurationSection getWorldConfig(String worldName) {
		return this.worldConfigMap.get(worldName);
	}
	
	public void setup(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();
		}
		
		cfile = new File(p.getDataFolder(), "config.yml");
		if (!cfile.exists()) {
			try{
        		File en = new File(p.getDataFolder(), "/config.yml");
         		InputStream E = getClass().getResourceAsStream("/config.yml");
         		copyFile(E, en);
         	}catch (Exception e) {
         		e.printStackTrace();
         	}
		}
		config = YamlConfiguration.loadConfiguration(cfile);
		
		dfile = new File(p.getDataFolder(), "Data.yml");
		if (!dfile.exists()) {
			try{
        		File en = new File(p.getDataFolder(), "/Data.yml");
         		InputStream E = getClass().getResourceAsStream("/Data.yml");
         		copyFile(E, en);
         	}catch (Exception e) {
         		e.printStackTrace();
         	}
		}
		data = YamlConfiguration.loadConfiguration(dfile);
		ConfigurationSection worldGroups = this.data.getConfigurationSection("WorldGroups");
		for (String key : worldGroups.getKeys(false)) {
			ConfigurationSection worldConfig = worldGroups.getConfigurationSection(key);
			worldConfig.getStringList("worlds").forEach(world -> this.worldConfigMap.put(world, worldConfig));
		}
		
		mfile = new File(p.getDataFolder(), "Messages.yml");
		if (!mfile.exists()) {
			try{
        		File en = new File(p.getDataFolder(), "/Messages.yml");
         		InputStream E = getClass().getResourceAsStream("/Messages.yml");
         		copyFile(E, en);
         	}catch (Exception e) {
         		e.printStackTrace();
         	}
		}
		msg = YamlConfiguration.loadConfiguration(mfile);
	}

	public List<World> getWorldGroup(World world) {
		List<World> worlds = new ArrayList<>();
		ConfigurationSection worldData = this.getWorldConfig(world.getName());
		if (worldData == null)
			return worlds;
		ConfigurationSection section = this.data.getConfigurationSection("WorldGroups")
				.getConfigurationSection(worldData.getName());
		for (String worldName : section.getStringList("worlds")) {
			World w = Bukkit.getWorld(worldName);
			if (w == null)
				continue;
			worlds.add(w);
		}
		return worlds;
	}

	public FileConfiguration getMsg() {
		return msg;
	}
	public void saveData() {
		try {
			data.save(dfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save Data.yml!");
		}
	}
	public void saveMsg() {
		try {
			msg.save(mfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save Messages.yml!");
		}
	}
	public void reloadData() {
		data = YamlConfiguration.loadConfiguration(dfile);
	}
	public void reloadMsg() {
		msg = YamlConfiguration.loadConfiguration(mfile);
	}
	public FileConfiguration getConfig() {
		return config;
	}

	public void saveConfig() {
		try {
			config.save(cfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger()
					.severe(ChatColor.RED + "Could not save config.yml!");
		}
	}

	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(cfile);
	}

	public PluginDescriptionFile getDesc() {
		return p.getDescription();
	}
	public static void copyFile(InputStream in, File out) throws Exception { // https://bukkit.org/threads/extracting-file-from-jar.16962/
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}