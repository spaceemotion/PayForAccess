package com.spaceemotion.payforaccess.command;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.CommandManager;
import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.config.SavesConfigManager;
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

		if (args.length == 1 && workingTriggerIsSet(player)) {
			region = plugin.getRegionConfigManager().getWorkingTrigger(player);
		} else if (!checkArguments(args, 1)) { return false; }

		if (region == null) region = args[1];

		SavesConfigManager regionManager = CommandManager.getPlugin().getRegionConfigManager();

		if (!regionManager.get().isSet(region)) setLastError(MessageUtil.parseMessage("error.notexists", region));
		else {
			regionManager.get().set(region, null);
			regionManager.removeFromList(region);

			if (workingTriggerIsSet(player.getName(), false) && regionManager.getWorkingTrigger(player.getName()).equalsIgnoreCase(region)) {
				regionManager.setWorkingTrigger(player.getName(), "");
			}

			regionManager.save();

			ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("remove.success", region));

		}

		return getLastError().isEmpty();
	}

}
