package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class WaterApiBookCodeCancelResponse extends OUCResponse {
    private final int retNo;
    private final String description;
    private final String bookCode;
    public WaterApiBookCodeCancelResponse(Response response) {
        super(response);
        JsonObject jsonObject = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        retNo = jsonObject.get("RetNo").getAsInt();
        description = jsonObject.get("RetDsp").getAsString();
        bookCode = jsonObject.get("BookCode").getAsString();
    }

    public String getBookCode() {
        return bookCode;
    }

    public int getRetNo() {
        return retNo;
    }

    public String getDescription() {
        return description;
    }
}