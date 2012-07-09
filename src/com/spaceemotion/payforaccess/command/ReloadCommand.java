package com.spaceemotion.payforaccess.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.LanguageUtil;

public class ReloadCommand extends AbstractCommand {

	public ReloadCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.EDIT, plugin);

		description = "Reloads config files";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		plugin.reloadConfig();
		plugin.getSavesConfigManager().getTriggerList(true);
		plugin.getPlayerConfigManager().getPlayerList(true);

		ChatUtil.sendPlayerMessage((Player) sender, LanguageUtil.getString("reload"));
		return true;
	}

}
