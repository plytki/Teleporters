package me.mrmacor.teleporters.manager;

import me.mrmacor.teleporters.model.LinkedPad;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LinkedPadManager {

    public static final ArrayList<Player> cooldownList = new ArrayList<>();
    public static Set<LinkedPad> linkedPads = new HashSet<>();

    public static LinkedPad getLinkedPad(Block block) {
        for (LinkedPad linkedPad : linkedPads) {
            for (Location pad : linkedPad.getPads()) {
                if (pad != null && pad.equals(block.getLocation())) {
                    return linkedPad;
                }
            }
        }
        return null;
    }

    public static boolean isBlockUsed(Block block) {
        for (LinkedPad linkedPad : linkedPads) {
            Location pad1 = linkedPad.getPad1();
            if (pad1 != null && block.getLocation().equals(pad1)) {
                return true;
            }
            Location pad2 = linkedPad.getPad2();
            if (pad2 != null && block.getLocation().equals(pad2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean setPad(LinkedPad linkedPad, Block padBlock) {
        if (linkedPad.getPad1() == null) {
            linkedPad.setPad1(padBlock);
            return true;
        } else if (linkedPad.getPad2() == null) {
            linkedPad.setPad2(padBlock);
            return true;
        }
        return false;
    }

}