package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class GetRsaOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn/Common/GetRsaKey";
    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder())
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("json", "true")
                        .build())
                .build();
    }
}
