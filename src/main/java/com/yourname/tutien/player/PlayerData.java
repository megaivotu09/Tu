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

    public PlayerData(UUID uuid) { /*...*/ }
    public PlayerData(UUID uuid, CanhGioi canhGioi, int tang, long linhKhi, LinhCan linhCan) { /*...*/ }
    public void addLinhKhi(long amount) { /*...*/ }
    public void handleTierBreakthrough() { /*...*/ }
    public boolean canGrandBreakthrough() { /*...*/ }
    public long getExcessLinhKhi() { /*...*/ }
    private void updateAllAttributes(Player player) { /*...*/ }
    public UUID getUuid() { return uuid; }
    public TuLuyenInfo getTuLuyenInfo() { return tuLuyenInfo; }
    public void setTuLuyenInfo(TuLuyenInfo tuLuyenInfo) { this.tuLuyenInfo = tuLuyenInfo; }
    public long getLinhKhi() { return linhKhi; }
    public void setLinhKhi(long linhKhi) { this.linhKhi = linhKhi; }
    public LinhCan getLinhCan() { return linhCan; }
    public void setLinhCan(LinhCan linhCan) { this.linhCan = linhCan; }

    // HÀM QUAN TRỌNG CẦN SỬA
    public void performGrandBreakthrough() {
        // 1. Tăng cảnh giới và tầng.
        this.tuLuyenInfo.dotPha(); 
        
        // 2. (THAY ĐỔI) KHÔNG TRỪ LINH KHÍ.
        // Dòng code trừ linh khí đã bị xóa.
        // this.setLinhKhi(this.getLinhKhi() - linhKhiDaDung);

        // 3. Cập nhật lại chỉ số cho người chơi.
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            updateAllAttributes(player);
        }
    }
}
