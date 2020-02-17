package me.mrmacor.teleporters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Teleporter implements Listener {
  private static ArrayList<String> cooldownList = new ArrayList<>();
  
  public static LinkedHashMap<Integer, Location> padList1 = new LinkedHashMap<>();
  public static LinkedHashMap<Integer, Location> padList2 = new LinkedHashMap<>();
  
  
  @EventHandler
  public void PlayerInteractEvent(PlayerInteractEvent e) {
	  
	// Null-checks to avoid NullPointerExceptions
  
    if (e.getPlayer() == null) return; 
    if (e.getAction() == null) return; 
    if (e.getClickedBlock() == null) return; 
    if (e.getClickedBlock().getLocation() == null) return; 
    
    
    Player player = e.getPlayer();
    Action action = e.getAction();
    Block block = e.getClickedBlock();
    Location blockLocation = block.getLocation();
    
	if (padList1.containsValue(blockLocation) && !cooldownList.contains(player.getName()) && player.isSneaking() && action.equals(Action.PHYSICAL) && block.getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE) | block.getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) { 
       
		for (int key : padList1.keySet()) {
        	
          if (padList1.get(key).equals(blockLocation)) {
        	  
          
            Location oldLoc = padList2.get(key);
            Location newLoc = new Location(oldLoc.getWorld(), oldLoc.getX() + 0.5, oldLoc.getY(), oldLoc.getZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
            player.teleport(newLoc);
            
            // Cooldown stuff
            cooldownList.add(player.getName());
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
            	cooldownList.remove(player.getName());
                }, 5L);
            return;
            
            
          } 
        }  
    } else if (padList2.containsValue(blockLocation) && !cooldownList.contains(player.getName()) && player.isSneaking() && block.getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE) | block.getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
      
    	for (int key : padList2.keySet()) {
        
	        if (padList2.get(key).equals(blockLocation)) {
	        	
	        
	          Location oldLoc = padList1.get(key);
	          Location newLoc = new Location(oldLoc.getWorld(), oldLoc.getX() + 0.5D, oldLoc.getY(), oldLoc.getZ() + 0.5D, player.getLocation().getYaw(), player.getLocation().getPitch());
	          player.teleport(newLoc);
	          
	          // Cooldown stuff
	          cooldownList.add(player.getName());
	          Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
				cooldownList.remove(player.getName());
	              }, 5L);
	          
	          return;
	        } 
      } 
    } 
  }
 
  
  @EventHandler
  public void BlockBreakEvent(BlockBreakEvent e) {
    
	//Null-checks to avoid more NullPointerExceptions
	  
	if (e.getBlock() == null) return; 
    if (e.getPlayer() == null) return; 
    if (e.getBlock().getLocation() == null) return; 
    if (e.getBlock().getType() == null) return; 
    if (padList2.keySet() == null) return; 
    
    
    Block block = e.getBlock();
    Player player = e.getPlayer();
    
    if ((padList1.containsValue(block.getLocation()) | padList2.containsValue(block.getLocation()) && block.getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE) | block.getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE))) {
      for (int key : padList2.keySet()) {
        
        if (((padList2.get(key)).equals(block.getLocation()) | (padList1.get(key)).equals(block.getLocation()))) {
          padList1.remove(key);
          padList2.remove(key);
          player.sendMessage(ChatColor.DARK_RED + "Teleporter successfully unlinked.");
        } 
        
      }  
    }
    
  }
  
}