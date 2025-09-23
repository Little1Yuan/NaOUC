package cn.nahco3awa.naouc.network.ouc.request;

import cn.nahco3awa.naouc.network.ouc.OUCRequestSender;
import okhttp3.Request;

public interface OUCRequest {
    Request makeRequest(OUCRequestSender sender);
}
