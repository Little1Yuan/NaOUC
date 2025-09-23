package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class MobilePayCommonOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn:20085/PhonePay/MobilePayCommon";
    private final String request;
    private final String method;
    private final String md5String;
    private final String sign;
    private final String imei;
    private final String sourceType;
    private final String timestamp;

    public MobilePayCommonOUCRequest(String request, String method, String md5String, String sign, String imei, String sourceType, String timestamp) {
        this.request = request;
        this.method = method;
        this.md5String = md5String;
        this.sign = sign;
        this.imei = imei;
        this.sourceType = sourceType;
        this.timestamp = timestamp;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder()
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("request", request)
                        .addEncoded("clientType", "1")
                        .addEncoded("method", method)
                        .addEncoded("registerid", "mobileby")
                        .addEncoded("v", "1.0")
                        .addEncoded("md5string", md5String)
                        .addEncoded("sign", sign)
                        .addEncoded("versionName", "1.5.5")
                        .addEncoded("versionCode", "10505")
                        .addEncoded("sourcetype", sourceType)
                        .addEncoded("timestamp", timestamp)
                        .build())).build();
    }
}
