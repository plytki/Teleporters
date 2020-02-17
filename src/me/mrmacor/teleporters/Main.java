package me.mrmacor.teleporters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class Main extends JavaPlugin {
	
  private static LinkedHashMap<Integer, Location> toLoadPadList1 = new LinkedHashMap<>();
  private static LinkedHashMap<Integer, Location> toLoadPadList2 = new LinkedHashMap<>();
  
  public void onEnable() {
	
	// creating directories
    File dir = getDataFolder();
    if (!dir.exists() &&  !dir.mkdir()) {
      System.out.println("Could not create directory for plugin: " + getDescription().getName()); 
    }
    
    
    // stuff below is to make sure that you aren't writing null onto the padLists
    toLoadPadList1 = (LinkedHashMap<Integer, Location>)load(new File(getDataFolder(), "padList1.dat"));
    toLoadPadList2 = (LinkedHashMap<Integer, Location>)load(new File(getDataFolder(), "padList2.dat"));
    
    if (toLoadPadList1 != null && toLoadPadList2 != null) {
      Teleporter.padList1 = (LinkedHashMap<Integer, Location>)load(new File(getDataFolder(), "padList1.dat"));
      Teleporter.padList2 = (LinkedHashMap<Integer, Location>)load(new File(getDataFolder(), "padList2.dat"));
    } 
    
    if (Teleporter.padList1.size() != Teleporter.padList2.size()) {
    	System.out.println(ChatColor.DARK_RED + "The teleporter pad lists do not line up, which means that unintended teleports may occur. ie. a teleporter teleporters a player to the wrong place."); 
    }
      
    
    getCommand("linkpads").setExecutor(new Linkpads());
    
    getServer().getPluginManager().registerEvents(new Linkpads(), this);
    getServer().getPluginManager().registerEvents(new Teleporter(), this);
    
  }
  
  public void save(Object o, File f) {
    try {
      if (!f.exists())
        f.createNewFile(); 
      BukkitObjectOutputStream oos = new BukkitObjectOutputStream(new FileOutputStream(f));
      oos.writeObject(o);
      oos.flush();
      oos.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public Object load(File f) {
    try {
      BukkitObjectInputStream ois = new BukkitObjectInputStream(new FileInputStream(f));
      Object result = ois.readObject();
      ois.close();
      return result;
    } catch (Exception e) {
      return null;
    } 
  }
  
  public void onDisable() {
    save(Teleporter.padList1, new File(getDataFolder(), "padList1.dat"));
    save(Teleporter.padList2, new File(getDataFolder(), "padList2.dat"));
  }
}
