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

    public void handleTierBreakthrough() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || tuLuyenInfo.getCanhGioi() == CanhGioi.PHAM_NHAN) {
            return;
        }

        while (tuLuyenInfo.getTang() < 9 && this.linhKhi >= tuLuyenInfo.getLinhKhiCanThiet()) {
            long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
            this.linhKhi -= linhKhiCanThiet;
            tuLuyenInfo.dotPha();
            
            player.sendMessage("§b[Tiểu Đột Phá] §fLinh khí ngưng tụ, đạo hữu đã đột phá lên §e" + tuLuyenInfo.getTenHienThiDayDu());
            
            TuTienPlugin.getInstance().getAttributeManager().updatePlayerAttributes(player);
            TuTienPlugin.getInstance().getFlightManager().updatePlayerFlight(player);
        }
    }
    
    // HÀM ĐÃ ĐƯỢC SỬA LỖI
    public boolean canGrandBreakthrough() {
        // Lấy thông tin cần thiết
        CanhGioi canhGioiHienTai = tuLuyenInfo.getCanhGioi();
        int tangHienTai = tuLuyenInfo.getTang();
        
        boolean isAtBreakthroughPoint = (canhGioiHienTai == CanhGioi.PHAM_NHAN || tangHienTai == 9);
        
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
