package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.Request;

public class NetCheckOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn:8988/web/common/check.html?";
    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder())
                .url(URL + "ticket=" + sender.getSourceTypeTicket())
                .get()
                .build();
    }
}
