package com.yourname.tutien.player;

import com.yourname.tutien.enums.CanhGioi;

public class TuLuyenInfo {
    private CanhGioi canhGioi;
    private int tang;

    public TuLuyenInfo(CanhGioi canhGioi, int tang) {
        this.canhGioi = canhGioi;
        this.tang = tang;
    }
    
    public CanhGioi getCanhGioi() { return canhGioi; }
    public int getTang() { return tang; }

    public String getTenHienThiDayDu() {
        if (this.canhGioi == CanhGioi.DO_KIEP && this.tang == 9) return this.canhGioi.getTenHienThi() + " §7(Viên Mãn)";
        if (this.canhGioi == CanhGioi.PHAM_NHAN) return this.canhGioi.getTenHienThi();
        return this.canhGioi.getTenHienThi() + " Tầng " + this.tang;
    }

    public long getLinhKhiCanThiet() {
        if (this.canhGioi == CanhGioi.DO_KIEP && this.tang == 9) return Long.MAX_VALUE;
        if (this.tang == 9) return CanhGioi.getNext(this.canhGioi).getLinhKhiChoTangMot();
        long baseLinhKhi = this.canhGioi.getLinhKhiChoTangMot();
        return baseLinhKhi * (long) Math.pow(3, this.tang - 1);
    }
    
    public void dotPha() {
        if (this.canhGioi == CanhGioi.DO_KIEP && this.tang == 9) return;
        if (this.tang < 9) {
            this.tang++;
        } else {
            this.canhGioi = CanhGioi.getNext(this.canhGioi);
            this.tang = 1;
        }
    }
}
