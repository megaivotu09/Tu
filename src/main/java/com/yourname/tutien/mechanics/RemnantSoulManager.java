package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RemnantSoulManager {
    private final TuTienPlugin plugin;
    private final Map<UUID, RemnantSoulData> activeSouls = new HashMap<>();

    public RemnantSoulManager(TuTienPlugin plugin) { this.plugin = plugin; }
    public boolean isSoul(UUID uuid) { return activeSouls.containsKey(uuid); }
    public Map<UUID, RemnantSoulData> getActiveSouls() { return activeSouls; }
    public RemnantSoulData getSoulData(UUID uuid) { return activeSouls.get(uuid); }

    public void startSoulState(Player player, PlayerData originalData) {
        int canhGioiId = originalData.getTuLuyenInfo().getCanhGioi().getId();
        long baseTime = 300; // 5 phút gốc
        long bonusTime = (long)(canhGioiId - plugin.getConfigManager().CHET_TAN_HON_ID) * 120; // +2 phút mỗi cảnh giới trên mốc
        long linhKhiNeeded = (long) (originalData.getTuLuyenInfo().getCanhGioi().getLinhKhiChoTangMot() * 2.5);

        RemnantSoulData soulData = new RemnantSoulData(originalData, baseTime + bonusTime, linhKhiNeeded);
        activeSouls.put(player.getUniqueId(), soulData);
        plugin.getPlayerDataManager().unloadPlayerData(player);

        player.setGameMode(GameMode.SPECTATOR);
        player.sendTitle("§8§lTÀN HỒN", "§7Bạn đã trở thành tàn hồn, hãy tìm người giúp đỡ!", 10, 80, 20);
    }

    public void revivePlayer(Player player) {
        RemnantSoulData soulData = activeSouls.remove(player.getUniqueId());
        if (soulData == null) return;

        PlayerData originalData = soulData.getOriginalData();
        int originalRealmId = originalData.getTuLuyenInfo().getCanhGioi().getId();
        int originalTier = originalData.getTuLuyenInfo().getTang();
        int totalTiers = (originalRealmId > 0 ? (originalRealmId - 1) * 9 : 0) + (originalTier - 1);

        int newTotalTiers = (int) (totalTiers * 0.70);
        int newRealmId = (newTotalTiers / 9) + 1;
        int newTier = (newTotalTiers % 9) + 1;
        CanhGioi newCanhGioi = CanhGioi.PHAM_NHAN;
        for (CanhGioi cg : CanhGioi.values()) {
            if (cg.getId() == newRealmId) {
                newCanhGioi = cg;
                break;
            }
        }

        PlayerData revivedData = new PlayerData(player.getUniqueId(), newCanhGioi, newTier, 0, originalData.getLinhCan());
        plugin.getPlayerDataManager().loadRevivedPlayerData(player.getUniqueId(), revivedData);

        player.setGameMode(GameMode.SURVIVAL);
        plugin.getAttributeManager().updatePlayerAttributes(player);
        player.sendTitle("§a§lTÁI TẠO THÂN THỂ", "§fChúc mừng bạn đã trọng sinh!", 10, 80, 20);
    }

    public void dissipateSoul(UUID uuid) {
        activeSouls.remove(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            player.sendTitle("§4§lHỒN PHI PHÁCH TÁN", "§cThời gian đã hết, tu vi của bạn đã tiêu tán.", 10, 80, 20);
            plugin.getPlayerDataManager().resetPlayerData(player);
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(0.0);
        }
    }

    public void transferLinhKhi(Player helper, Player soulPlayer, long amount) {
        PlayerData helperData = plugin.getPlayerDataManager().getPlayerData(helper);
        if (helperData == null || helperData.getLinhKhi() < amount) {
            helper.sendMessage("§cBạn không đủ linh khí!");
            return;
        }
        RemnantSoulData soulData = getSoulData(soulPlayer.getUniqueId());
        if (soulData == null) {
            helper.sendMessage("§cNgười này không phải là tàn hồn.");
            return;
        }
        helperData.setLinhKhi(helperData.getLinhKhi() - amount);
        soulData.addLinhKhi(amount);
        soulData.addTime(amount / 100);

        helper.sendMessage("§aBạn đã truyền §e" + amount + "§a linh khí cho §b" + soulPlayer.getName());
        soulPlayer.sendMessage("§aBạn nhận được §e" + amount + "§a linh khí từ §b" + helper.getName());
        
        if (soulData.isReadyToRevive()) {
            revivePlayer(soulPlayer);
        }
    }
}
