package com.yourname.tutien.listeners;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerConnectionListener implements Listener {
    private final TuTienPlugin plugin;
    public PlayerConnectionListener(TuTienPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerDataManager().loadPlayerData(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) return;
                plugin.getScoreboardManager().setScoreboard(player);
                plugin.getBossBarManager().createBossBar(player);
                plugin.getAttributeManager().updatePlayerAttributes(player);
                plugin.getFlightManager().updatePlayerFlight(player);
                if (!player.hasPlayedBefore()) {
                    PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
                    if (data != null) {
                        player.sendMessage("\n§e[Hệ Thống] §fGiáng trần vào thế giới tu tiên, linh căn của bạn đã được thức tỉnh!");
                        player.sendMessage("§e[Hệ Thống] §fLinh căn của bạn là: " + data.getLinhCan().getTenHienThi() + "\n");
                    }
                }
            }
        }.runTaskLater(plugin, 40L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (plugin.getRemnantSoulManager().isSoul(player.getUniqueId())) {
            plugin.getRemnantSoulManager().dissipateSoul(player.getUniqueId());
        } else {
            plugin.getPlayerDataManager().saveAndUnloadPlayerData(player);
        }
        plugin.getBossBarManager().removeBossBar(player);
        plugin.getScoreboardManager().removeScoreboard(player);
        plugin.getFlightManager().removePlayerFromCache(player);
    }
}
