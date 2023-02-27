package me.mrmacor.teleporters.model;

import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.Block;

@NoArgsConstructor
public class LinkedPad {

    public Position pad1;
    public Position pad2;

    public LinkedPad(Location pad1, Location pad2) {
        this.pad1 = Position.fromLocation(pad1);
        this.pad2 = Position.fromLocation(pad2);
    }

    public void setPad1(Block block) {
        this.pad1 = Position.fromLocation(block.getLocation());
    }

    public void setPad2(Block block) {
        this.pad2 = Position.fromLocation(block.getLocation());
    }

    public Location getPad1() {
        if (this.pad1 == null) return null;
        return this.pad1.toLocation();
    }

    public Location getPad2() {
        if (this.pad2 == null) return null;
        return this.pad2.toLocation();
    }

    public Location[] getPads() {
        return new Location[]{ getPad1(), getPad2() };
    }

    public boolean isFinished() {
        return this.pad1 != null && this.pad2 != null;
    }

}
