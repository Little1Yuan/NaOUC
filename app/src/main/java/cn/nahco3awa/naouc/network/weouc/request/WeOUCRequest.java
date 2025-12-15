package cn.nahco3awa.naouc.network.weouc.request;

import cn.nahco3awa.naouc.network.weouc.WeOUCRequestSender;
import okhttp3.Request;

public interface WeOUCRequest {
    Request makeRequest(WeOUCRequestSender sender);
}
