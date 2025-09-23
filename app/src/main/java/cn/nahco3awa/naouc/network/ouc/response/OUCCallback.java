package cn.nahco3awa.naouc.network.ouc.response;

import okhttp3.Response;

public interface OUCCallback<R extends OUCResponse> {
    void onSuccess(R response);
    void onFailure(Throwable e);
}
