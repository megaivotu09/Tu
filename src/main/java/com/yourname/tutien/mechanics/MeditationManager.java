package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MeditationManager {
    private final TuTienPlugin plugin;
    private final Set<UUID> meditatingPlayers = new HashSet<>();
    private final Map<UUID, ArmorStand> meditationSeats = new HashMap<>();
    
    public MeditationManager(TuTienPlugin plugin) { this.plugin = plugin; }
    public boolean isMeditating(Player player) { return meditatingPlayers.contains(player.getUniqueId()); }
    public Set<UUID> getMeditatingPlayers() { return meditatingPlayers; }

    public void startMeditation(Player player) {
        if (isMeditating(player)) {
            stopMeditation(player);
            return;
        }
        if (!player.isOnGround()) {
            player.sendMessage(plugin.getConfigManager().getMessage("khong-the-thien"));
            return;
        }
        Location seatLocation = player.getLocation().clone().subtract(0, 0.5, 0);
        ArmorStand seat = (ArmorStand) player.getWorld().spawnEntity(seatLocation, EntityType.ARMOR_STAND);
        seat.setGravity(false);
        seat.setVisible(false);
        seat.setInvulnerable(true);
        seat.setMarker(true);
        seat.addPassenger(player);
        meditatingPlayers.add(player.getUniqueId());
        meditationSeats.put(player.getUniqueId(), seat);
        player.sendMessage(plugin.getConfigManager().getMessage("thien-bat-dau"));
        player.sendMessage("§7Gõ §e/tuluyen §7lần nữa hoặc di chuyển để thoát.");
    }

    public void stopMeditation(Player player) {
        if (!isMeditating(player)) return;
        ArmorStand seat = meditationSeats.remove(player.getUniqueId());
        if (seat != null) {
            seat.removePassenger(player);
            seat.remove();
        }
        meditatingPlayers.remove(player.getUniqueId());
        player.sendMessage(plugin.getConfigManager().getMessage("thien-ket-thuc"));
    }
}
