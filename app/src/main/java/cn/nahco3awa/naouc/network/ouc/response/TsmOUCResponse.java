package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class TsmOUCResponse extends OUCResponse {
    private final String msg;
    private final String aid;
    private final String account;
    private final String netAccount;
    private final float balance;
    private final boolean isFrozen;
    private final boolean hasLost;
    private final String retcode;

    public TsmOUCResponse(Response response) {
        super(response);
        JsonObject jsonObject = JsonParser.parseReader(response.body().charStream()).getAsJsonObject().getAsJsonObject("query_net_info");
        if (!jsonObject.get("retcode").getAsString().equals("0")) {
            throw new RuntimeException(jsonObject.get("errmsg").getAsString());
        }
        retcode = jsonObject.get("retcode").getAsString();
        msg = jsonObject.get("errmsg").getAsString();
        aid = jsonObject.get("aid").getAsString();
        account = jsonObject.get("account").getAsString();
        JsonObject netAccountObject = jsonObject.getAsJsonObject("netacc");
        netAccount = netAccountObject.get("netacc").getAsString();
        balance = Integer.parseInt(netAccountObject.get("bal").getAsString()) / 100.0f;
        isFrozen = netAccountObject.get("freezeflag").getAsString().equals("1");
        hasLost = netAccountObject.get("lostflag").getAsString().equals("1");
    }

    public String getMsg() {
        return msg;
    }

    public String getAid() {
        return aid;
    }

    public String getAccount() {
        return account;
    }

    public String getNetAccount() {
        return netAccount;
    }

    public float getBalance() {
        return balance;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public boolean hasLost() {
        return hasLost;
    }
}
