package com.spaceemotion.payforaccess.util;


import org.bukkit.configuration.file.FileConfiguration;

import com.spaceemotion.payforaccess.CommandManager;
import com.spaceemotion.payforaccess.config.ConfigManager;


public class LanguageUtil {
	public static String getString(String msg) {
		ConfigManager manager = CommandManager.getPlugin().getLanguageConfigManager();
		if (CommandManager.getPlugin() != null) {
			FileConfiguration lang = manager.get();

			if (lang.isString(msg)) return lang.getString(msg);
		}

		if (manager.getFallback() != null && manager.getFallback().isString(msg)) {
			return manager.getFallback().getString(msg);
		} else {
			return ("{" + msg + "}").toUpperCase();
		}
	}
}