package com.yourname.tutien.tasks;
import com.yourname.tutien.TuTienPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
public class FlightCheckTask extends BukkitRunnable {
    private final TuTienPlugin plugin;
    public FlightCheckTask(TuTienPlugin plugin) { this.plugin = plugin; }
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.getFlightManager().updatePlayerFlight(player);
        }
    }
}
