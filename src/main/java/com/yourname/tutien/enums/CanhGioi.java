package com.yourname.tutien.enums;

import org.bukkit.ChatColor;

public enum CanhGioi {
    PHAM_NHAN("§7Phàm Nhân", 0, 1000),
    LUYEN_KHI("§fLuyện Khí Kỳ", 1, 1000),
    TRUC_CO("§aTrúc Cơ Kỳ", 2, 50000),
    KIM_DAN("§eKim Đan Kỳ", 3, 500000),
    NGUYEN_ANH("§bNguyên Anh Kỳ", 4, 4000000),
    HOA_THAN("§dHoá Thần Kỳ", 5, 25000000),
    LUYEN_HU("§5Luyện Hư Kỳ", 6, 150000000),
    HOP_THE("§c§lHợp Thể Kỳ", 7, 800000000),
    DAI_THUA("§9§lĐại Thừa Kỳ", 8, 4000000000L),
    DO_KIEP("§4§lĐộ Kiếp Kỳ", 9, 20000000000L);

    private final String tenHienThi;
    private final int id;
    private final long linhKhiChoTangMot;

    CanhGioi(String tenHienThi, int id, long linhKhiChoTangMot) {
        this.tenHienThi = tenHienThi;
        this.id = id;
        this.linhKhiChoTangMot = linhKhiChoTangMot;
    }

    public String getTenHienThi() { return ChatColor.translateAlternateColorCodes('&', tenHienThi); }
    public int getId() { return id; }
    public long getLinhKhiChoTangMot() { return linhKhiChoTangMot; }

    public static CanhGioi getNext(CanhGioi hienTai) {
        if (hienTai == DO_KIEP) return DO_KIEP;
        for (CanhGioi canhGioi : values()) {
            if (canhGioi.getId() == hienTai.getId() + 1) {
                return canhGioi;
            }
        }
        return DO_KIEP;
    }
}
