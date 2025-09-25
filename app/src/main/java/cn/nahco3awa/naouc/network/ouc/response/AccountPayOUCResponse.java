package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class AccountPayOUCResponse extends OUCResponse {
    private final String url;
    private final String method;
    private final String orderId;
    public AccountPayOUCResponse(Response response) {
        super(response);
        JsonObject jsonObject = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        if (!jsonObject.get("IsSucceed").getAsBoolean()) {
            throw new RuntimeException(jsonObject.get("Msg").getAsString());
        }
        url = jsonObject.get("Msg").getAsString();
        method = jsonObject.get("Obj").getAsString();
        orderId = jsonObject.get("OrderID").getAsString();
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getOrderId() {
        return orderId;
    }
}
