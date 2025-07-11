package com.yourname.tutien.tasks;
import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.mechanics.RemnantSoulData;
import com.yourname.tutien.mechanics.RemnantSoulManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashSet;
import java.util.UUID;
public class SoulDecayTask extends BukkitRunnable {
    private final TuTienPlugin plugin;
    public SoulDecayTask(TuTienPlugin plugin) { this.plugin = plugin; }
    @Override
    public void run() {
        RemnantSoulManager soulManager = plugin.getRemnantSoulManager();
        for (UUID uuid : new HashSet<>(soulManager.getActiveSouls().keySet())) {
            RemnantSoulData soulData = soulManager.getSoulData(uuid);
            if (soulData == null) continue;
            soulData.addTime(-1);
            if (soulData.getTimeRemaining() <= 0) {
                soulManager.dissipateSoul(uuid);
                continue;
            }
            Player soulPlayer = Bukkit.getPlayer(uuid);
            if (soulPlayer != null && soulPlayer.isOnline()) {
                long minutes = soulData.getTimeRemaining() / 60;
                long seconds = soulData.getTimeRemaining() % 60;
                soulPlayer.sendActionBar(String.format("§8Tàn Hồn Sắp Tiêu Tán: §c%02d:%02d", minutes, seconds));
            }
        }
    }
}
