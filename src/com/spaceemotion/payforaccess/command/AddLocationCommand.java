package com.spaceemotion.payforaccess.command;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.CommandManager;
import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.config.SavesConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public class AddLocationCommand extends AbstractCommand {

	public AddLocationCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.ADD, plugin);

		description = "Add trigger to selected one";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		SavesConfigManager regionManager = plugin.getRegionConfigManager();
		Player player = (Player) sender;

		if (!workingTriggerIsSet(player)) return false;

		String id = regionManager.getWorkingTrigger(player);

		Block target = player.getTargetBlock(null, CommandManager.getPlugin().getConfig().getInt("max-block-sight", 120));
		if (!hasValidMaterial(target)) return false;

		Location location = target.getLocation();

		ConfigurationSection section = regionManager.get().getConfigurationSection(id);
		ArrayList<String> vectorList = (ArrayList<String>) section.getStringList("locations");
		String vecString = location.toVector().toString();
		if (!vectorList.contains(vecString)) vectorList.add(vecString);

		section.set("locations", vectorList);
		regionManager.save();

		ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("addloc.success", id));

		return true;
	}

}
