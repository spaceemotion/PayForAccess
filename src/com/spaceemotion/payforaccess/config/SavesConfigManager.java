package com.spaceemotion.payforaccess.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;


public class SavesConfigManager extends ConfigManager {
	public static final String CONFIG_NAME = "saves";

	private ArrayList<String> triggerList;
	private Map<String, String> workingTrigger = new HashMap<String, String>();


	public SavesConfigManager() {
		super(CONFIG_NAME);

		getTriggerList();
	}

	public ArrayList<String> getTriggerList() {
		return getTriggerList(false);
	}

	public ArrayList<String> getTriggerList(boolean forceReload) {
		if (forceReload || triggerList == null) {
			reload();
			triggerList = new ArrayList<String>();

			for (String key : get().getKeys(false))
				triggerList.add(key);
		}

		return triggerList;
	}

	public void addTriggerToList(String id) {
		if (!triggerList.contains(id)) triggerList.add(id);
	}

	public void removeFromList(String id) {
		if (triggerList.contains(id)) triggerList.remove(id);
	}

	public void setWorkingTrigger(Player player, String region) {
		setWorkingTrigger(player.getName(), region);
	}

	public void setWorkingTrigger(String player, String region) {
		if (region.isEmpty()) workingTrigger.remove(player);
		else workingTrigger.put(player, region);
	}

	public String getWorkingTrigger(Player player) {
		return workingTrigger.get(player.getName());
	}

	public String getWorkingTrigger(String player) {
		return workingTrigger.get(player);
	}

}
