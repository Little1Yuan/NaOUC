package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.FormBody;
import okhttp3.Request;

public class LoginOUCRequest implements OUCRequest {
    public static final String URL = "https://vcard.ouc.edu.cn/Phone/Login";
    private final String sno;
    private final String pwd;
    private final boolean remember;
    private final String uclass;
    private final String yzm;
    private final String key;

    public LoginOUCRequest(String sno, String pwd, boolean remember, String uClass, String yzm, String key) {
        this.sno = sno;
        this.pwd = pwd;
        this.remember = remember;
        this.uclass = uClass;
        this.yzm = yzm;
        this.key = key;
    }

    @Override
    public Request makeRequest(OUCRequestSender sender) {
        return sender.setAspHeaders(new Request.Builder()
                .url(URL)
                .post(new FormBody.Builder()
                        .addEncoded("sno", sno)
                        .addEncoded("pwd", pwd)
                        .addEncoded("remember", remember ? "1" : "0")
                        .addEncoded("uclass", uclass)
                        .addEncoded("yzm", yzm)
                        .addEncoded("key", key)
                        .addEncoded("json", "true")
                        .build()))
                .build();
    }
}
