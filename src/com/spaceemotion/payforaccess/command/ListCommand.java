package com.spaceemotion.payforaccess.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.util.ArrayUtil;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.LanguageUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;

public class ListCommand extends AbstractCommand {

	public ListCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.LIST, plugin);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		ArrayList<String> regionList = plugin.getRegionConfigManager().getRegionList();

		if (regionList == null || regionList.size() == 0) {
			ChatUtil.sendPlayerMessage((Player) sender, LanguageUtil.getString("list.empty"));
			return true;
		}

		ChatUtil.sendPlayerMessage((Player) sender, LanguageUtil.getString("list.found"));
		
		String messages[] = new String[regionList.size()];
		
		for (int i = 0; i < messages.length; i++) {
			String name = regionList.get(i);
			String msg = LanguageUtil.getString("list.format");

			Map<String, String> replaceMap = new HashMap<String, String>();
			replaceMap.put("name", name);
			replaceMap.put("number", Integer.toString(i + 1));

			ConfigurationSection region = plugin.getRegionConfigManager().get().getConfigurationSection(name);
			ArrayList<String> locationList = (ArrayList<String>) region.getStringList("locations");

			if (locationList != null && !locationList.isEmpty()) {
				String vectorString = "";

				for (int j = 0; j < locationList.size(); j++) {
					String loc = locationList.get(j);

					ArrayList<String> locs = (ArrayList<String>) ArrayUtil.stringToArrayList(loc, ",");
					Vector vector = new Vector(0, 0, 0);

					vector.setX(Float.parseFloat(locs.get(0)));
					vector.setY(Float.parseFloat(locs.get(1)));
					vector.setZ(Float.parseFloat(locs.get(2)));

					vectorString += "&7x &f" + vector.getX() + " &7y &f" + vector.getY() + " &7z &f" + vector.getZ();
					if (j < locationList.size() - 2) vectorString += ", ";
					else if (j < locationList.size() - 1) vectorString += " " + LanguageUtil.getString("vocab.and") + " ";
				}

				replaceMap.put("location", vectorString);
			}

			replaceMap.put("price", Integer.toString(region.getInt("price")));

			for (String key : replaceMap.keySet())
				msg = msg.replaceAll("%" + key.toUpperCase() + "%", replaceMap.get(key));

			messages[i] = MessageUtil.parseColors(msg);
		}
		
		ChatUtil.splitSendMessage(messages, false);

		return true;
	}

}
