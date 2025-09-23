package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.Request;

public class GetValidateCodeOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn/Phone/GetValidateCode";
    private long time;
    public GetValidateCodeOUCRequest(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder()
                .url(URL + "?time=" + getTime())
                .get())
                .build();
    }
}
