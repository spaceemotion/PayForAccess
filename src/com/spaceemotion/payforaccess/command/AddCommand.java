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
import com.spaceemotion.payforaccess.config.RegionConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public class AddCommand extends AbstractCommand {

	public AddCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.ADD, plugin);

		description = "Add trigger to region";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		RegionConfigManager regionManager = plugin.getRegionConfigManager();
		Player player = (Player) sender;

		if (!workingRegionIsSet(player)) return false;

		String region = regionManager.getWorkingRegion(player);

		Block target = player.getTargetBlock(null, CommandManager.getPlugin().getConfig().getInt("max-block-sight", 120));
		if (!hasValidMaterial(target)) return false;

		Location location = target.getLocation();

		ConfigurationSection section = regionManager.get().getConfigurationSection(region);
		ArrayList<String> vectorList = (ArrayList<String>) section.getStringList("locations");
		vectorList.add(location.toVector().toString());

		section.set("locations", vectorList);
		regionManager.save();

		ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("add.success", region));

		return true;
	}

}
