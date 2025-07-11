package com.yourname.tutien.storage;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.enums.LinhCan;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.io.File;
import java.io.IOException;

public class StorageManager {
    private final TuTienPlugin plugin;
    private final File dataFolder;

    public StorageManager(TuTienPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public void savePlayerData(Player player, PlayerData data) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        config.set("name", player.getName());
        config.set("canh-gioi", data.getTuLuyenInfo().getCanhGioi().name());
        config.set("tang", data.getTuLuyenInfo().getTang());
        config.set("linh-khi", data.getLinhKhi());
        config.set("linh-can", data.getLinhCan().name());
        try {
            config.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Khong the luu du lieu cho " + player.getName());
            e.printStackTrace();
        }
    }

    public PlayerData loadPlayerData(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (!playerFile.exists()) {
            PlayerData newData = new PlayerData(player.getUniqueId());
            savePlayerData(player, newData);
            return newData;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        CanhGioi canhGioi = CanhGioi.valueOf(config.getString("canh-gioi", "PHAM_NHAN"));
        int tang = config.getInt("tang", 1);
        long linhKhi = config.getLong("linh-khi", 0);
        LinhCan linhCan = LinhCan.valueOf(config.getString("linh-can", "HA_PHAM"));
        return new PlayerData(player.getUniqueId(), canhGioi, tang, linhKhi, linhCan);
    }
}
