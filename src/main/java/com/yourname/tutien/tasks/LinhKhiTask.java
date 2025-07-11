package com.yourname.tutien.tasks;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LinhKhiTask extends BukkitRunnable {
    private final TuTienPlugin plugin;

    public LinhKhiTask(TuTienPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Task này chạy mỗi 5 giây (100 ticks), không phải 1 giây.
        // Để cộng 3 linh khí mỗi giây, chúng ta cần cộng 3 * 5 = 15 linh khí mỗi lần task chạy.
        int linhKhiCongThemKhiThien = plugin.getConfigManager().THIEN_DINH_LINH_KHI_CONG_THEM * 5;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.getRemnantSoulManager().isSoul(player.getUniqueId())) continue;

            PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
            if (data != null) {
                // Lượng linh khí gốc nhận được khi AFK
                double baseAmount = 1 + data.getTuLuyenInfo().getCanhGioi().getId();
                
                // (THAY ĐỔI TẠI ĐÂY)
                // Nếu đang thiền, cộng thêm lượng linh khí cố định vào lượng gốc
                if (plugin.getMeditationManager().isMeditating(player)) {
                    baseAmount += linhKhiCongThemKhiThien;
                }

                // Nhân với hệ số của linh căn
                double finalAmount = baseAmount * data.getLinhCan().getHeSoLinhKhi();

                // Nếu đang thiền, nhân tiếp với hệ số bonus
                if (plugin.getMeditationManager().isMeditating(player)) {
                    finalAmount *= plugin.getConfigManager().THIEN_DINH_BONUS;
                }

                if (finalAmount > 0) {
                    data.addLinhKhi((long) finalAmount);
                }
            }
        }
    }
}
