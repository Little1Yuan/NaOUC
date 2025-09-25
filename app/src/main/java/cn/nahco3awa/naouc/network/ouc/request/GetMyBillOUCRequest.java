package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class GetMyBillOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn/Report/GetMyBill";
    private final String account;
    private final int page;

    public GetMyBillOUCRequest(String account, int page) {
        this.account = account;
        this.page = page;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder())
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("account",  account)
                        .addEncoded("page", String.valueOf(page))
                        .addEncoded("json", "true")
                        .build())
                .build();
    }
}
