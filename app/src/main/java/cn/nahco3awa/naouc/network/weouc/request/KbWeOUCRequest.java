package cn.nahco3awa.naouc.network.weouc.request;

import cn.nahco3awa.naouc.network.weouc.LoginType;
import cn.nahco3awa.naouc.network.weouc.WeOUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class KbWeOUCRequest implements WeOUCRequest {
    public static final String URL = "https://api.weouc.com/api/jw/kb";
    private final int xn;
    private final int xq;
    private final String sno;
    private final LoginType loginType;
    private final String password;

    public KbWeOUCRequest(int xn, int xq, String sno, LoginType loginType, String password) {
        this.xn = xn;
        this.xq = xq;
        this.sno = sno;
        this.loginType = loginType;
        this.password = password;
    }

    @Override
    public Request makeRequest(WeOUCRequestSender sender) {
        return new Request.Builder()
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("xn", String.valueOf(xn))
                        .addEncoded("xq", String.valueOf(xq))
                        .addEncoded("sno", sno)
                        .addEncoded("loginType", loginType.getName())
                        .addEncoded("password", password)
                        .build())
                .build();
    }
}
