package com.yourname.tutien.display;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.mechanics.RemnantSoulData;
import com.yourname.tutien.player.PlayerData;
import com.yourname.tutien.player.TuLuyenInfo;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {
    private final TuTienPlugin plugin;
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

    public BossBarManager(TuTienPlugin plugin) {
        this.plugin = plugin;
    }

    public void createBossBar(Player player) {
        if (playerBossBars.containsKey(player.getUniqueId())) return;
        BossBar bossBar = Bukkit.createBossBar("§aĐang tải...", BarColor.GREEN, BarStyle.SOLID);
        bossBar.addPlayer(player);
        playerBossBars.put(player.getUniqueId(), bossBar);
        updateBossBar(player);
    }

    public void removeBossBar(Player player) {
        BossBar bossBar = playerBossBars.remove(player.getUniqueId());
        if (bossBar != null) bossBar.removeAll();
    }

    public void updateBossBar(Player player) {
        BossBar bossBar = playerBossBars.get(player.getUniqueId());
        if (bossBar == null) return;

        if (plugin.getRemnantSoulManager().isSoul(player.getUniqueId())) {
            updateSoulBossBar(bossBar, player);
            return;
        }

        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            bossBar.setTitle("§aĐang tải dữ liệu...");
            bossBar.setProgress(0);
            return;
        }

        if (plugin.getTribulationManager().isInTribulation(player)) {
            AttributeInstance maxHealthAttribute = player.getAttribute(getAttributeSafely("GENERIC_MAX_HEALTH"));
            double maxHealth = (maxHealthAttribute != null) ? maxHealthAttribute.getValue() : 20.0;
            bossBar.setTitle("§4§lĐANG ĐỘ KIẾP - CHỐNG CỰ LÔI KIẾP!");
            bossBar.setColor(BarColor.RED);
            bossBar.setProgress(player.getHealth() / maxHealth);
            return;
        }

        if (data.canGrandBreakthrough()) {
            bossBar.setTitle("§d§lSẴN SÀNG ĐỘT PHÁ - Gõ /dotpha");
            bossBar.setColor(BarColor.PURPLE);
            bossBar.setProgress(1.0);
            return;
        }

        updateNormalBossBar(bossBar, data);
    }

    private void updateNormalBossBar(BossBar bossBar, PlayerData data) {
        TuLuyenInfo info = data.getTuLuyenInfo();
        long linhKhiCan = info.getLinhKhiCanThiet();

        if (bossBar.getColor() != BarColor.GREEN) bossBar.setColor(BarColor.GREEN);

        if (linhKhiCan == Long.MAX_VALUE) {
            bossBar.setTitle("§6§lVIÊN MÃN - Đỉnh Cao Tu Tiên");
            bossBar.setColor(BarColor.YELLOW);
            bossBar.setProgress(1.0);
            return;
        }

        long linhKhiHienTai = data.getLinhKhi();
        double progress = (double) linhKhiHienTai / linhKhiCan;
        progress = Math.max(0.0, Math.min(1.0, progress));

        bossBar.setProgress(progress);
        bossBar.setTitle(String.format("§aLinh Khí: §f%s §7/ §c%s",
                formatNumber(linhKhiHienTai), formatNumber(linhKhiCan)));
    }

    private void updateSoulBossBar(BossBar bossBar, Player player) {
        RemnantSoulData soulData = plugin.getRemnantSoulManager().getSoulData(player.getUniqueId());
        if (soulData == null) return;

        bossBar.setTitle("§8TÁI TẠO THÂN THỂ");
        bossBar.setColor(BarColor.WHITE);
        double progress = (double) soulData.getLinhKhiCollected() / soulData.getLinhKhiNeeded();
        bossBar.setProgress(Math.min(1.0, progress));
    }

    private String formatNumber(long number) {
        return numberFormat.format(number);
    }

    private Attribute getAttributeSafely(String name) {
        try {
            return Attribute.valueOf(name);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Không tìm thấy attribute: " + name);
            return null;
        }
    }
}
