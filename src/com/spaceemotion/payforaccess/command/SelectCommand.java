package com.spaceemotion.payforaccess.command;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;

public class SelectCommand extends AbstractCommand {

	public SelectCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.SELECT, plugin);

		description = "Select a region for editing";
		usage = "<name>";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;

		if (args.length == 1) {
			if (!workingTriggerIsSet(player.getName())) return false;

			plugin.getSavesConfigManager().setWorkingTrigger(player, "");
			return true;
		} else {
			if (!checkArguments(args, 1)) return false;

			String region = args[1];

			if (!plugin.getSavesConfigManager().get().isSet(region)) setLastError(MessageUtil.parseMessage("error.notfound", region));
			else {
				plugin.getSavesConfigManager().setWorkingTrigger(player, region);
				ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("select.selected", region));
			}

			return getLastError().isEmpty();
		}
	}

}
