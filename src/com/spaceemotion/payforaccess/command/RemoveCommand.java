package com.spaceemotion.payforaccess.command;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.CommandManager;
import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.config.RegionConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;

public class RemoveCommand extends AbstractCommand {

	public RemoveCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.REMOVE, plugin);

		description = "Remove a region from list";
		usage = "[name]";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String region = null;

		if (args.length == 1 && workingRegionIsSet(player)) {
			region = plugin.getRegionConfigManager().getWorkingRegion(player);
		} else if (!checkArguments(args, 1)) { return false; }

		region = args[1];

		RegionConfigManager regionManager = CommandManager.getPlugin().getRegionConfigManager();

		if (!regionManager.get().isSet(region)) setLastError(MessageUtil.parseMessage("error.notexists", region));
		else {
			regionManager.get().set(region, null);
			regionManager.removeFromList(region);

			if (workingRegionIsSet(player.getName(), false) && regionManager.getWorkingRegion(player.getName()).equalsIgnoreCase(region)) {
				regionManager.setWorkingRegion(player.getName(), "");
			}

			regionManager.save();

			ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("remove.success", region));

		}

		return getLastError().isEmpty();
	}

}
