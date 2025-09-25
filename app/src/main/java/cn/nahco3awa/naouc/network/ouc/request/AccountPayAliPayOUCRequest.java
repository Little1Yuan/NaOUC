package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class AccountPayAliPayOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn/User/Account_Pay";
    private final String account;
    private final String tranamt;

    public AccountPayAliPayOUCRequest(String account, String tranamt) {
        this.account = account;
        this.tranamt = tranamt;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder()
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("account", account)
                        .addEncoded("acctype", "001")
                        .addEncoded("tranamt", tranamt)
                        .addEncoded("objtype", "own")
                        .addEncoded("paytype", "alipay")
                        .addEncoded("client_type", "wap")
                        .addEncoded("paymethod", "4")
                        .addEncoded("iacctype", "acc")
                        .addEncoded("iswap", "0")
                        .addEncoded("json", "true")
                        .build())).build();
    }
}
