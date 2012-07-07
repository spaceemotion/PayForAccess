package com.spaceemotion.payforaccess.command;


import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spaceemotion.payforaccess.CommandManager;
import com.spaceemotion.payforaccess.PayForAccessPlugin;
import com.spaceemotion.payforaccess.util.LanguageUtil;
import com.spaceemotion.payforaccess.util.MessageUtil;


public abstract class AbstractCommand {
	private final String permission;
	private String lastError = "";

	protected String description = "";
	protected String usage = "";

	protected final PayForAccessPlugin plugin;


	public AbstractCommand(String permission, PayForAccessPlugin plugin) {
		this.plugin = plugin;
		this.permission = permission;
	}

	/**
	 * Executes the command
	 * 
	 * @param sender The CommandSender
	 * @param args List with the arguments
	 * @return boolean Returns false if command failed to execute
	 */
	public abstract boolean execute(CommandSender sender, String[] args);

	/**
	 * Checks for the argument length and produces an error
	 * 
	 * @param number Number of arguments needed
	 * @param name Displayed command name
	 * @return
	 */
	protected boolean checkArguments(String[] args, int number) {
		if (args.length != number + 1) {
			lastError = MessageUtil.parseMessage("error.arguments", Integer.toString(number));
			return false;
		}

		return true;
	}

	/**
	 * Get the last error the command caused
	 * 
	 * @return String The error
	 */
	public String getLastError() {
		return lastError;
	}

	protected void setLastError(String err) {
		this.lastError = err;
	}

	/**
	 * Clear the last error
	 */
	public void clearErrors() {
		lastError = "";
	}

	/**
	 * Get the description for this command
	 * 
	 * @return String The description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Get the permission needed for this command
	 * 
	 * @return String The needed permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * Get the usage string for this command
	 * 
	 * @return String the usage string
	 */
	public String getUsage() {
		return usage;
	}

	public boolean workingTriggerIsSet(Player player) {
		return workingTriggerIsSet(player.getName());
	}

	public boolean workingTriggerIsSet(Player player, boolean showError) {
		return workingTriggerIsSet(player.getName(), showError);
	}

	public boolean workingTriggerIsSet(String player) {
		return workingTriggerIsSet(player, true);
	}

	public boolean workingTriggerIsSet(String player, boolean showError) {
		String region = plugin.getRegionConfigManager().getWorkingTrigger(player);

		if (region == null || region.isEmpty()) {
			if (showError) setLastError(LanguageUtil.getString("select.notselected"));

			return false;
		}

		return true;
	}

	public boolean hasValidMaterial(Block block) {
		if (!CommandManager.getPlugin().getConfig().getIntegerList("triggers").contains(block.getType().getId())) {
			setLastError(LanguageUtil.getString("error.material"));

			return false;
		}

		return true;
	}
}
