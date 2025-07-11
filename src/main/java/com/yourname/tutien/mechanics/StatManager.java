package com.yourname.tutien.mechanics;

import com.yourname.tutien.player.TuLuyenInfo;

public class StatManager {
    private final TuLuyenInfo info;

    public StatManager(TuLuyenInfo info) { this.info = info; }

    public double getMaxHealth() {
        double baseHealth = 20.0;
        if (info.getCanhGioi().getId() == 0) return baseHealth;
        double daiCanhGioiBonus = info.getCanhGioi().getId() * 2.0;
        double tieuCanhGioiBonus = (info.getTang() - 1) * 0.5;
        return baseHealth + daiCanhGioiBonus + tieuCanhGioiBonus;
    }

    public double getDefense() {
        if (info.getCanhGioi().getId() == 0) return 0.0;
        double daiCanhGioiBonus = info.getCanhGioi().getId() * 1.0;
        double tieuCanhGioiBonus = (info.getTang() - 1) * 0.2;
        return Math.min(20.0, daiCanhGioiBonus + tieuCanhGioiBonus);
    }

    public double getAttackDamage() {
        if (info.getCanhGioi().getId() == 0) return 0.0;
        double daiCanhGioiBonus = info.getCanhGioi().getId() * 0.5;
        double tieuCanhGioiBonus = (info.getTang() - 1) * 0.1;
        return daiCanhGioiBonus + tieuCanhGioiBonus;
    }
}
