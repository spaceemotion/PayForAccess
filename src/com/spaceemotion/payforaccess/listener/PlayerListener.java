package com.spaceemotion.payforaccess.listener;

import java.util.ArrayList;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.spaceemotion.payforaccess.CommandManager;
import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.PermissionManager;
import com.spaceemotion.payforaccess.util.ArrayUtil;
import com.spaceemotion.payforaccess.util.ChatUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public class PlayerListener implements Listener {
	
	public PlayerListener(PayForAccessPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
		

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		PayForAccessPlugin plugin = CommandManager.getPlugin();
		Player player = event.getPlayer();

		if (!player.hasPermission(PermissionManager.BUY)) return;

		Block block = event.getClickedBlock();

		if (block == null || !CommandManager.getPlugin().getConfig().getIntegerList("triggers").contains(block.getType().getId())) {
			return;
		}

		ArrayList<String> regionList = plugin.getRegionConfigManager().getRegionList();
		if (regionList == null) return;

		for (String name : regionList) {
			ConfigurationSection section = plugin.getRegionConfigManager().get().getConfigurationSection(name);

			ArrayList<String> locationList = (ArrayList<String>) section.getStringList("locations");

			if (locationList != null && !locationList.isEmpty()) {
				for (int j = 0; j < locationList.size(); j++) {
					String loc = locationList.get(j);

					ArrayList<String> locs = (ArrayList<String>) ArrayUtil.stringToArrayList(loc, ",");
					Vector lVec = new Vector(Float.parseFloat(locs.get(0)), Float.parseFloat(locs.get(1)), Float.parseFloat(locs.get(2)));

					Location dist = block.getLocation().subtract(lVec);

					if (dist.getBlockX() != 0 && dist.getBlockY() != 0 && dist.getBlockZ() != 0) continue;

					RegionManager regionMng = plugin.getWorldGuard().getRegionManager(player.getWorld());
					ProtectedRegion region = regionMng.getRegions().get(name);

					if (region.isMember(player.getName())) {
						/* Already bought access */
						ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("buy.alreadymember"));
						return;
					} else {
						Economy econ = plugin.getEconomy();
						int price = Integer.parseInt(section.getString("price"));

						if (econ.hasAccount(player.getName()) && econ.getBalance(player.getName()) >= price) {
							econ.withdrawPlayer(player.getName(), price);

							DefaultDomain memberDomain = region.getMembers();
							memberDomain.addPlayer(player.getName());

							try {
								regionMng.save();
								ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("buy.success", name));
							} catch (Exception e) {
								player.sendMessage(ChatColor.RED + "Could not save region: " + e.getMessage());
							}

						} else {
							ChatUtil.sendPlayerMessage(player, MessageUtil.parseMessage("buy.notenoughmoney", section.getString("price")));
						}
					}

					return;
				}
			}
		}
	}

}
