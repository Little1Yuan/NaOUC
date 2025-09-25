package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collection;

import cn.nahco3awa.naouc.network.ouc.response.data.SingleBillData;
import okhttp3.Response;

public class GetMyBillOUCResponse extends OUCResponse {
    private final Collection<SingleBillData> billData;
    public GetMyBillOUCResponse(Response response) {
        super(response);
        JsonObject jsonObject = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        var arr = jsonObject.get("rows").getAsJsonArray();
        billData = new ArrayList<>(arr.size());
        Gson gson = new Gson();
        for (JsonElement jsonElement : arr) {
            billData.add(gson.fromJson(jsonElement, SingleBillData.class));
        }
    }

    public Collection<SingleBillData> getBillData() {
        return billData;
    }
}
