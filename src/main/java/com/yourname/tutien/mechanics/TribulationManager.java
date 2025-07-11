package com.yourname.tutien.mechanics;

// ... các import giữ nguyên ...
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
    // ... các biến và constructor giữ nguyên ...
    private final TuTienPlugin plugin;
    private final Map<UUID, Double> playersInTribulation = new HashMap<>();
    public TribulationManager(TuTienPlugin plugin) { this.plugin = plugin; }
    public boolean isInTribulation(Player player) { return playersInTribulation.containsKey(player.getUniqueId()); }
    public void addTamMa(Player player, double damage) { /*...*/ }
    public void startTribulation(Player player) { /*...*/ }
    
    // HÀM QUAN TRỌNG CẦN SỬA
    private void handleTribulationResult(Player player) {
        double tamMaValue = playersInTribulation.getOrDefault(player.getUniqueId(), 0.0);
        
        if (player.isFlying()) {
            player.setFlying(false);
        }
        plugin.getFlightManager().updatePlayerFlight(player);

        if (player.isDead()) {
            failTribulation(player, "§cBạn đã thất bại dưới lôi kiếp!");
            return;
        }
        
        if (tamMaValue >= plugin.getConfigManager().LOI_KIEP_NGUONG_NANG) tauHoaNhapMa(player);
        else if (tamMaValue >= plugin.getConfigManager().LOI_KIEP_NGUONG_NHE) kinhMachRoiLoan(player);
        else succeedTribulation(player);
    }

    private void succeedTribulation(Player player) {
        playersInTribulation.remove(player.getUniqueId());
        // CẬP NHẬT BOSSBAR NGAY LẬP TỨC
        plugin.getBossBarManager().updateBossBar(player);
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data != null) {
            String canhGioiCu = data.getTuLuyenInfo().getTenHienThiDayDu();
            data.performGrandBreakthrough();
            String canhGioiMoi = data.getTuLuyenInfo().getTenHienThiDayDu();
            player.sendTitle("§a§lTHÀNH CÔNG", "§fChúc mừng đột phá lên §e" + canhGioiMoi, 10, 80, 20);
            Bukkit.broadcastMessage(String.format("§e[Chúc Mừng] §fĐạo hữu §b%s§f đã vượt qua thiên kiếp, từ %s đột phá lên %s!", player.getName(), canhGioiCu, canhGioiMoi));
            plugin.getAttributeManager().updatePlayerAttributes(player);
            plugin.getFlightManager().updatePlayerFlight(player);
        }
    }
    
    private void tauHoaNhapMa(Player player) {
        playersInTribulation.remove(player.getUniqueId());
        // CẬP NHẬT BOSSBAR NGAY LẬP TỨC
        plugin.getBossBarManager().updateBossBar(player);

        plugin.getPlayerDataManager().resetPlayerData(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1));
        Bukkit.broadcastMessage("§4§l[BI KỊCH] §cDo bị ngoại giới can nhiễu, đạo hữu §b" + player.getName() + " §cđã tẩu hỏa nhập ma, tu vi tiêu tán, trở thành phế nhân!");
    }
    
    private void kinhMachRoiLoan(Player player) {
        playersInTribulation.remove(player.getUniqueId());
        // CẬP NHẬT BOSSBAR NGAY LẬP TỨC
        plugin.getBossBarManager().updateBossBar(player);

        player.sendMessage("§cDo bị can thiệp, kinh mạch của bạn bị rối loạn, đột phá thất bại!");
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data != null) {
            long penalty = (long) (data.getLinhKhi() * plugin.getConfigManager().LOI_KIEP_PHAT_NHE);
            data.setLinhKhi(data.getLinhKhi() - penalty);
            player.sendMessage(String.format("§cBạn đã mất §e%,d§c linh khí!", penalty));
        }
    }

    public void failTribulation(Player player, String message) {
        playersInTribulation.remove(player.getUniqueId());
        // CẬP NHẬT BOSSBAR NGAY LẬP TỨC
        plugin.getBossBarManager().updateBossBar(player);

        player.sendMessage(message);
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data != null) {
            long penalty = (long) (data.getLinhKhi() * 0.3);
            data.setLinhKhi(data.getLinhKhi() - penalty);
            player.sendMessage(String.format("§cBạn đã mất §e%,d§c linh khí!", penalty));
        }
    }
}
