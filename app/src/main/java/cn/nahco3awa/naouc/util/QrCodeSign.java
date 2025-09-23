package cn.nahco3awa.naouc.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import synjones.commerce.utils.JniQrCodeSign;

public class QrCodeSign {
    private static final JniQrCodeSign jniQrCodeSign = new JniQrCodeSign();

    public static String getSign(Map<?, ?> map) {
        return jniQrCodeSign.GetSign(map);
    }

    public static String getSign(String str) {
        return jniQrCodeSign.GetOfflineQrCode(str);
    }

    public static String getBase64(String str) {
        return new String(Base64.decode(str, 0), StandardCharsets.UTF_8);
    }
}