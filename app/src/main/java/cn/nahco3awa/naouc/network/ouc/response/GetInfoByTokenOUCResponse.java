package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class GetInfoByTokenOUCResponse extends OUCResponse {
    private final String no;
    private final String name;
    private final String sex;
    private final String sno;
    private final String lastUpdate;
    private final String account;
    private final String cardId;
    private final String password;
    private final String bmmc;
    private final String subjectCode;
    private final String subjectName;
    private final String gradeName;
    private final String schoolName;
    private final String pidName;
    private final String pidCode;
    private final String classCode;
    private final String expDate;
    private final String areaCode;
    private final String deptCode;
    private final String className;

    public GetInfoByTokenOUCResponse(Response response) {
        super(response);
        JsonObject object = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        if (!object.get("IsSucceed").getAsBoolean()) {
            throw new RuntimeException(object.get("Msg").getAsString());
        }
        JsonObject obj2 = object.getAsJsonObject("Obj");
        no = obj2.get("NO").getAsString();
        name = obj2.get("NAME").getAsString();
        sex = obj2.get("SEX").getAsString();
        sno = obj2.get("SNO").getAsString();
        lastUpdate = obj2.get("LASTUPDATE").getAsString();
        account = obj2.get("ACCOUNT").getAsString();
        cardId = obj2.get("CARDID").getAsString();
        subjectName = obj2.get("ZYMC").getAsString();
        subjectCode = obj2.get("ZYDM").getAsString();
        gradeName = obj2.get("NJMC").getAsString();
        schoolName = obj2.get("XQMC").getAsString();
        pidName = obj2.get("PIDNAME").getAsString();
        pidCode = obj2.get("PIDCODE").getAsString();
        classCode = obj2.get("BJDM").getAsString();
        expDate = obj2.get("EXPDATE").getAsString();
        areaCode = obj2.get("AREACODE").getAsString();
        deptCode = obj2.get("DEPTCODE").getAsString();
        className = obj2.get("BJMC").getAsString();
        password = obj2.get("PASSWORD").getAsString();
        bmmc = obj2.get("BMMC").getAsString();
    }

    public String getClassName() {
        return className;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public String getExpDate() {
        return expDate;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getPidCode() {
        return pidCode;
    }

    public String getPidName() {
        return pidName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getBmmc() {
        return bmmc;
    }

    public String getPassword() {
        return password;
    }

    public String getCardId() {
        return cardId;
    }

    public String getAccount() {
        return account;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getSno() {
        return sno;
    }

    public String getSex() {
        return sex;
    }

    public String getName() {
        return name;
    }

    public String getNo() {
        return no;
    }
}
