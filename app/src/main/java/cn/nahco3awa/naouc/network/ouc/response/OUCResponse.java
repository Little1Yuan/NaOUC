package cn.nahco3awa.naouc.network.ouc.response;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class OUCResponse {
    private final Response response;
     public OUCResponse(Response response) {
         this.response = response;
     }

    public Response getResponse() {
        return response;
    }
}
