package cn.nahco3awa.naouc.network.ouc.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;

public class LoginOUCResponse extends OUCResponse {
    private final String rescouseType;
    private final String no;
    private final String name;
    private final String sex;
    private final String sno;
    private final String lastUpdate;
    private final String account;
    private final String cardId;
    private final String tel;
    private final String subjectName;
    private final String subjectCode;
    private final String gradeName;
    private final String schoolName;
    private final String pidName;
    private final String pidCode;
    private final String classCode;
    private final String expDate;
    private final String areaCode;
    private final String deptCode;
    private final String className;
    private final String createdDate;
    public LoginOUCResponse(Response response) {
        super(response);
        JsonObject object = JsonParser.parseReader(response.body().charStream()).getAsJsonObject();
        if (!object.get("IsSucceed").getAsBoolean()) {
            throw new RuntimeException(object.get("Msg").getAsString());
        }
        JsonObject obj2 = object.getAsJsonObject("Obj2");
        rescouseType = obj2.get("RescouseType").getAsString();
        no = obj2.get("NO").getAsString();
        name = obj2.get("NAME").getAsString();
        sex = obj2.get("SEX").getAsString();
        sno = obj2.get("SNO").getAsString();
        lastUpdate = obj2.get("LASTUPDATE").getAsString();
        account = obj2.get("ACCOUNT").getAsString();
        cardId = obj2.get("CARDID").getAsString();
        tel = obj2.get("TEL").getAsString();
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
        createdDate = obj2.get("CREATEDATE").getAsString();
    }


    public String getCreatedDate() {
        return createdDate;
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

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTel() {
        return tel;
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

    public String getRescouseType() {
        return rescouseType;
    }
}
