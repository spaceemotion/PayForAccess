package com.spaceemotion.payforaccess.command;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.config.SavesConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;

public class EditCommand extends AbstractCommand {
	private List<Type> types;


	public EditCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.EDIT, plugin);

		description = "Edit properties of a saved region";
		usage = "[<property> <value>]";

		types = new ArrayList<Type>();
		types.add(new Type("price", "The amount of money that is needed to buy the access"));
		types.add(new Type("overwrite-groups", "Overwrite the groups the player is in or not"));
		types.add(new Type("msg-notenoughmoneys", "Custom message for 'not enough money'"));
		types.add(new Type("msg-buy", "Custom message when buying"));
		types.add(new Type("msg-paid", "Custom message when already paid"));
		types.add(new Type("msg-limit", "Custom message when the limit is reached"));
		types.add(new Type("as-owner", "If the player should be added as owner instead of member"));
		types.add(new Type("server-cmd", "Execute commands as server or player"));
		types.add(new Type("max-players", "The player limit for this region"));
		types.add(new Type("time-limit", "The 'renting' time"));
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		if (!workingTriggerIsSet(player.getName())) {
			return false;
		} else if (args.length == 1) {
			this.displayHelp(player);

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
				} else if (key.equalsIgnoreCase("msg-limit")) {
					messages.set("limit", value);
				}
			} else if (checkArguments(args, 2)) {
				if (key.equalsIgnoreCase("price")) {
					section.set("price", Integer.parseInt(value));
				} else if (key.equalsIgnoreCase("overwrite-groups")) {
					section.set("overwrite-groups", Boolean.parseBoolean(value));
				} else if (key.equalsIgnoreCase("as-owner")) {
					section.set("as-owner", Boolean.parseBoolean(value));
				} else if (key.equalsIgnoreCase("server-cmd")) {
					section.set("server-cmd", Boolean.parseBoolean(value));
				} else if (key.equalsIgnoreCase("max-players")) {
					section.set("max-players", Integer.parseInt(value));
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

	private void displayHelp(Player player) {
		int size = types.size();
		String[] messages = new String[size];

		for (int s = 0; s < size; ++s) {
			Type type = types.get(s);
			String msg = "&a" + type.getName();

			if (!type.getDescription().isEmpty()) msg += "&7 " + type.getDescription();
			else msg += "&8 (No description available)";

			messages[s] = MessageUtil.parseColors(msg);
		}

		ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("edit.info", Integer.toString(size)));
		ChatUtil.splitSendMessage(messages, false);
	}

}
