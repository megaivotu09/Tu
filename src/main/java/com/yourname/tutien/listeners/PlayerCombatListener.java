package com.yourname.tutien.listeners;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.mechanics.DamageManager;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerCombatListener implements Listener {
    private final TuTienPlugin plugin;

    public PlayerCombatListener(TuTienPlugin plugin) { this.plugin = plugin; }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onRealmSuppression(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        
        if (plugin.getTribulationManager().isInTribulation(victim)) {
            plugin.getTribulationManager().addTamMa(victim, event.getFinalDamage());
        }

        PlayerData attackerData = plugin.getPlayerDataManager().getPlayerData(attacker);
        PlayerData victimData = plugin.getPlayerDataManager().getPlayerData(victim);

        if (attackerData == null || victimData == null) return;

        DamageManager damageManager = new DamageManager(attackerData, victimData);
        double originalDamage = event.getDamage();
        double newDamage = damageManager.calculateFinalDamage(originalDamage);

        event.setDamage(newDamage);

        if (damageManager.isAbsoluteDomination()) {
            attacker.sendActionBar("§4§l[UY ÁP]§c Cảnh giới của bạn hoàn toàn áp đảo đối phương!");
            victim.sendActionBar("§4§l[UY ÁP]§c Bạn bị cảnh giới đối phương áp đảo hoàn toàn!");
        }
    }
}
