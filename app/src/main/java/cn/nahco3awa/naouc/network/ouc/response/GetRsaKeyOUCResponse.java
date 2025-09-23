package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class GetRsaKeyOUCResponse extends OUCResponse {
    private final String msg;
    private final String e;
    private final String mod;
    public GetRsaKeyOUCResponse(Response response) {
        super(response);
        JsonObject object = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        msg = object.get("Msg").getAsString();
        String[] keys = object.get("Obj").getAsString().split(",");
        e = keys[0];
        mod = keys[1];
    }

    public String getMod() {
        return mod;
    }

    public String getMsg() {
        return msg;
    }

    public String getE() {
        return e;
    }
}
