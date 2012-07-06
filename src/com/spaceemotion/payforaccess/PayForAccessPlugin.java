package com.spaceemotion.payforaccess;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.spaceemotion.payforaccess.config.ConfigManager;
import com.spaceemotion.payforaccess.config.RegionConfigManager;
import com.spaceemotion.payforaccess.listener.PlayerListener;


public class PayForAccessPlugin extends JavaPlugin {
	private static Economy econ = null;

	private static WorldGuardPlugin wgPlugin = null;
	private RegionConfigManager configManager;
	private ConfigManager languageConfigManager;


	public void onEnable() {
		/* Vault and WorldGuard integration */
		if (!setupEconomy()) {
			getLogger().info("Economy integration with Vault failed or no economy plugin found!");
			getServer().getPluginManager().disablePlugin(this);

			return;
		}

		if (!setupWorldGuard()) {
			getLogger().info("WorldGuard integration failed, please make sure that you have it installed!");
			getServer().getPluginManager().disablePlugin(this);

			return;
		}


		/* set up the listeners */
		new PlayerListener(this);


		/* set up command executor */
		getCommand("pfa").setExecutor(new CommandManager(this));


		/* set up configuration file(s) */
		getConfig().options().copyDefaults(true);
		this.saveConfig();


		configManager = new RegionConfigManager();
		languageConfigManager = new ConfigManager(configManager.get().getString("language", getConfig().getString("language", "english")));
		if (languageConfigManager.getName() != "english") languageConfigManager.setFallbackConfiguration("english");


		/* finally done! */
		getLogger().info("Activated");
	}

	public void onDisable() {
		getLogger().info("Disabled");
	}


	public RegionConfigManager getRegionConfigManager() {
		return configManager;
	}

	public ConfigManager getLanguageConfigManager() {
		return languageConfigManager;
	}

	/*
	 * WorldGuard implementation functions
	 */

	private boolean setupWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		// WorldGuard may not be loaded
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) return false;
		else {
			wgPlugin = (WorldGuardPlugin) plugin;
			return true;
		}
	}

	public WorldGuardPlugin getWorldGuard() {
		if (wgPlugin != null) return wgPlugin;

		return null; // TODO: Maybe throw an exception in here
	}

	/*
	 * Vault implementation functions
	 */

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

			if (rsp != null) {
				econ = rsp.getProvider();
				return econ != null;
			}
		}

		return false;
	}

	public Economy getEconomy() {
		return econ;
	}
}
