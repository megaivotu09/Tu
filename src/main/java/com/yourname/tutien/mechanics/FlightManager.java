package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlightManager {
    private final TuTienPlugin plugin;
    private final Map<UUID, Boolean> flightStatusCache = new HashMap<>();

    public FlightManager(TuTienPlugin plugin) { this.plugin = plugin; }

    public void updatePlayerFlight(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            if (!player.getAllowFlight()) player.setAllowFlight(true);
            return;
        }
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null) {
            setCanFly(player, false, "");
            return;
        }
        int realmId = data.getTuLuyenInfo().getCanhGioi().getId();
        int tier = data.getTuLuyenInfo().getTang();
        if (realmId >= CanhGioi.TRUC_CO.getId()) {
            setCanFly(player, true, plugin.getConfigManager().getMessage("bat-dau-lang-khong"));
            return;
        }
        if (realmId == CanhGioi.LUYEN_KHI.getId() && tier >= plugin.getConfigManager().NGU_KIEM_YEU_CAU_TANG) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand.getType().name().endsWith("_SWORD")) {
                setCanFly(player, true, plugin.getConfigManager().getMessage("bat-dau-ngu-kiem"));
            } else {
                setCanFly(player, false, plugin.getConfigManager().getMessage("can-kiem-de-bay"));
            }
            return;
        }
        setCanFly(player, false, "");
    }

    private void setCanFly(Player player, boolean canFly, String message) {
        if (canFly) {
            if (!player.getAllowFlight()) {
                player.setAllowFlight(true);
                if (!message.isEmpty()) player.sendActionBar(message);
            }
        } else {
            if (player.getAllowFlight()) {
                if (player.isFlying()) player.setFlying(false);
                player.setAllowFlight(false);
                if (!message.isEmpty()) player.sendActionBar(message);
            }
        }
    }
    
    public void removePlayerFromCache(Player player) {
        flightStatusCache.remove(player.getUniqueId());
    }
}
