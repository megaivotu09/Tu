package com.yourname.tutien.tasks;
import com.yourname.tutien.TuTienPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.UUID;
public class ParticleTask extends BukkitRunnable {
    private final TuTienPlugin plugin;
    private double angle = 0;
    public ParticleTask(TuTienPlugin plugin) { this.plugin = plugin; }
    @Override
    public void run() {
        angle += Math.PI / 16;
        if (angle > 2 * Math.PI) angle = 0;
        for (UUID uuid : plugin.getMeditationManager().getMeditatingPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                Location playerLoc = player.getLocation();
                double radius = 0.8;
                double x = playerLoc.getX() + radius * Math.cos(angle);
                double y = playerLoc.getY() + 0.5 + (angle / 4);
                double z = playerLoc.getZ() + radius * Math.sin(angle);
                double x2 = playerLoc.getX() + radius * Math.cos(angle + Math.PI);
                double y2 = playerLoc.getY() + 0.5 + ((angle + Math.PI) / 4);
                double z2 = playerLoc.getZ() + radius * Math.sin(angle + Math.PI);
                player.getWorld().spawnParticle(Particle.END_ROD, x, y, z, 1, 0, 0, 0, 0);
                player.getWorld().spawnParticle(Particle.END_ROD, x2, y2, z2, 1, 0, 0, 0, 0);
            }
        }
    }
}
