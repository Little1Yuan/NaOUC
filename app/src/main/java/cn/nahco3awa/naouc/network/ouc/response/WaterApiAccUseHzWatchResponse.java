package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import cn.nahco3awa.naouc.network.ouc.response.data.SingleWaterHzWatchData;
import okhttp3.Response;

public class WaterApiAccUseHzWatchResponse extends OUCResponse {
    private final int retNo;
    private final String description;
    private final List<SingleWaterHzWatchData> data;
    public WaterApiAccUseHzWatchResponse(Response response) {
        super(response);
        JsonObject jsonObject = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        retNo = jsonObject.get("RetNo").getAsInt();
        description = jsonObject.get("RetDsp").getAsString();
        if (retNo != 0) {
            throw new RuntimeException(description);
        }
        data = new ArrayList<>();
        for (JsonElement single : jsonObject.getAsJsonArray("List")) {
            JsonObject singleData = single.getAsJsonObject();
            data.add(new SingleWaterHzWatchData(
                    singleData.get("ClassNo").getAsInt(),
                    singleData.get("ClassName").getAsString(),
                    singleData.get("PosNum").getAsInt(),
                    singleData.get("WarnPosNum").getAsInt(),
                    singleData.get("UseFreeRate").getAsInt(),
                    singleData.get("BookRate").getAsInt(),
                    singleData.get("Actkind").getAsInt(),
                    singleData.get("BookCode").getAsString()
            ));
        }
    }

    public List<SingleWaterHzWatchData> getData() {
        return data;
    }

    public int getRetNo() {
        return retNo;
    }

    public String getDescription() {
        return description;
    }
}
