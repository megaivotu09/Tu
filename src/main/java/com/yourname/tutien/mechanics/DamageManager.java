package com.yourname.tutien.mechanics;

import com.yourname.tutien.player.PlayerData;

public class DamageManager {
    private final PlayerData attackerData;
    private final PlayerData victimData;

    public DamageManager(PlayerData attackerData, PlayerData victimData) {
        this.attackerData = attackerData;
        this.victimData = victimData;
    }

    public double calculateFinalDamage(double originalDamage) {
        int attackerRealmId = attackerData.getTuLuyenInfo().getCanhGioi().getId();
        int attackerTier = attackerData.getTuLuyenInfo().getTang();
        int victimRealmId = victimData.getTuLuyenInfo().getCanhGioi().getId();
        int victimTier = victimData.getTuLuyenInfo().getTang();
        
        int attackerLevel = attackerRealmId * 10 + attackerTier;
        int victimLevel = victimRealmId * 10 + victimTier;
        
        if (attackerLevel == victimLevel) return originalDamage;
        
        int levelDifference = attackerLevel - victimLevel;
        double damageMultiplier = 1.0 + (levelDifference * 0.03);
        
        if (damageMultiplier < 0.1) damageMultiplier = 0.1;
        return originalDamage * damageMultiplier;
    }

    public boolean isAbsoluteDomination() {
        int realmDifference = attackerData.getTuLuyenInfo().getCanhGioi().getId() - victimData.getTuLuyenInfo().getCanhGioi().getId();
        return realmDifference >= 2;
    }
}
