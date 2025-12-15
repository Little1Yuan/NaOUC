package cn.nahco3awa.naouc.network.ouc.response.data;

import java.util.Objects;

public final class SingleWaterHzWatchData {
    private final int ClassNo;
    private final String ClassName;
    private final int PosNum;
    private final int WarnPosNum;
    private final int UseFreeRate;
    private final int BookRate;
    private int Actkind;
    private String BookCode;

    public SingleWaterHzWatchData(
            int ClassNo, // 单位编号
            String ClassName, // 单位名称
            int PosNum, // 设备量
            int WarnPosNum, // ???
            int UseFreeRate, // 空闲率
            int BookRate, // 预约比例（66为0.66%）
            int Actkind, // 0未预约 1已预约
            String BookCode // 预约编号
    ) {
        this.ClassNo = ClassNo;
        this.ClassName = ClassName;
        this.PosNum = PosNum;
        this.WarnPosNum = WarnPosNum;
        this.UseFreeRate = UseFreeRate;
        this.BookRate = BookRate;
        this.Actkind = Actkind;
        this.BookCode = BookCode;
    }

    public int ClassNo() {
        return ClassNo;
    }

    public String ClassName() {
        return ClassName;
    }

    public int PosNum() {
        return PosNum;
    }

    public int WarnPosNum() {
        return WarnPosNum;
    }

    public int UseFreeRate() {
        return UseFreeRate;
    }

    public int BookRate() {
        return BookRate;
    }

    public int Actkind() {
        return Actkind;
    }

    public String BookCode() {
        return BookCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SingleWaterHzWatchData) obj;
        return this.ClassNo == that.ClassNo &&
                Objects.equals(this.ClassName, that.ClassName) &&
                this.PosNum == that.PosNum &&
                this.WarnPosNum == that.WarnPosNum &&
                this.UseFreeRate == that.UseFreeRate &&
                this.BookRate == that.BookRate &&
                this.Actkind == that.Actkind &&
                Objects.equals(this.BookCode, that.BookCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ClassNo, ClassName, PosNum, WarnPosNum, UseFreeRate, BookRate, Actkind, BookCode);
    }

    @Override
    public String toString() {
        return "SingleWaterHzWatchData[" +
                "ClassNo=" + ClassNo + ", " +
                "ClassName=" + ClassName + ", " +
                "PosNum=" + PosNum + ", " +
                "WarnPosNum=" + WarnPosNum + ", " +
                "UseFreeRate=" + UseFreeRate + ", " +
                "BookRate=" + BookRate + ", " +
                "Actkind=" + Actkind + ", " +
                "BookCode=" + BookCode + ']';
    }

    public void setActkind(int actkind) {
        Actkind = actkind;
    }

    public void setBookCode(String bookCode) {
        BookCode = bookCode;
    }
}
