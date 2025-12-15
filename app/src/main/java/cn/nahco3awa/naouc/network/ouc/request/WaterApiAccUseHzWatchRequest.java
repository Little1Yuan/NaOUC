package cn.nahco3awa.naouc.network.ouc.request;

import android.os.Debug;

import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.Request;

public class WaterApiAccUseHzWatchRequest implements OUCRequest {
    public static final String URL = "http://222.195.158.17:5003/waterapi/api/AccUseHzWatch";
    public static final byte[] PUBLIC_KEY = new byte[] {
            0x5e, -0x2, -0x7d, -0xc, 0x6e, -0x45, -0x53, -0x1f, 0x76, -0x76, 0xf, 0x56, -0x2, 0x16, 0x5d, 0x48,
    };
    public static final String AES_MODE = "AES/PKCS7Padding";
    public static final String AES = "AES";
    private final String account;
    public WaterApiAccUseHzWatchRequest(String account) {
        this.account = account;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ano", account);
        try {
            byte[] raw = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(PUBLIC_KEY, AES);
            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedInfo = cipher.doFinal(raw);
            String info = Base64.getEncoder().encodeToString(encryptedInfo);
            return new Request.Builder()
                    .url(URL + "?info=" + info + "&token=" + sender.getSourceTypeTicket())
                    .get()
                    .build();
        } catch (Exception e) {
            Logger.getGlobal().severe(e.getMessage());
        }
        return null;
    }
}
