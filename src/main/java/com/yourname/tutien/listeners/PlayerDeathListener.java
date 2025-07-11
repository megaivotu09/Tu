package com.yourname.tutien.listeners;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener {
    private final TuTienPlugin plugin;
    public PlayerDeathListener(TuTienPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (plugin.getRemnantSoulManager().isSoul(player.getUniqueId())) {
            event.getDrops().clear();
            return;
        }

        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null) return;
        
        int canhGioiId = data.getTuLuyenInfo().getCanhGioi().getId();
        int tanHonId = plugin.getConfigManager().CHET_TAN_HON_ID;
        int phatId = plugin.getConfigManager().CHET_PHAT_ID;
        
        if (canhGioiId >= tanHonId) {
            event.setKeepInventory(true);
            event.getDrops().clear();
            event.setKeepLevel(true);
            plugin.getRemnantSoulManager().startSoulState(player, data);
        } else if (canhGioiId >= phatId) {
            handleSeverePenalty(player);
        } else {
            player.sendMessage("§aMay mắn thay, tu vi của bạn còn thấp, tử vong không làm tổn hại đạo cơ.");
        }
    }

    private void handleSeverePenalty(Player player) {
        player.sendMessage("§4§l[TRỌNG THƯƠNG] §cBạn đã tử vong, đạo cơ tổn hại nghiêm trọng, tu vi tiêu tán!");
        PlayerData newPlayerData = new PlayerData(player.getUniqueId());
        plugin.getPlayerDataManager().replacePlayerData(player.getUniqueId(), newPlayerData);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    player.sendMessage("§eLinh hồn tái tạo, bạn đã được chuyển thế trọng sinh với một linh căn mới!");
                    player.sendMessage("§fLinh căn mới của bạn là: " + newPlayerData.getLinhCan().getTenHienThi());
                    plugin.getAttributeManager().updatePlayerAttributes(player);
                }
            }
        }.runTaskLater(plugin, 20L);
    }
}
