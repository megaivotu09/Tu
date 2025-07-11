package com.yourname.tutien.player;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.enums.LinhCan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;

public class PlayerData {
    // ... các biến và constructor giữ nguyên ...
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
    
    // ... các hàm khác giữ nguyên ...
    public void addLinhKhi(long amount) { /*...*/ }
    public void handleTierBreakthrough() { /*...*/ }
    public void performGrandBreakthrough() { /*...*/ }
    public long getExcessLinhKhi() { /*...*/ }
    public UUID getUuid() { return uuid; }
    public TuLuyenInfo getTuLuyenInfo() { return tuLuyenInfo; }
    public void setTuLuyenInfo(TuLuyenInfo tuLuyenInfo) { this.tuLuyenInfo = tuLuyenInfo; }
    public long getLinhKhi() { return linhKhi; }
    public void setLinhKhi(long linhKhi) { this.linhKhi = linhKhi; }
    public LinhCan getLinhCan() { return linhCan; }
    public void setLinhCan(LinhCan linhCan) { this.linhCan = linhCan; }


    // HÀM QUAN TRỌNG CẦN SỬA
    public boolean canGrandBreakthrough() {
        // Lấy thông tin cần thiết
        CanhGioi canhGioiHienTai = tuLuyenInfo.getCanhGioi();
        int tangHienTai = tuLuyenInfo.getTang();
        
        // Điều kiện 1: Người chơi có phải đang ở mốc cần đột phá lớn không?
        // Chỉ có Phàm Nhân (luôn là tầng 1) hoặc các cảnh giới khác ở Tầng 9 mới được đột phá lớn.
        boolean isAtBreakthroughPoint = (canhGioiHienTai == CanhGioi.PHAM_NHAN || tangHienTai == 9);
        
        if (!isAtBreakthroughPoint) {
            return false; // Nếu không ở Tầng 9 (và không phải Phàm Nhân), chắc chắn không thể đột phá lớn.
        }

        // Điều kiện 2: Kiểm tra linh khí
        long linhKhiCanThietGoc = tuLuyenInfo.getLinhKhiCanThiet();
        
        // Nếu đã max cảnh giới, không thể đột phá
        if (linhKhiCanThietGoc == Long.MAX_VALUE) {
            return false;
        }

        // Lấy hệ số vượt mốc từ config
        double multiplier = TuTienPlugin.getInstance().getConfigManager().DOT_PHA_VUOT_MOC;
        
        // Tính ngưỡng linh khí yêu cầu (mốc gốc * hệ số)
        long nguongYeuCau = (long) (linhKhiCanThietGoc * multiplier);

        // Điều kiện cuối: Linh khí hiện tại có đủ so với ngưỡng yêu cầu không?
        boolean hasEnoughLinhKhi = (this.linhKhi >= nguongYeuCau);
        
        // (Dành cho kiểm tra lỗi) Gửi log ra console
        // TuTienPlugin.getInstance().getLogger().info(String.format(
        //     "[Check /dotpha] Player: %s | Can: %b | CurrentLK: %d | RequiredLK: %d",
        //     Bukkit.getOfflinePlayer(uuid).getName(), hasEnoughLinhKhi, this.linhKhi, nguongYeuCau
        // ));

        return hasEnoughLinhKhi;
    }
}
