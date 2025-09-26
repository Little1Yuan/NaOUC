package cn.nahco3awa.naouc.network.ouc.request;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class TsmOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn:8988/web/Common/Tsm.html";
    private final String account;
    private final String payAccount;

    public TsmOUCRequest(String account, String payAccount) {
        this.account = account;
        this.payAccount = payAccount;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        JsonObject jsonObject = new JsonObject();
        JsonObject queryInfo = new JsonObject();
        queryInfo.addProperty("aid", "0030000000000301");
        queryInfo.addProperty("account", account);
        queryInfo.addProperty("payacc", payAccount);
        jsonObject.add("query_net_info", queryInfo);
        return sender.setAspHeaders(new Request.Builder())
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("jsondata", jsonObject.toString())
                        .addEncoded("funname", "synjones.onecard.query.net.info")
                        .addEncoded("json", "true")
                        .build())
                .build();
    }
}
