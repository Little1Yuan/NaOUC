package cn.nahco3awa.naouc.network.weouc;

public enum LoginType {
    UNDERGRADUATE("undergraduate");
    String name;
    LoginType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
