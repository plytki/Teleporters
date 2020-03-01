package me.mrmacor.teleporters;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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

public class Linkpads implements CommandExecutor, Listener{


	private static ArrayList<String> LinkingList1 = new ArrayList<String>();
	private static ArrayList<String> LinkingList2 = new ArrayList<String>();
	
	
	
    @Override
	public boolean onCommand(CommandSender player, Command commmand, String label, String[] args) {
    	if (player.hasPermission("teleporters.linkpads")) {
			if (player instanceof Player) {
			
				if (LinkingList1.contains(player.getName())) {
				
					// Fired when the player sends the command twice in a row
					player.sendMessage(ChatColor.DARK_RED + "Pad linking cancelled.");
					LinkingList1.remove(player.getName());
					return false;
					
				} else {
				
					LinkingList1.add(player.getName());
					player.sendMessage(ChatColor.AQUA + "Please right click the first teleport pad.");
					return false;
				}
				
				} else {
					player.sendMessage("Only players can execute this command!");
				}
	
	
    		} else {
    			player.sendMessage(ChatColor.DARK_RED + "Sorry, you do not have permission to execute this command!");
    		}
    	
		return false;
   }
	
	//TODO: make the pressure plates sparkly when you select them
	
	// vectors for storing pad locations have to be created here for reasons
	
	private Location pad1;
	private Location pad2;
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
	
	// Making sure nothing the event gives is null, to prevent a NullPointerException
	if (e.getPlayer() == null) return;
	if (e.getAction() == null) return;
	if (e.getHand() == null) return;
	if (e.getClickedBlock() == null) return;
	
	
	if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return; // Makes sure the event only fires once
	
	Player player = e.getPlayer();
	Block block = e.getClickedBlock();
	Action action = e.getAction();
	
	
	if (LinkingList1.contains(player.getName())) {
		if (!Teleporter.padList1.containsValue(block.getLocation()) && !Teleporter.padList2.containsValue(block.getLocation())) { // Chosen when the player needs to select the first teleport pad
			
			if (block.getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE) | block.getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)&& action.equals(Action.RIGHT_CLICK_BLOCK) && block.getRelative(BlockFace.DOWN).getType() == Material.PURPLE_GLAZED_TERRACOTTA | block.getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) {
			
				
				
				// stuff for 1st pad linking
				player.sendMessage(ChatColor.AQUA + "Please right click the next teleport pad.");
				LinkingList1.remove(player.getName());
				LinkingList2.add(player.getName());
				
				pad1 = block.getLocation();
				
			
			} else if (action.equals(Action.RIGHT_CLICK_BLOCK)){
				// Fired when the player clicks the wrong block
				player.sendMessage(ChatColor.DARK_RED + "Pad linking cancelled.");
				LinkingList1.remove(player.getName());
			
			}
		
		}
	
	
	
	} else if (LinkingList2.contains(player.getName())) {
		if (!Teleporter.padList1.containsValue(block.getLocation()) && !Teleporter.padList2.containsValue(block.getLocation())) {
			
		
			if (block.getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE) | block.getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE) && action.equals(Action.RIGHT_CLICK_BLOCK) && block.getRelative(BlockFace.DOWN).getType() == Material.PURPLE_GLAZED_TERRACOTTA | block.getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN && !block.getLocation().equals(pad1)) {
			
			
				//stuff for 2nd pad linking
				player.sendMessage(ChatColor.AQUA + "Pad linking successful!");
				LinkingList2.remove(player.getName());
				
				pad2 = block.getLocation();
			
			} else if (action.equals(Action.RIGHT_CLICK_BLOCK)){
				// Fired when the player clicks the wrong block
				player.sendMessage(ChatColor.DARK_RED + "Pad linking cancelled.");
				LinkingList2.remove(player.getName());
			}  
			
			
			if (!(pad1 == null) && !(pad2 == null)) {
			
				Teleporter.padList1.put(Teleporter.padList1.size() + 1, pad1);
				Teleporter.padList2.put(Teleporter.padList2.size() + 1, pad2);
			
			}
		
		
		}
	
	}
	 
 }


}