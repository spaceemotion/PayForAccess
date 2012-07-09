package com.spaceemotion.payforaccess.command;


import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.config.SavesConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.LanguageUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;

public class EditCommand extends AbstractCommand {

	public EditCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.EDIT, plugin);

		description = "Edit properties of a saved region";
		usage = "[<property> <value>]";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		if (!workingTriggerIsSet(player.getName())) return false;

		ArrayList<String> types = new ArrayList<String>();
		types.add("price");
		types.add("overwrite-groups");
		types.add("msg-notenoughmoney");
		types.add("msg-buy");
		types.add("msg-paid");

		if (args.length == 1) {
			String str = "";

			for (int s = 0; s < types.size(); s++) {
				str += "&7" + types.get(s);

				if (s < types.size() - 2) str += "&f, ";
				else if (s < types.size() - 1) str += " &f" + LanguageUtil.getString("vocab.and") + " ";
			}

			ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("edit.info", str));

			return true;
		} else {
			if (args.length < 3) {
				setLastError(MessageUtil.parseMessage("error.arguments", Integer.toString(2)));
				return false;
			}

			SavesConfigManager regionManager = plugin.getSavesConfigManager();
			String current = regionManager.getWorkingTrigger(player.getName());
			ConfigurationSection section = regionManager.get().getConfigurationSection(current);
			
			String key = args[1];
			String value = args[2];

			if (key.startsWith("msg-") || key.startsWith("MSG-")) {
				if (args.length > 3) {
					for (int s = 3; s < args.length; s++) {
						value += " " + args[s];
					}
				}

				ConfigurationSection messages = null;

				if (section.isSet("messages")) messages = section.getConfigurationSection("messages");
				else messages = section.createSection("messages");

				if (key.equalsIgnoreCase("msg-notenoughmoney")) {
					messages.set("notenoughmoney", value);
				} else if (key.equalsIgnoreCase("msg-buy")) {
					messages.set("buy", value);
				} else if (key.equalsIgnoreCase("msg-paid")) {
					messages.set("paid", value);
				}

			} else if (checkArguments(args, 2)) {
				if (key.equalsIgnoreCase("price")) {
					section.set("price", Integer.parseInt(value));
				} else if (key.equalsIgnoreCase("overwrite-groups")) {
					section.set("overwrite-groups", Boolean.parseBoolean(value));
				}
			}

			if (getLastError().isEmpty()) {
				regionManager.save();
				ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("edit.success", key, current, value));

				return true;
			}
		}
		
		return false;
	}

}
