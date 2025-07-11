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
        this.uuid = uuid; // Đảm bảo uuid được gán trước
        this.tuLuyenInfo = new TuLuyenInfo(CanhGioi.PHAM_NHAN, 1);
        this.linhKhi = 0;
        this.linhCan = LinhCan.phanLoaiNgauNhien();
    }

    public PlayerData(UUID uuid, CanhGioi canhGioi, int tang, long linhKhi, LinhCan linhCan) {
        this.uuid = uuid; // Đảm bảo uuid được gán trước
        this.tuLuyenInfo = new TuLuyenInfo(canhGioi, tang);
        this.linhKhi = linhKhi;
        this.linhCan = linhCan;
    }
    
    public void addLinhKhi(long amount) {
        if (amount > 0) this.linhKhi += amount;
        handleTierBreakthrough();
    }

    public void handleTierBreakthrough() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        if (tuLuyenInfo.getCanhGioi() == CanhGioi.PHAM_NHAN) return;

        while (tuLuyenInfo.getTang() < 9 && this.linhKhi >= tuLuyenInfo.getLinhKhiCanThiet()) {
            long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
            this.linhKhi -= linhKhiCanThiet;
            tuLuyenInfo.dotPha();
            
            player.sendMessage("§b[Tiểu Đột Phá] §fLinh khí ngưng tụ, đạo hữu đã đột phá lên §e" + tuLuyenInfo.getTenHienThiDayDu());
            
            TuTienPlugin.getInstance().getAttributeManager().updatePlayerAttributes(player);
            TuTienPlugin.getInstance().getFlightManager().updatePlayerFlight(player);
        }
    }
    
    public boolean canGrandBreakthrough() {
        long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
        if (linhKhiCanThiet == Long.MAX_VALUE) return false;

        double multiplier = TuTienPlugin.getInstance().getConfigManager().DOT_PHA_VUOT_MOC;
        long nguongYeuCau = (long) (linhKhiCanThiet * multiplier);
        
        // Phàm Nhân và Tầng 9 dùng chung logic này
        if (tuLuyenInfo.getCanhGioi() == CanhGioi.PHAM_NHAN || tuLuyenInfo.getTang() == 9) {
            return this.linhKhi >= nguongYeuCau;
        }

        // Các trường hợp khác (tầng 1-8) không thể đột phá lớn
        return false;
    }
    
    public void performGrandBreakthrough() {
        if (!canGrandBreakthrough()) return;
        
        long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
        this.linhKhi -= linhKhiCanThiet;
        tuLuyenInfo.dotPha();

        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            TuTienPlugin.getInstance().getAttributeManager().updatePlayerAttributes(player);
            TuTienPlugin.getInstance().getFlightManager().updatePlayerFlight(player);
        }
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
