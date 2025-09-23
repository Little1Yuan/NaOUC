package cn.nahco3awa.naouc.network.ouc.response;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

public class GetValidateCodeOUCResponse extends OUCResponse {
    private final Bitmap image;

    public GetValidateCodeOUCResponse(Response response) {
        super(response);
        image = BitmapFactory.decodeStream(response.body().byteStream());
    }

    public Bitmap getImage() {
        return image;
    }
}
