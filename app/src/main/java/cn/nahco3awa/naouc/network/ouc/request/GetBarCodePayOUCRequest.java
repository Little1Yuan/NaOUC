package cn.nahco3awa.naouc.network.ouc.request;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import cn.nahco3awa.naouc.util.QrCodeSign;
import okhttp3.FormBody;
import okhttp3.Request;
import cn.nahco3awa.naouc.util.RSASign;
import cn.nahco3awa.naouc.util.SignUtils;

public class GetBarCodePayOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn:20085/PhonePay/MobilePayCommon";
    private final String account;
    private final String cardId;
    private final String imei;
    private final String sourceType;

    public GetBarCodePayOUCRequest(String account, String cardId, String imei, String sourceType) {
        this.account = account;
        this.cardId = cardId;
        this.imei = imei;
        this.sourceType = sourceType;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        HashMap<String, String> map = new HashMap<>();

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();

        jsonObject.addProperty("account", account);
        jsonObject.addProperty("acctype", "001");
        jsonObject.addProperty("sourcetype", sourceType);
        jsonObject.addProperty("flag", "00");
        jsonObject.addProperty("cardid", cardId);
        jsonObject2.add("synjones.pay.getbarcode", jsonObject);

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(jsonObject2);

        String request;
        map.put("method", "synjones.pay.getbarcode");
        String timestamp = URLEncoder.encode(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
        map.put("timestamp", timestamp);
        map.put("v", "1.0");
        map.put("registerid", "mobileby");
        request = new SignUtils().sign(jsonArray.get(0).toString(), "qJzGEh6hESZDVJeCnFPGuxzaiB7NLQM5", "DESede");
        String nr = URLEncoder.encode(request);
        map.put("request", nr);
        String sign = URLEncoder.encode(RSASign.sign(map));
        map.put("sign", sign);

        String signStatus = QrCodeSign.getSign(map);
        if (signStatus != null) {
            if (signStatus.equals("sign_error")) {
                throw new RuntimeException("验证签名失败！");
            }
            map.put("md5string", QrCodeSign.getBase64(signStatus).toLowerCase());

            FormBody formBody = new FormBody.Builder()
                    .addEncoded("request", nr)
                    .addEncoded("clientType", "1")
                    .addEncoded("method", "synjones.pay.getbarcode")
                    .addEncoded("registerid", "mobileby")
                    .addEncoded("v", "1.0")
                    .addEncoded("sign", sign)
                    .addEncoded("imei", imei)
                    .addEncoded("versionName", "1.5.5")
                    .addEncoded("versionCode", "10505")
                    .addEncoded("sourcetype", sourceType)
                    .addEncoded("timestamp", timestamp)
                    .build();

            return sender.setAspHeaders(new Request.Builder()
                    .url(URL)
                    .post(formBody)).build();
        }
        throw new RuntimeException("签名为空！");
    }
}
