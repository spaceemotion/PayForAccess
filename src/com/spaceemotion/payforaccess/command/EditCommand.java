package com.spaceemotion.payforaccess.command;


import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.config.RegionConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
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

		if (!workingRegionIsSet(player.getName())) return false;

		if (args.length == 1) {
			ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("edit.info", "price"));
		} else if (checkArguments(args, 2)) {
			String key = args[1];
			String value = args[2];

			RegionConfigManager regionManager = plugin.getRegionConfigManager();
			String region = regionManager.getWorkingRegion(player.getName());

			if (key.equalsIgnoreCase("price")) {
				ConfigurationSection section = regionManager.get().getConfigurationSection(region);
				section.set("price", Integer.parseInt(value));

				regionManager.save();

				ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("edit.success", key, region, value));
			}
		}
		
		return getLastError().isEmpty();
	}

}
