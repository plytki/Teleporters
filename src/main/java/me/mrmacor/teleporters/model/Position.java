package me.mrmacor.teleporters.model;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@AllArgsConstructor
public class Position {

    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public Location toLocation() {
        return new Location(Bukkit.getWorld(this.world),this.x,this.y,this.z,this.yaw,this.pitch);
    }

    public static Position position(String world, double x, double y, double z, float yaw, float pitch) {
        return new Position(world,x,y,z,yaw,pitch);
    }

    public static Position fromLocation(Location location) {
        return new Position(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

}