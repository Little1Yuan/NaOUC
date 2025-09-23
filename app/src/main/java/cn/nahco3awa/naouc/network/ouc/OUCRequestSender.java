package cn.nahco3awa.naouc.network.ouc;

import androidx.annotation.NonNull;

import java.io.IOException;

import cn.nahco3awa.naouc.network.ouc.request.GetInfoByTokenOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.GetPhotoBySnoOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.GetRsaOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.GetValidateCodeOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.LoginOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.MobilePayCommonOUCRequest;
import cn.nahco3awa.naouc.network.ouc.request.OUCRequest;
import cn.nahco3awa.naouc.network.ouc.response.GetInfoByTokenOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.GetPhotoBySnoOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.GetRsaKeyOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.GetValidateCodeOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.LoginOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.MobilePayCommonOUCResponse;
import cn.nahco3awa.naouc.network.ouc.response.OUCCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OUCRequestSender {
    public static OUCRequestSender INSTANCE = null;
    private String sessionId;
    private final String imeiTicket;
    private String sourceTypeTicket;
    private final OkHttpClient httpClient;
    private OUCRequestSender(String imeiTicket) {
        this.imeiTicket = imeiTicket;
        this.sourceTypeTicket = "0";
        this.sessionId = "";
        httpClient = new OkHttpClient();
    }

    public static void init(String imeiTicket) {
        INSTANCE = new OUCRequestSender(imeiTicket);
    }

    public static OUCRequestSender getInstance() {
        return INSTANCE;
    }

    public String getImeiTicket() {
        return imeiTicket;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSourceTypeTicket() {
        return sourceTypeTicket;
    }

    public Request.Builder setAspHeaders(Request.Builder request) {
        return request.header("Cookie", "ASP.NET_SessionId=" + getSessionId() + "; imeiticket=" + getImeiTicket() + "; sourcetypeticket=" + getSourceTypeTicket())
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Origin", "https://vcard.ouc.edu.cn")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Dest", "empty")
                .header("Referer", "https://vcard.ouc.edu.cn//Phone/Login")
                .header("User-Agent", "Mozilla/5.0 (Linux; Android 12; ALA-AN70 Build/HONORALA-AN70; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/99.0.4844.88 Mobile Safari/537.36");
    }

    public void mobilePayCommon(MobilePayCommonOUCRequest request, OUCCallback<MobilePayCommonOUCResponse> callback) {
        sendRequest(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.onSuccess(new MobilePayCommonOUCResponse(response));
            }
        });
    }

    public void getPhotoBySno(GetPhotoBySnoOUCRequest request, OUCCallback<GetPhotoBySnoOUCResponse> callback) {
        sendRequest(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.onSuccess(new GetPhotoBySnoOUCResponse(response));
            }
        });
    }

    public void getInfoByToken(GetInfoByTokenOUCRequest request, OUCCallback<GetInfoByTokenOUCResponse> callback) {
        sendRequest(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.onSuccess(new GetInfoByTokenOUCResponse(response));
            }
        });
    }

    public void getRsaKey(GetRsaOUCRequest request, OUCCallback<GetRsaKeyOUCResponse> callback) {
        sendRequest(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                callback.onSuccess(new GetRsaKeyOUCResponse(response));
            }
        });
    }

    public void login(LoginOUCRequest request, OUCCallback<LoginOUCResponse> callback) {
        sendRequest(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    callback.onSuccess(new LoginOUCResponse(response));
                } catch (Exception e) {
                    callback.onFailure(e);
                }
            }
        });
    }

    public void getValidateCode(GetValidateCodeOUCRequest request, OUCCallback<GetValidateCodeOUCResponse> callback) {
        sendRequest(request, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                callback.onSuccess(new GetValidateCodeOUCResponse(response));
            }
        });
    }

    public void sendRequest(OUCRequest request, Callback callback) {
        Request req = request.makeRequest(this);
        Call call = httpClient.newCall(req);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String setCookie = response.header("Set-Cookie");
                    if (setCookie != null) {
                        for (String cookie : setCookie.split(";")) {
                            String[] kv = cookie.split("=");
                            if (kv[0].equals("ASP.NET_SessionId")) {
                                OUCRequestSender.this.sessionId = kv[1];
                            }
                        }
                    }
                }
                callback.onResponse(call, response);
            }
        });
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSourceTypeTicket(String sourceTypeTicket) {
        this.sourceTypeTicket = sourceTypeTicket;
    }
}
