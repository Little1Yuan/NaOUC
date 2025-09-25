package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class GetCardAccInfoOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn/User/GetCardAccInfo";
    private final String account;

    public GetCardAccInfoOUCRequest(String account) {
        this.account = account;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder()
                .post(new FormBody.Builder()
                        .addEncoded("acc", account)
                        .addEncoded("json", "true")
                        .build())
                .url(URL)).build();
    }
}
