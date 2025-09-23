package synjones.commerce.utils;

import java.util.Map;

public class JniQrCodeSign {
    public native String GetOfflineQrCode(String str);

    public native String GetOfflineQrCodeData(String str);

    public native String GetRandomNum();

    /** @noinspection rawtypes*/
    public native String GetSign(Map map);

    public native String SetKeyAndSha1(String str, String str2);

    public native String SetKeyAndSha1(String str, String str2, int i);

    static {
        System.loadLibrary("JniQrCodeSign");
    }
}