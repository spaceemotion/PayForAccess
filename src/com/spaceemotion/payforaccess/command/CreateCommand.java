package com.spaceemotion.payforaccess.command;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.spaceemotion.payforaccess.CommandManager;
import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.config.RegionConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public class CreateCommand extends AbstractCommand {
	public CreateCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.CREATE, plugin);

		description = "Creates a trigger to buy a worldguard region";
		usage = "<region> <price>";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!checkArguments(args, 2)) return false;

		String region = args[1];
		int price = Integer.parseInt(args[2]);
		Player player = (Player) sender;

		Block target = player.getTargetBlock(null, CommandManager.getPlugin().getConfig().getInt("max-block-sight", 120));
		if (!hasValidMaterial(target)) return false;

		Location location = target.getLocation();

		RegionConfigManager regionManager = CommandManager.getPlugin().getRegionConfigManager();

		RegionManager regionMng = CommandManager.getPlugin().getWorldGuard().getRegionManager(player.getWorld());
		ProtectedRegion protRegion = regionMng.getRegions().get(region);

		if (protRegion == null) {
			setLastError(MessageUtil.parseMessage("error.notexists", region));
		} else {
			if (regionManager.get().isSet(region)) setLastError(MessageUtil.parseMessage("error.defined", region));
			else {
				ConfigurationSection section = regionManager.get().createSection(region);

				String[] vectorList = new String[1];
				vectorList[0] = location.toVector().toString();
				section.set("locations", Arrays.asList(vectorList));

				section.set("price", price);

				regionManager.save();
				CommandManager.getPlugin().getRegionConfigManager().addRegionToList(region);

				ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("create.success", region, Integer.toString(price)));
			}
		}

		return getLastError().isEmpty();
	}

}
