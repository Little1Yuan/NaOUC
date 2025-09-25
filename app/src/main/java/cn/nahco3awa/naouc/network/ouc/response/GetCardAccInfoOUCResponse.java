package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Response;

public class GetCardAccInfoOUCResponse extends OUCResponse {
    private final int balance; // 我们假定海大没有富哥
    public GetCardAccInfoOUCResponse(Response response) throws IOException {
        super(response);
        JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
        String msg = jsonObject.get("Msg").getAsString();
        JsonObject queryObject = JsonParser.parseString(msg).getAsJsonObject().get("query_accinfo").getAsJsonObject();
        if (!queryObject.get("retcode").getAsString().equals("0")) {
            throw new RuntimeException(queryObject.get("errmsg").getAsString());
        }
        balance = Integer.parseInt(queryObject.get("accinfo").getAsJsonArray().get(0).getAsJsonObject().get("balance").getAsString());
    }

    public int getBalance() {
        return balance;
    }
}
