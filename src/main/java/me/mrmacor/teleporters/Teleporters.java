package me.mrmacor.teleporters;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import me.mrmacor.teleporters.listener.LinkPadSetupListener;
import me.mrmacor.teleporters.listener.LinkPadTeleportListener;
import me.mrmacor.teleporters.manager.LinkedPadManager;
import me.mrmacor.teleporters.model.LinkedPad;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class Teleporters extends JavaPlugin {

  @Getter
  private static Teleporters plugin;
  private final File padsFile = new File(getDataFolder(), "pads.json");

  @Override
  public void onEnable() {
      plugin = this;
      // creating directories
      File dir = getDataFolder();
      if (!dir.exists() && !dir.mkdir())
          System.out.println("Could not create directory for plugin: " + getDescription().getName());
      if (this.padsFile.exists()) {
          Set<LinkedPad> load = load(this.padsFile);
          if (load != null)
              LinkedPadManager.linkedPads = load;
      }
      getCommand("linkpads").setExecutor(new LinkPadSetupListener());
      getServer().getPluginManager().registerEvents(new LinkPadSetupListener(), this);
      getServer().getPluginManager().registerEvents(new LinkPadTeleportListener(), this);
  }

  @Override
  public void onDisable() {
      save(LinkedPadManager.linkedPads, this.padsFile);
  }

  public void save(Set<LinkedPad> object, File file) {
      try {
          Gson gson = new GsonBuilder().setPrettyPrinting().create();
          Type setType = new TypeToken<Set<LinkedPad>>(){}.getType();
          FileWriter fileWriter = new FileWriter(file);
          fileWriter.write(gson.toJson(object, setType));
          fileWriter.close();
      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  public Set<LinkedPad> load(File file) {
      try {
          Gson gson = new Gson();
          Type setType = new TypeToken<Set<LinkedPad>>(){}.getType();
          JsonElement jsonElement = JsonParser.parseReader(new FileReader(file));
          return gson.fromJson(jsonElement, setType);
      } catch (Exception e) {
          return null;
      }
  }

}