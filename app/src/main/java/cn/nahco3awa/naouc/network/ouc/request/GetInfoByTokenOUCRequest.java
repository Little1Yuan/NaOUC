package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class GetInfoByTokenOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn/NoBase/GetInfoByToken";
    private final String imei;
    private final String sourceType;
    private final String token;

    public GetInfoByTokenOUCRequest(String imei, String sourceType, String token) {
        this.imei = imei;
        this.sourceType = sourceType;
        this.token = token;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder())
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("clientType", "1")
                        .addEncoded("imei", imei)
                        .addEncoded("sourcetype", sourceType)
                        .addEncoded("versionName", "1.5.5")
                        .addEncoded("versionCode", "10505")
                        .addEncoded("token", token)
                        .build())
                .build();
    }
}
