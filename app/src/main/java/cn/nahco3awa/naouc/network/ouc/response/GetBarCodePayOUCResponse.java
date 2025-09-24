package cn.nahco3awa.naouc.network.ouc.response;

import android.os.Debug;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class GetBarCodePayOUCResponse extends OUCResponse {
    private final int account;
    private final long expires;
    private final String[] barcode;
    public GetBarCodePayOUCResponse(Response response) {
        super(response);
        JsonObject object = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        if (!object.get("retcode").getAsString().equals("0")) {
            Log.e("GBCP", object.toString());
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

    public int getAccount() {
        return account;
    }

    public long getExpires() {
        return expires;
    }

    public String[] getBarcode() {
        return barcode;
    }
}
