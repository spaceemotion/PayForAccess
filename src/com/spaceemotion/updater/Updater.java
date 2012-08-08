package com.spaceemotion.updater;

import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.spaceemotion.payforaccess.PayForAccessPlugin;


public class Updater {
	private static final double VERSION = 2.0;

	private static Map<String, String> data;
	private static double newVersion;
	private static String reason, urgency;


	public static boolean isUpToDate(PayForAccessPlugin plugin) {
		FileConfiguration config = plugin.getConfig();
		if (config.getString("updater.channel").equalsIgnoreCase("none")) return true;

		String chn = config.getString("updater.channel");

		data = Fetcher.getData(chn);
		if (data == null) return true;
		else {
			if (Boolean.parseBoolean(data.get("update")) == true) {

			}
		}

		return false;
	}
}
