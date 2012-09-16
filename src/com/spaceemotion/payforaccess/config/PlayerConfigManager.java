package com.spaceemotion.payforaccess.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class PlayerConfigManager extends ConfigManager {
	public static final String CONFIG_NAME = "players";

	private Map<String, ConfigurationSection> playerList;

	public PlayerConfigManager() {
		super(CONFIG_NAME);
		
		getPlayerList();
	}

	public Map<String, ConfigurationSection> getPlayerList() {
		return getPlayerList(false);
	}

	public Map<String, ConfigurationSection> getPlayerList(boolean forceReload) {
		if (forceReload || playerList == null) {
			reload();
			playerList = new HashMap<String, ConfigurationSection>();

			for (String key : get().getKeys(false)) {
				// Compatibility code ...
				if (get().isConfigurationSection(key)) {
					// Wohoo, no more crappy conversions!
					playerList.put(key, get().getConfigurationSection(key));
				} else {
					// Okay, it IS the old system >.>
					ConfigurationSection cs = get().createSection(key);
					playerList.put(key, cs);
					get().set(key, null); // remove the old element
				}
			}
		}

		return playerList;
	}

	public void addPlayerToList(String name, Player player) {
		if (playerList.containsKey(name) && !playerList.get(name).contains(player.getName())) {
			playerList.get(name).createSection(player.getName());
		}
	}

	public void removePlayerFromList(String name, String player) {
		if (playerList.containsKey(name) && playerList.get(name).contains(player)) {
			playerList.get(name).get(player);
		}
	}

	public Set<String> getPlayerListOfTrigger(String trigger) {
		return (playerList.get(trigger) != null) ? playerList.get(trigger).getKeys(false) : null;
	}
}
