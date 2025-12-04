package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class NetGdcOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn:8988/web/NetWork/NetGdc.html";
    private final String account;
    private final String netAccount;
    private final int tran;

    public NetGdcOUCRequest(String account, String netAccount, int tran) {
        this.account = account;
        this.netAccount = netAccount;
        this.tran = tran;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder())
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("aid", "0030000000000301")
                        .addEncoded("account", account)
                        .addEncoded("tran", String.valueOf(tran))
                        .addEncoded("netacc", netAccount)
                        .addEncoded("acctype", "###")
                        .build())
                .build();
    }
}
