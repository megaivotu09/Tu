package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin; import com.yourname.tutien.enums.CanhGioi; import com.yourname.tutien.player.PlayerData; import org.bukkit.Bukkit; import org.bukkit.Location; import org.bukkit.entity.Player; import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap; import java.util.Map; import java.util.UUID;

public class TribulationManager {

private final TuTienPlugin plugin;
private final Map<UUID, Double> playersInTribulation = new HashMap<>();

public TribulationManager(TuTienPlugin plugin) {
    this.plugin = plugin;
}

public boolean isInTribulation(Player player) {
    return playersInTribulation.containsKey(player.getUniqueId());
}

public void addTamMa(Player player, double damage) {
    // TODO: Implement logic to add Tam Ma points
}

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

    // Bay lên không trung
    Location skyLocation = player.getLocation().add(0, 20, 0);
    player.setAllowFlight(true);
    player.setFlying(true);
    player.teleport(skyLocation);

    playersInTribulation.put(player.getUniqueId(), 0.0);
    player.sendTitle("§4§lLÔI KIẾP", "§cThiên kiếp sắp giáng lâm, hãy chuẩn bị!", 10, 70, 20);

    final long excessLinhKhi = data.getExcessLinhKhi();
    final int durationInSeconds = 15;
    final CanhGioi nextCanhGioi = CanhGioi.getNext(data.getTuLuyenInfo().getCanhGioi());
    final int baseLightningStrikes = 5 + (nextCanhGioi.getId() * 2);
    final double lightningMultiplier = 1.0 + (excessLinhKhi / 20000.0 / 100.0);
    final int totalLightningStrikes = Math.max(5, Math.min((int) (baseLightningStrikes * lightningMultiplier), 40));

    new BukkitRunnable() {
        private int strikesLeft = totalLightningStrikes;
        private int ticksLeft = durationInSeconds * 20;

        @Override
        public void run() {
            if (!player.isOnline() || !playersInTribulation.containsKey(player.getUniqueId())) {
                failTribulation(player, "§cĐộ kiếp bị gián đoạn!");
                this.cancel();
                return;
            }

            if (!player.isFlying()) {
                player.setFlying(true);
            }

            if (ticksLeft <= 0 || player.isDead()) {
                handleTribulationResult(player);
                this.cancel();
                return;
            }

            if (strikesLeft > 0 && ticksLeft > 0 && (ticksLeft % (durationInSeconds * 20 / totalLightningStrikes) == 0)) {
                player.getWorld().strikeLightning(player.getLocation());
                strikesLeft--;
            }

            ticksLeft--;
        }
    }.runTaskTimer(plugin, 40L, 1L);
}

private void handleTribulationResult(Player player) {
    double tamMaValue = playersInTribulation.getOrDefault(player.getUniqueId(), 0.0);

    // Tắt chế độ bay
    if (player.isFlying()) {
        player.setFlying(false);
    }

    plugin.getFlightManager().updatePlayerFlight(player);

    if (player.isDead()) {
        failTribulation(player, "§cBạn đã thất bại dưới lôi kiếp!");
        return;
    }

    if (tamMaValue >= plugin.getConfigManager().LOI_KIEP_NGUONG_NANG) {
        tauHoaNhapMa(player);
    } else if (tamMaValue >= plugin.getConfigManager().LOI_KIEP_NGUONG_NHE) {
        kinhMachRoiLoan(player);
    } else {
        succeedTribulation(player);
    }
}

private void succeedTribulation(Player player) {
    // TODO: Implement logic when tribulation succeeds
}

private void tauHoaNhapMa(Player player) {
    // TODO: Implement logic when player gets a severe backlash
}

private void kinhMachRoiLoan(Player player) {
    // TODO: Implement logic when player gets a minor backlash
}

public void failTribulation(Player player, String message) {
    // TODO: Implement fail logic
    player.sendMessage(message);
    playersInTribulation.remove(player.getUniqueId());
}

}

