package cn.nahco3awa.naouc.network.weouc.response;

import okhttp3.Response;

public abstract class WeOUCResponse {
    private final Response response;
     public WeOUCResponse(Response response) {
         this.response = response;
     }

    public Response getResponse() {
        return response;
    }
}
