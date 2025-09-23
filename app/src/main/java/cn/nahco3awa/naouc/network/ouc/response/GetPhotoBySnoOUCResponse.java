package cn.nahco3awa.naouc.network.ouc.response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64;

import okhttp3.Response;

public class GetPhotoBySnoOUCResponse extends OUCResponse {
    private final Bitmap image;
    public GetPhotoBySnoOUCResponse(Response response) {
        super(response);
        JsonObject object = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        if (!object.get("IsSucceed").getAsBoolean()) {
            throw new RuntimeException(object.get("Msg").getAsString());
        }
        String imageBase64 = object.get("Obj").getAsString();
        byte[] bs = Base64.getDecoder().decode(imageBase64);
        image = BitmapFactory.decodeByteArray(bs, 0, bs.length);
    }

    public Bitmap getImage() {
        return image;
    }
}
