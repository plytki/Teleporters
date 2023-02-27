package me.mrmacor.teleporters.listener;

import me.mrmacor.teleporters.Teleporters;
import me.mrmacor.teleporters.manager.LinkedPadManager;
import me.mrmacor.teleporters.model.LinkedPad;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class LinkPadTeleportListener implements Listener {

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        Player player = e.getPlayer();
        Action action = e.getAction();
        Block block = e.getClickedBlock();
        Location blockLocation = block.getLocation();
        if (!canUseLinkedPad(player, action, block)) return;
        for (LinkedPad linkedPad : new ArrayList<>(LinkedPadManager.linkedPads)) {
            for (int i = 0; i < linkedPad.getPads().length; i++) {
                Location pad = linkedPad.getPads()[i];
                Location secondPad = linkedPad.getPads()[i == 0 ? i + 1 : i - 1];
                if (blockLocation.equals(pad)) {
                    World world = secondPad.getWorld();
                    if (world != null && Bukkit.getWorld(world.getName()) != null) {
                        Location newLoc = new Location(secondPad.getWorld(), secondPad.getX() + 0.5, secondPad.getY(), secondPad.getZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                        player.teleport(newLoc);
                        // Cooldown stuff
                        LinkedPadManager.cooldownList.add(player);
                        Bukkit.getScheduler().runTaskLater(Teleporters.getPlugin(), () -> {
                            LinkedPadManager.cooldownList.remove(player);
                        }, 5L);
                        return;
                    } else {
                        player.sendMessage("The receiving teleporter pad is in a world that no longer exists, the pair will now be deleted from the list.");
                        LinkedPadManager.linkedPads.remove(linkedPad);
                    }
                }
            }
        }
    }

    @EventHandler
    public void BlockBreakEvent(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        LinkedPad linkedPad = LinkedPadManager.getLinkedPad(block);
        if (linkedPad != null) {
            LinkedPadManager.linkedPads.remove(linkedPad);
            player.sendMessage(ChatColor.DARK_RED + "Teleporter successfully unlinked.");
        }
    }

    public boolean canUseLinkedPad(Player player, Action action, Block block) {
        return !LinkedPadManager.cooldownList.contains(player) &&
                player.isSneaking() &&
                action.equals(Action.PHYSICAL) &&
                block.getType().equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE) | block.getType().equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
    }

}