package me.mrmacor.teleporters.listener;

import java.util.HashMap;

import me.mrmacor.teleporters.manager.LinkedPadManager;
import me.mrmacor.teleporters.model.LinkedPad;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class LinkPadSetupListener implements CommandExecutor, Listener{

	private static final HashMap<Player, LinkedPad> currentPadLinking = new HashMap<>();

    @Override
	public boolean onCommand(CommandSender player, Command commmand, String label, String[] args) {
		if (!player.hasPermission("teleporters.linkpads")) {
			player.sendMessage(ChatColor.DARK_RED + "Sorry, you do not have permission to execute this command!");
			return false;
		}
		if (!(player instanceof Player)) {
			player.sendMessage("Only players can execute this command!");
			return false;
		}
		if (currentPadLinking.get(player) != null) {
			// Fired when the player sends the command twice in a row
			player.sendMessage(ChatColor.DARK_RED + "Pad linking cancelled.");
			currentPadLinking.remove(player);
		} else {
			currentPadLinking.put((Player) player, new LinkedPad());
			player.sendMessage(ChatColor.AQUA + "Please right click the first teleport pad.");
		}
		return false;
	}

	//TODO: make the pressure plates sparkly when you select them
	
	// vectors for storing pad locations have to be created here for reasons
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		// Making sure nothing the event gives is null, to prevent a NullPointerException
		if (e.getHand() == null) return;
		if (e.getClickedBlock() == null) return;
		if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return; // Makes sure the event only fires once
		Player player = e.getPlayer();
		Block block = e.getClickedBlock();
		Action action = e.getAction();
		if (currentPadLinking.get(player) == null) return;
		if (LinkedPadManager.isBlockUsed(block)) {
			player.sendMessage(ChatColor.DARK_RED + "This pad is already in use!");
			return;
		}
		if (block.getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE) | block.getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE) && action.equals(Action.RIGHT_CLICK_BLOCK) && block.getRelative(BlockFace.DOWN).getType() == Material.PURPLE_GLAZED_TERRACOTTA | block.getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) {
			// stuff for 1st pad linking
			player.sendMessage(ChatColor.AQUA + "Please right click the next teleport pad.");
			LinkedPad linkedPad = currentPadLinking.get(player);
			LinkedPadManager.setPad(linkedPad, block);
			if (linkedPad.isFinished()) {
				LinkedPadManager.linkedPads.add(linkedPad);
				currentPadLinking.remove(player);
				player.sendMessage(ChatColor.AQUA + "Pad linking successful!");
			}
		} else if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
			// Fired when the player clicks the wrong block
			player.sendMessage(ChatColor.DARK_RED + "Pad linking cancelled.");
			currentPadLinking.remove(player);
		}
	}

}