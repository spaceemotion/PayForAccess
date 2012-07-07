package com.spaceemotion.payforaccess.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;


public class PlayerConfigManager extends ConfigManager {
	public static final String CONFIG_NAME = "players";

	private Map<String, ArrayList<String>> playerList;

	public PlayerConfigManager() {
		super(CONFIG_NAME);
		
		getPlayerList();
	}

	public Map<String, ArrayList<String>> getPlayerList() {
		if (playerList == null) {
			reload();
			playerList = new HashMap<String, ArrayList<String>>();

			for (String key : get().getKeys(false)) {
				ArrayList<String> players = (ArrayList<String>) get().getStringList(key);

				if (players == null) {
					players = new ArrayList<String>();
				}

				playerList.put(key, players);
			}
		}

		return playerList;
	}

	public void addPlayerToList(String name, Player player) {
		if (playerList.containsKey(name) && !playerList.get(name).contains(player)) {
			playerList.get(name).add(player.getName());
		}
	}
}
