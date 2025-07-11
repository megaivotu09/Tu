package com.yourname.tutien.listeners;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import java.util.HashMap;
import java.util.Map;

public class MobKillListener implements Listener {
    private final TuTienPlugin plugin;
    private final Map<EntityType, Integer> linhKhiFromMob = new HashMap<>();

    public MobKillListener(TuTienPlugin plugin) {
        this.plugin = plugin;
        linhKhiFromMob.put(EntityType.ZOMBIE, 5);
        linhKhiFromMob.put(EntityType.SKELETON, 5);
        linhKhiFromMob.put(EntityType.SPIDER, 4);
        linhKhiFromMob.put(EntityType.CREEPER, 8);
        linhKhiFromMob.put(EntityType.ENDERMAN, 20);
        linhKhiFromMob.put(EntityType.BLAZE, 15);
        linhKhiFromMob.put(EntityType.WITHER, 5000);
        linhKhiFromMob.put(EntityType.ENDER_DRAGON, 10000);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();
        if (killer == null) return;
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(killer);
        if (data != null) {
            int baseLinhKhi = linhKhiFromMob.getOrDefault(entity.getType(), 1);
            long finalLinhKhi = (long) (baseLinhKhi * data.getLinhCan().getHeSoLinhKhi());
            if (finalLinhKhi > 0) {
                data.addLinhKhi(finalLinhKhi);
                killer.sendActionBar("§b+ " + finalLinhKhi + " Linh Khí");
            }
        }
    }
}
