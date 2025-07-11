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

    public PlayerData(UUID uuid) { /* ... */ }
    public PlayerData(UUID uuid, CanhGioi canhGioi, int tang, long linhKhi, LinhCan linhCan) { /* ... */ }

    public void addLinhKhi(long amount) {
        if (amount > 0) this.linhKhi += amount;
        handleTierBreakthrough();
    }

    // HÀM QUAN TRỌNG CẦN SỬA
    public void handleTierBreakthrough() {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        // (THÊM ĐIỀU KIỆN MỚI) Bỏ qua nếu đang là Phàm Nhân
        if (tuLuyenInfo.getCanhGioi() == CanhGioi.PHAM_NHAN) {
            return;
        }

        // Vòng lặp này bây giờ sẽ chỉ chạy cho các cảnh giới từ Luyện Khí trở lên
        while (tuLuyenInfo.getTang() < 9 && this.linhKhi >= tuLuyenInfo.getLinhKhiCanThiet()) {
            long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
            this.linhKhi -= linhKhiCanThiet;
            tuLuyenInfo.dotPha();
            
            player.sendMessage("§b[Tiểu Đột Phá] §fLinh khí ngưng tụ, đạo hữu đã đột phá lên §e" + tuLuyenInfo.getTenHienThiDayDu());
            
            TuTienPlugin.getInstance().getAttributeManager().updatePlayerAttributes(player);
            TuTienPlugin.getInstance().getFlightManager().updatePlayerFlight(player);
        }
    }
    
    // Hàm canGrandBreakthrough cần được cập nhật để xử lý cả Phàm Nhân
    public boolean canGrandBreakthrough() {
        // Phàm Nhân cũng có thể độ kiếp (lên Luyện Khí)
        if (tuLuyenInfo.getCanhGioi() == CanhGioi.PHAM_NHAN) {
            long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
            long nguongYeuCau = (long) (linhKhiCanThiet * TuTienPlugin.getInstance().getConfigManager().DOT_PHA_VUOT_MOC);
            return this.linhKhi >= nguongYeuCau;
        }

        if (tuLuyenInfo.getTang() != 9) return false;
        
        long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
        long nguongYeuCau = (long) (linhKhiCanThiet * TuTienPlugin.getInstance().getConfigManager().DOT_PHA_VUOT_MOC);
        return this.linhKhi >= nguongYeuCau;
    }
    
    //... các hàm còn lại giữ nguyên
    public void performGrandBreakthrough() { /* ... */ }
    public long getExcessLinhKhi() { /* ... */ }
    public UUID getUuid() { return uuid; }
    public TuLuyenInfo getTuLuyenInfo() { return tuLuyenInfo; }
    public void setTuLuyenInfo(TuLuyenInfo tuLuyenInfo) { this.tuLuyenInfo = tuLuyenInfo; }
    public long getLinhKhi() { return linhKhi; }
    public void setLinhKhi(long linhKhi) { this.linhKhi = linhKhi; }
    public LinhCan getLinhCan() { return linhCan; }
    public void setLinhCan(LinhCan linhCan) { this.linhCan = linhCan; }
}
