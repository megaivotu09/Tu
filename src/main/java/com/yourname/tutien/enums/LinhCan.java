package com.yourname.tutien.enums;

import org.bukkit.ChatColor;

public enum LinhCan {
    KHONG_CO("§8Không có Linh Căn", 0.0),
    PHE_PHAM("§7Phế Phẩm Linh Căn", 0.0),
    HA_PHAM("§fHạ Phẩm Linh Căn", 1.0),
    TRUNG_PHAM("§aTrung Phẩm Linh Căn", 3.0),
    THUONG_PHAM("§bThượng Phẩm Linh Căn", 12.0),
    THANH_PHAM("§6§lThánh Phẩm Linh Căn", 72.0);

    private final String tenHienThi;
    private final double heSoLinhKhi;

    LinhCan(String tenHienThi, double heSoLinhKhi) {
        this.tenHienThi = tenHienThi;
        this.heSoLinhKhi = heSoLinhKhi;
    }

    public String getTenHienThi() { return ChatColor.translateAlternateColorCodes('&', tenHienThi); }
    public double getHeSoLinhKhi() { return heSoLinhKhi; }

    /**
     * Phân loại linh căn ngẫu nhiên theo tỷ lệ mới:
     * - Thánh Phẩm: 4%
     * - Thượng Phẩm: 10%
     * - Không có & Phế Phẩm: 6% (mỗi loại 3%)
     * - Trung & Hạ Phẩm: 80% còn lại (mỗi loại 40%)
     */
    public static LinhCan phanLoaiNgauNhien() {
        double random = Math.random() * 100; // Random một số từ 0 đến 99.99...

        // Kiểm tra Thánh Phẩm (4%)
        if (random < 4) { // Dải từ 0 -> 3.99...
            return THANH_PHAM;
        } 
        // Kiểm tra Thượng Phẩm (10%)
        else if (random < 14) { // Dải từ 4 -> 13.99... (14 = 4 + 10)
            return THUONG_PHAM;
        } 
        // Kiểm tra Không có Linh Căn (3%)
        else if (random < 17) { // Dải từ 14 -> 16.99... (17 = 14 + 3)
            return KHONG_CO;
        } 
        // Kiểm tra Phế Phẩm (3%)
        else if (random < 20) { // Dải từ 17 -> 19.99... (20 = 17 + 3)
            return PHE_PHAM;
        } 
        // Kiểm tra Trung Phẩm (40%)
        else if (random < 60) { // Dải từ 20 -> 59.99... (60 = 20 + 40)
            return TRUNG_PHAM;
        } 
        // Còn lại là Hạ Phẩm (40%)
        else { // Dải từ 60 -> 99.99...
            return HA_PHAM;
        }
    }
}
