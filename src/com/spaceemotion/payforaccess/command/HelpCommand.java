package com.spaceemotion.payforaccess.command;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.CommandManager;
import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public class HelpCommand extends AbstractCommand {
	public HelpCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.HELP, plugin);

		description = "Displays the help page";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		int size = CommandManager.getIndexList().size();
		ArrayList<String> messages = new ArrayList<String>();

		/* Generate the header */
		String header = "&dHelp for PayForAccess &5v" + plugin.getDescription().getVersion();
		String s = "&e============";
		
		messages.add(MessageUtil.parseColors(s + " " + header + " " + s));

		/* Generate the command list */
		for (int i = 0; i < size; i++) {
			String key = CommandManager.getIndexList().get(i);
			AbstractCommand cmd = CommandManager.getCommandList().get(key);
			String msg = "&a/pfa " + key + " &2" + cmd.getUsage();

			if (!cmd.getDescription().isEmpty()) msg += "&7 " + cmd.getDescription();
			else msg += "&8 (No description available)";

			messages.add(MessageUtil.parseColors(msg));
		}

		/* Are we currently working on a region? */
		String workingRegion = plugin.getRegionConfigManager().getWorkingTrigger(((Player) sender).getName());
		if (workingRegion != null && !workingRegion.isEmpty()) messages.add("&6Selected region: &7" + workingRegion);

		/* And off we go! */
		ChatUtil.splitSendMessage(messages.toArray(new String[0]), false);

		return true;
	}

}
