package cn.nahco3awa.naouc.network.ouc.request;

import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.Request;

public class WaterApiBookCodeRequest implements OUCRequest {
    public static final String URL = "http://222.195.158.17:5003/waterapi/api/BookCodeReq";
    public static final byte[] PUBLIC_KEY = Base64.getDecoder().decode("3n4DdO47LWH2Co/WfpbdyA==");
    public static final String AES_MODE = "AES/ECB/PKCS5Padding";
    public static final String AES = "AES";
    private final String account;
    private final int classNo;

    public WaterApiBookCodeRequest(String account, int classNo) {
        this.account = account;
        this.classNo = classNo;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ano", account);
        jsonObject.addProperty("classno", classNo);
        try {
            byte[] raw = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(PUBLIC_KEY, AES);
            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedInfo = cipher.doFinal(raw);
            String info = Base64.getEncoder().encodeToString(encryptedInfo).replaceAll("=", "%3D");
            return new Request.Builder()
                    .url(URL + "?info=" + info + "&token=" + sender.getSourceTypeTicket())
                    .get()
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
