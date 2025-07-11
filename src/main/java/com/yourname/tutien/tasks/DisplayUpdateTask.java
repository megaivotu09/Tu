package com.yourname.tutien.tasks;
import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
public class DisplayUpdateTask extends BukkitRunnable {
    private final TuTienPlugin plugin;
    public DisplayUpdateTask(TuTienPlugin plugin) { this.plugin = plugin; }
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.getScoreboardManager().updateScoreboard(player);
            plugin.getBossBarManager().updateBossBar(player);
            if (plugin.getRemnantSoulManager().isSoul(player.getUniqueId())) continue;
            PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
            if (data != null) {
                player.sendActionBar("§b§lCảnh Giới: §f" + data.getTuLuyenInfo().getTenHienThiDayDu());
            }
        }
    }
}
