package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class MobilePayCommonOUCResponse extends OUCResponse {
    private final int account;
    private final long expires;
    private final String[] barcode;
    public MobilePayCommonOUCResponse(Response response) {
        super(response);
        JsonObject object = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        if (!object.get("retcode").getAsString().equals("0")) {
            throw new RuntimeException(object.get("errmsg").getAsString());
        }
        JsonObject obj = object.getAsJsonObject("obj");
        account = Integer.parseInt(obj.get("ACCOUNT").getAsString());
        expires = Long.parseLong(obj.get("EXPIRES").getAsString());
        barcode = new String[4];
        var awa = obj.get("BARCODE").getAsJsonArray();
        for (int i = 0; i < 4; i++) {
            barcode[i] = awa.get(i).getAsString();
        }
    }
}
