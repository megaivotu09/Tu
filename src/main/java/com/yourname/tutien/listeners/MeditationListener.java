package com.yourname.tutien.listeners;

import com.yourname.tutien.TuTienPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.entity.EntityDismountEvent;

public class MeditationListener implements Listener {
    private final TuTienPlugin plugin;
    public MeditationListener(TuTienPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getMeditationManager().isMeditating(player)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
                plugin.getMeditationManager().stopMeditation(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getMeditationManager().isMeditating(player)) {
                plugin.getMeditationManager().stopMeditation(player);
            }
        }
    }

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getMeditationManager().isMeditating(player)) {
                plugin.getMeditationManager().stopMeditation(player);
            }
        }
    }
}
