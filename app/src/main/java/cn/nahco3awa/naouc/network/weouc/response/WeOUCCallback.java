package cn.nahco3awa.naouc.network.weouc.response;

public interface WeOUCCallback<R extends WeOUCResponse> {
    void onSuccess(R response);
    void onFailure(Throwable e);
}
