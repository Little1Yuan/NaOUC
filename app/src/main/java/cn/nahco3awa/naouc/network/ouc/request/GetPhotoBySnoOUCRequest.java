package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class GetPhotoBySnoOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn/NoBase/GetPhotoBySno";
    private final String imei;
    private final String sourceType;
    private final String sno;

    public GetPhotoBySnoOUCRequest(String imei, String sourceType, String sno) {
        this.imei = imei;
        this.sourceType = sourceType;
        this.sno = sno;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder())
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("accesstype", sourceType)
                        .addEncoded("clientType", "1")
                        .addEncoded("sno", sno)
                        .addEncoded("imei", imei)
                        .addEncoded("sourcetype", sourceType)
                        .addEncoded("versionName", "1.5.5")
                        .addEncoded("versionCode", "10505")
                        .build())
                .build();
    }
}
