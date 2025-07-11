package com.yourname.tutien.player;

import com.yourname.tutien.TuTienPlugin;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final TuTienPlugin plugin;
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerDataManager(TuTienPlugin plugin) { this.plugin = plugin; }

    public void loadPlayerData(Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerData data = plugin.getStorageManager().loadPlayerData(player);
            playerDataMap.put(player.getUniqueId(), data);
            player.sendMessage("§a[Tu Tiên] §fĐã tải xong dữ liệu tu tiên của bạn.");
        });
    }

    public void saveAndUnloadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        if (playerDataMap.containsKey(uuid)) {
            PlayerData data = playerDataMap.get(uuid);
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                plugin.getStorageManager().savePlayerData(player, data);
                playerDataMap.remove(uuid);
            });
        }
    }

    public void unloadPlayerData(Player player) {
        playerDataMap.remove(player.getUniqueId());
    }

    public void loadRevivedPlayerData(UUID uuid, PlayerData data) {
        playerDataMap.put(uuid, data);
    }

    public void resetPlayerData(Player player) {
        PlayerData newPlayerData = new PlayerData(player.getUniqueId());
        playerDataMap.put(player.getUniqueId(), newPlayerData);
        plugin.getAttributeManager().updatePlayerAttributes(player);
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public void replacePlayerData(UUID uuid, PlayerData newData) {
        playerDataMap.put(uuid, newData);
    }
}
