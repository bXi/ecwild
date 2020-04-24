package net.bixxy.ecwild;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
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
		//TODO fix setBoundaries() to fire on Enable and on worldborder change
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
		if (cmd.getName().equalsIgnoreCase("wild")) {
			if (sender instanceof Player) {

				Player player = (Player) sender;
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());

				Long lastTimestamp = usages.get(player.getUniqueId());

				if (lastTimestamp != null && lastTimestamp + 5000 > timestamp.getTime()) {
					sender.sendMessage("Please wait a bit.");
				} else {

					setBoundaries();
					Location loc = player.getLocation();

					Location newloc = loc;

					double newX = getRandomCoordinate(minX, maxX);
					double newZ = getRandomCoordinate(minZ, maxZ);

					newloc.setX(newX);
					newloc.setZ(newZ);

					World world = Bukkit.getWorld("world");

					double newY = world.getHighestBlockYAt(newloc) + 1;

					newloc.setY(newY);

					player.teleport(newloc);

					usages.put(player.getUniqueId(), timestamp.getTime());
					sender.sendMessage("Teleporting!");

				}
				return true;
			}

		}
		return false;
	}

	private double getRandomCoordinate(double min, double max) {
		return Math.floor(Math.random() * (max - min + 1) + min) + 0.5;
	}

}
