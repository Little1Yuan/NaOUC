package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class NetGdcOUCResponse extends OUCResponse {
    private final int retCode;
    private final String errMessage;
    private final String aid;
    private final String account;
    private final String accountType;
    private final int tranAmount;
    public NetGdcOUCResponse(Response response) {
        super(response);
        JsonObject jsonObject = JsonParser.parseReader(response.body().charStream()).getAsJsonObject().getAsJsonObject("pay_net_gdc");
        retCode = Integer.parseInt(jsonObject.get("retcode").getAsString());
        errMessage = jsonObject.get("errmsg").getAsString();
        aid = jsonObject.get("aid").getAsString();
        account = jsonObject.get("account").getAsString();
        accountType = jsonObject.get("acctype").getAsString();
        tranAmount = Integer.parseInt(jsonObject.get("tranamt").getAsString());
    }

    public int getRetCode() {
        return retCode;
    }

    public int getTranAmount() {
        return tranAmount;
    }

    public String getAccount() {
        return account;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAid() {
        return aid;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
