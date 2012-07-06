package com.spaceemotion.payforaccess.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;


public class RegionConfigManager extends ConfigManager {
	public static final String CONFIG_NAME = "regions";

	private ArrayList<String> regionList;
	private Map<String, String> workingRegion = new HashMap<String, String>();


	public RegionConfigManager() {
		super(CONFIG_NAME);

		getRegionList();
	}

	public ArrayList<String> getRegionList() {
		if (regionList == null) {
			reload();
			regionList = new ArrayList<String>();

			for (String key : get().getKeys(false))
				regionList.add(key);
		}

		return regionList;
	}

	public void addRegionToList(String region) {
		if (!regionList.contains(region)) regionList.add(region);
	}

	public void removeFromList(String region) {
		if (regionList.contains(region)) regionList.remove(region);
	}

	public void setWorkingRegion(Player player, String region) {
		setWorkingRegion(player.getName(), region);
	}

	public void setWorkingRegion(String player, String region) {
		if (region.isEmpty()) workingRegion.remove(player);
		else workingRegion.put(player, region);
	}

	public String getWorkingRegion(Player player) {
		return workingRegion.get(player.getName());
	}

	public String getWorkingRegion(String player) {
		return workingRegion.get(player);
	}

}
