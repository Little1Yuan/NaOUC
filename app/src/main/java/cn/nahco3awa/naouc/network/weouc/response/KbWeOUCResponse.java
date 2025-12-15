package cn.nahco3awa.naouc.network.weouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import cn.nahco3awa.naouc.network.weouc.response.data.kb.ClassData;
import okhttp3.Response;

public class KbWeOUCResponse extends WeOUCResponse {
    private final List<ClassData> classes;
    private final boolean fromCache;
    private final String message;
    private final int status;
    public KbWeOUCResponse(Response response) {
        super(response);
        JsonObject jsonObject = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        fromCache = jsonObject.get("from_cache").getAsBoolean();
        message = jsonObject.get("message").getAsString();
        status = jsonObject.get("status").getAsInt();
        classes = ClassData.parseClassesData(jsonObject.get("data").getAsJsonArray());
    }

    public int getStatus() {
        return status;
    }

    public List<ClassData> getClasses() {
        return classes;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFromCache() {
        return fromCache;
    }
}
