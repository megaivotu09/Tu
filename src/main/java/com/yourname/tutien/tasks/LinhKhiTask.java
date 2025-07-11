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
        for (Player player : Bukkit.getOnlinePlayers()) {
            // --- THAY ĐỔI QUAN TRỌNG TẠI ĐÂY ---
            // 1. Bỏ qua nếu là Tàn hồn
            if (plugin.getRemnantSoulManager().isSoul(player.getUniqueId())) {
                continue;
            }

            // 2. Chỉ xử lý nếu người chơi đang thiền định (dùng /tuluyen)
            if (!plugin.getMeditationManager().isMeditating(player)) {
                continue; // Bỏ qua người chơi này nếu họ không thiền
            }
            
            // --- Logic cũ chỉ chạy khi các điều kiện trên được thỏa mãn ---
            PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
            if (data != null) {
                // Task chạy mỗi 5 giây (100 ticks)
                int linhKhiCongThemKhiThien = plugin.getConfigManager().THIEN_DINH_LINH_KHI_CONG_THEM * 5;

                // Lượng linh khí gốc khi thiền
                double baseAmount = 1 + data.getTuLuyenInfo().getCanhGioi().getId();
                baseAmount += linhKhiCongThemKhiThien;

                // Nhân với hệ số của linh căn
                double finalAmount = baseAmount * data.getLinhCan().getHeSoLinhKhi();

                // Nhân tiếp với hệ số bonus thiền
                finalAmount *= plugin.getConfigManager().THIEN_DINH_BONUS;

                if (finalAmount > 0) {
                    data.addLinhKhi((long) finalAmount);
                }
            }
        }
    }
}
