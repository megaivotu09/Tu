package com.yourname.tutien.player;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.enums.LinhCan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private TuLuyenInfo tuLuyenInfo;
    private long linhKhi;
    private LinhCan linhCan;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.tuLuyenInfo = new TuLuyenInfo(CanhGioi.PHAM_NHAN, 1);
        this.linhKhi = 0;
        this.linhCan = LinhCan.phanLoaiNgauNhien();
    }

    public PlayerData(UUID uuid, CanhGioi canhGioi, int tang, long linhKhi, LinhCan linhCan) {
        this.uuid = uuid;
        this.tuLuyenInfo = new TuLuyenInfo(canhGioi, tang);
        this.linhKhi = linhKhi;
        this.linhCan = linhCan;
    }

    public void addLinhKhi(long amount) {
        if (amount > 0) this.linhKhi += amount;
        handleTierBreakthrough();
    }

    private void handleTierBreakthrough() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || tuLuyenInfo.getCanhGioi() == CanhGioi.PHAM_NHAN) {
            return;
        }

        while (tuLuyenInfo.getTang() < 9 && this.linhKhi >= tuLuyenInfo.getLinhKhiCanThiet()) {
            long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
            this.linhKhi -= linhKhiCanThiet;
            this.tuLuyenInfo.dotPha();
            player.sendMessage("§b[Tiểu Đột Phá] §fLinh khí ngưng tụ, đạo hữu đã đột phá lên §e" + tuLuyenInfo.getTenHienThiDayDu());
            updateAllAttributes(player);
        }
    }

    public boolean canGrandBreakthrough() {
        boolean isAtBreakthroughPoint = (tuLuyenInfo.getCanhGioi() == CanhGioi.PHAM_NHAN || tuLuyenInfo.getTang() == 9);
        if (!isAtBreakthroughPoint) {
            return false;
        }

        long linhKhiCanThietGoc = tuLuyenInfo.getLinhKhiCanThiet();
        if (linhKhiCanThietGoc == Long.MAX_VALUE) {
            return false;
        }

        double multiplier = TuTienPlugin.getInstance().getConfigManager().DOT_PHA_VUOT_MOC;
        long nguongYeuCau = (long) (linhKhiCanThietGoc * multiplier);
        return this.linhKhi >= nguongYeuCau;
    }

    public void performGrandBreakthrough() {
        long linhKhiDaDung = this.tuLuyenInfo.getLinhKhiCanThiet();
        this.tuLuyenInfo.dotPha();
        this.setLinhKhi(this.getLinhKhi() - linhKhiDaDung); // Sửa lại logic trừ linh khí

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            updateAllAttributes(player);
        }
    }

    private void updateAllAttributes(Player player) {
        TuTienPlugin.getInstance().getAttributeManager().updatePlayerAttributes(player);
        TuTienPlugin.getInstance().getFlightManager().updatePlayerFlight(player);
    }

    public long getExcessLinhKhi() {
        if (!canGrandBreakthrough()) return 0;
        return this.linhKhi - tuLuyenInfo.getLinhKhiCanThiet();
    }

    // Getters and Setters
    public UUID getUuid() { return uuid; }
    public TuLuyenInfo getTuLuyenInfo() { return tuLuyenInfo; }
    public void setTuLuyenInfo(TuLuyenInfo tuLuyenInfo) { this.tuLuyenInfo = tuLuyenInfo; }
    public long getLinhKhi() { return linhKhi; }
    public void setLinhKhi(long linhKhi) { this.linhKhi = linhKhi; }
    public LinhCan getLinhCan() { return linhCan; }
    public void setLinhCan(LinhCan linhCan) { this.linhCan = linhCan; }
}
