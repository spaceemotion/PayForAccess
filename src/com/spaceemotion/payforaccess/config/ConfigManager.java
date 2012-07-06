package com.spaceemotion.payforaccess.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.spaceemotion.payforaccess.CommandManager;


public class ConfigManager {
	private FileConfiguration config;
	private File file = null;

	private FileConfiguration fallback;

	private String name;

	public ConfigManager(String name) {
		this.name = name;
	}

	public void reload() {
		if (file == null) file = new File(CommandManager.getPlugin().getDataFolder(), name + ".yml");

		config = YamlConfiguration.loadConfiguration(file);

		// Look for defaults in the jar
		InputStream defConfigStream = CommandManager.getPlugin().getResource(name + ".yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

	public FileConfiguration get() {
		if (config == null) this.reload();

		return config;
	}

	public void save() {
		if (config == null || file == null) return;

		try {
			get().save(file);
		} catch (IOException e) {
			CommandManager.getPlugin().getLogger().log(Level.SEVERE, "Could not save config to " + file, e);
		}
	}

	public String getName() {
		return name;
	}

	public void setFallbackConfiguration(String alternative) {
		fallback = YamlConfiguration.loadConfiguration(new File(CommandManager.getPlugin().getDataFolder(), alternative + ".yml"));

		// Look for defaults in the jar
		InputStream defConfigStream = CommandManager.getPlugin().getResource(alternative + ".yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			fallback.setDefaults(defConfig);
		}
	}

	public FileConfiguration getFallback() {
		return fallback;
	}
}
