package com.spaceemotion.payforaccess.command;

import java.util.ArrayList;
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
import com.spaceemotion.payforaccess.config.SavesConfigManager;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.LanguageUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public class CreateCommand extends AbstractCommand {
	public CreateCommand(PayForAccessPlugin plugin) {
		super(PermissionManager.CREATE, plugin);

		description = "Creates a new trigger";
		usage = "<name> <price> [region]";
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String region = null;

		if (args.length == 4) {
			region = args[3];
		} else if (!checkArguments(args, 2)) return false;

		String name = args[1];
		int price = Integer.parseInt(args[2]);


		/* Get the block we're looking at */
		Block target = player.getTargetBlock(null, CommandManager.getPlugin().getConfig().getInt("max-block-sight", 120));
		if (!hasValidMaterial(target)) return false;

		Location location = target.getLocation();

		SavesConfigManager config = CommandManager.getPlugin().getSavesConfigManager();

		if (config.get().isSet(name)) {
			setLastError(MessageUtil.parseMessage("error.defined", name));

			return false;
		}


		/* Create first section content */
		ConfigurationSection section = config.get().createSection(name);
		section.set("price", price);

		String[] vectorList = new String[1];
		vectorList[0] = location.toVector().toString();
		section.set("locations", Arrays.asList(vectorList));
		section.createSection("effects");

		/* Add default region (copied from addeffect command) */
		if (region != null) {
			ConfigurationSection effects = section.getConfigurationSection("effects");

			ArrayList<String> regions = (ArrayList<String>) effects.getStringList("regions");

			RegionManager regionMng = CommandManager.getPlugin().getWorldGuard().getRegionManager(player.getWorld());
			ProtectedRegion protRegion = regionMng.getRegions().get(region);

			if (protRegion == null) {
				setLastError(MessageUtil.parseMessage("error.region.notexists", region));
			} else {
				if (regions.contains(region)) {
					setLastError(LanguageUtil.getString("error.region.defined"));
				} else {
					regions.add(region);
					effects.set("regions", regions);
				}
			}
		}


		/* Save to file and set to working trigger */
		if (getLastError().isEmpty()) {
			config.save();
			config.addTriggerToList(name);
			config.setWorkingTrigger(player, name);

			ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("create.success", name, Integer.toString(price)));

			return true;
		} else {
			return false;
		}
	}

}
