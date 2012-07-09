package com.spaceemotion.payforaccess.command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.config.PlayerConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public class ForgetCommand extends AbstractCommand {

	public ForgetCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.EDIT, plugin);

		description = "Forget that a given player bought the access";
		usage = "[trigger] <player>";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (args.length > 3 || args.length == 1) {
			setLastError(MessageUtil.parseMessage("error.arguments", Integer.toString(2)));
		} else {
			Player player = (Player) sender;
			PlayerConfigManager config = plugin.getPlayerConfigManager();
			
			String select, name;

			if (args.length == 2 && workingTriggerIsSet(player.getName())) {
				select = plugin.getSavesConfigManager().getWorkingTrigger(player);
				name = args[2];
			} else {
				select = args[1];
				name = args[2];
			}

			ArrayList<String> list = (ArrayList<String>) config.get().getStringList(select);

			if (list != null) {
				list.remove(name);

				config.get().set(select, list);
				config.save();
				config.removePlayerFromList(select, name);

				ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("forget.success", select, name));
				return true;
			}
		}

		return false;
	}

}
