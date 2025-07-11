package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location; // Thêm import
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TribulationManager {
    // ... các biến và constructor giữ nguyên ...
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
            player.sendMessage("§cBạn chưa đủ điều kiện để độ kiếp!");
            return;
        }

        // --- (TÍNH NĂNG MỚI) BAY LÊN KHÔNG TRUNG ---
        // Lấy vị trí an toàn trên không
        Location skyLocation = player.getLocation().add(0, 20, 0);
        // Tạm thời cho phép bay và tự động bật chế độ bay
        player.setAllowFlight(true);
        player.setFlying(true);
        // Dịch chuyển người chơi lên trời
        player.teleport(skyLocation);
        // -----------------------------------------

        playersInTribulation.put(player.getUniqueId(), 0.0);
        player.sendTitle("§4§lLÔI KIẾP", "§cThiên kiếp sắp giáng lâm, hãy chuẩn bị!", 10, 70, 20);

        // ... phần tính toán sức mạnh lôi kiếp giữ nguyên ...
        long excessLinhKhi = data.getExcessLinhKhi();
        int durationInSeconds = 15;
        CanhGioi nextCanhGioi = CanhGioi.getNext(data.getTuLuyenInfo().getCanhGioi());
        int baseLightningStrikes = 5 + (nextCanhGioi.getId() * 2);
        double lightningMultiplier = 1.0 + (excessLinhKhi / 20000.0 / 100.0);
        int totalLightningStrikes = (int) (baseLightningStrikes * lightningMultiplier);
        totalLightningStrikes = Math.max(5, Math.min(totalLightningStrikes, 40));

        new BukkitRunnable() {
            // ...
            @Override
            public void run() {
                // ... logic chạy task giữ nguyên ...
                if (!player.isOnline() || !playersInTribulation.containsKey(player.getUniqueId())) {
                    failTribulation(player, "§cĐộ kiếp bị gián đoạn!");
                    this.cancel();
                    return;
                }
                // (MỚI) Giữ người chơi ở trên không
                if (player.isFlying() == false) {
                     player.setFlying(true);
                }
                if (ticksLeft <= 0 || player.isDead()) {
                    handleTribulationResult(player);
                    this.cancel();
                    return;
                }
                if (strikesLeft > 0 && ticksLeft > 0 && (ticksLeft % (durationInSeconds * 20 / totalLightningStrikes) == 0)) {
                    // Giáng sét ngay vị trí người chơi đang bay
                    player.getWorld().strikeLightning(player.getLocation());
                    strikesLeft--;
                }
                ticksLeft--;
            }
        }.runTaskTimer(plugin, 40L, 1L);
    }
    
    // HÀM QUAN TRỌNG CẦN SỬA
    private void handleTribulationResult(Player player) {
        double tamMaValue = playersInTribulation.getOrDefault(player.getUniqueId(), 0.0);
        
        // Dù thành công hay thất bại, cũng phải xử lý trạng thái bay
        // Tắt chế độ bay trước
        if (player.isFlying()) {
            player.setFlying(false);
        }
        // Sau đó gọi FlightManager để nó quyết định người chơi có được phép bay tiếp không
        plugin.getFlightManager().updatePlayerFlight(player);

        if (player.isDead()) {
            failTribulation(player, "§cBạn đã thất bại dưới lôi kiếp!");
            return;
        }
        
        if (tamMaValue >= plugin.getConfigManager().LOI_KIEP_NGUONG_NANG) tauHoaNhapMa(player);
        else if (tamMaValue >= plugin.getConfigManager().LOI_KIEP_NGUONG_NHE) kinhMachRoiLoan(player);
        else succeedTribulation(player);
    }
    
    // Các hàm còn lại giữ nguyên
    private void succeedTribulation(Player player) { /* ... */ }
    private void tauHoaNhapMa(Player player) { /* ... */ }
    private void kinhMachRoiLoan(Player player) { /* ... */ }
    public void failTribulation(Player player, String message) { /* ... */ }
}
