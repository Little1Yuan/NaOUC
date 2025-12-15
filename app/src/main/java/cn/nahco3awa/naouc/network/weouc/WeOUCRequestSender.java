package cn.nahco3awa.naouc.network.weouc;

import okhttp3.OkHttpClient;

public class WeOUCRequestSender {
    private final OkHttpClient client;
    private WeOUCRequestSender() {
        client = new OkHttpClient();
    }

    
}
