package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TribulationManager {
    private final TuTienPlugin plugin;
    private final Map<UUID, Double> playersInTribulation = new HashMap<>();

    public TribulationManager(TuTienPlugin plugin) { this.plugin = plugin; }
    public boolean isInTribulation(Player player) { return playersInTribulation.containsKey(player.getUniqueId()); }
    public void addTamMa(Player player, double damage) { /* ... */ }

    // HÀM QUAN TRỌNG CẦN SỬA
    public void startTribulation(Player player) {
        if (isInTribulation(player)) {
            player.sendMessage("§cBạn đang trong quá trình độ kiếp!");
            return;
        }
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null || !data.canGrandBreakthrough()) {
            player.sendMessage("§cBạn chưa đủ điều kiện để đột phá!");
            return;
        }

        // --- (THAY ĐỔI TẠI ĐÂY) KIỂM TRA CẢNH GIỚI PHÀM NHÂN ---
        if (data.getTuLuyenInfo().getCanhGioi() == CanhGioi.PHAM_NHAN) {
            // Nếu là Phàm Nhân, đột phá ngay lập tức không cần lôi kiếp
            succeedTribulation(player);
            return; // Kết thúc hàm tại đây
        }
        // --- KẾT THÚC THAY ĐỔI ---

        // Logic Lôi Kiếp chỉ chạy cho các cảnh giới từ Luyện Khí trở lên
        Location skyLocation = player.getLocation().add(0, 20, 0);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.teleport(skyLocation);

        playersInTribulation.put(player.getUniqueId(), 0.0);
        player.sendTitle("§4§lLÔI KIẾP", "§cThiên kiếp sắp giáng lâm, hãy chuẩn bị!", 10, 70, 20);

        long excessLinhKhi = data.getExcessLinhKhi();
        int durationInSeconds = plugin.getConfigManager().LOI_KIEP_THOI_GIAN;
        CanhGioi nextCanhGioi = CanhGioi.getNext(data.getTuLuyenInfo().getCanhGioi());
        int baseLightningStrikes = 5 + (nextCanhGioi.getId() * 2);
        double lightningMultiplier = 1.0 + (excessLinhKhi / 20000.0 / 100.0);
        int totalLightningStrikes = (int) (baseLightningStrikes * lightningMultiplier);
        totalLightningStrikes = Math.max(5, Math.min(totalLightningStrikes, 40));

        new BukkitRunnable() {
            // ... logic task giữ nguyên ...
        }.runTaskTimer(plugin, 40L, 1L);
    }
    
    // ... các hàm còn lại giữ nguyên ...
    private void handleTribulationResult(Player player) { /*...*/ }
    private void succeedTribulation(Player player) { /*...*/ }
    private void tauHoaNhapMa(Player player) { /*...*/ }
    private void kinhMachRoiLoan(Player player) { /*...*/ }
    public void failTribulation(Player player, String message) { /*...*/ }
}
