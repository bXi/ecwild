package net.bixxy.ecwild;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ecwild extends JavaPlugin {

	private double minX;
	private double maxX;
	private double minZ;
	private double maxZ;
	private double width;

	private HashMap<UUID, Long> usages = new HashMap<UUID, Long>();

	@Override
	public void onEnable() {
		// TODO fix setBoundaries() to fire on Enable and on worldborder change
		// setBoundaries();
	}

	private void setBoundaries() {
		WorldBorder b = Bukkit.getWorld("world").getWorldBorder();

		Location center = b.getCenter();
		width = b.getSize();

		minX = center.getX() - width / 2;
		maxX = minX + width;

		minZ = center.getZ() - width / 2;
		maxZ = minZ + width;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("wild") || cmd.getName().equalsIgnoreCase("rtp")) {
			if (sender instanceof Player) {

				Player player = (Player) sender;
				if (player.getWorld().getEnvironment() == Environment.NORMAL) {

					Timestamp timestamp = new Timestamp(System.currentTimeMillis());

					Long lastTimestamp = usages.get(player.getUniqueId());

					if (lastTimestamp != null && lastTimestamp + 5000 > timestamp.getTime()) {
						sender.sendMessage("Please wait a bit.");
					} else {
						doTeleportPlayer(sender, timestamp);
						

					}
					return true;
				} else {
					return true;
				}
			}

		}
		return true;
	}

	private boolean doTeleportPlayer(CommandSender sender, Timestamp timestamp) {
		Player player = (Player) sender;

		setBoundaries();
		Location loc = player.getLocation();

		Location newloc = loc;
	
		double newX = getRandomCoordinate(minX, maxX);
		double newZ = getRandomCoordinate(minZ, maxZ);

		newloc.setX(newX);
		newloc.setZ(newZ);

		World world = Bukkit.getWorld("world");
		
		double newY = world.getHighestBlockYAt(newloc);
		newloc.setY(newY);

		Block b = newloc.getBlock();
		
		for (int i = 5; i < 1; i--) {
		
						
			b = newloc.getBlock();

            if (!b.getType().equals(Material.LAVA)){
            	break;
            }

            newX = getRandomCoordinate(minX, maxX);
    		newZ = getRandomCoordinate(minZ, maxZ);

    		newloc.setX(newX);
    		newloc.setZ(newZ);
    		
    		newY = world.getHighestBlockYAt(newloc);
    		newloc.setY(newY);
		}
		
		newloc.setY(newY + 1);

		player.teleport(newloc);

		usages.put(player.getUniqueId(), timestamp.getTime());
		sender.sendMessage("Teleporting!");
		
		return true;
	}
	
	
	private double getRandomCoordinate(double min, double max) {
		return Math.floor(Math.random() * (max - min + 1) + min) + 0.5;
	}

}
