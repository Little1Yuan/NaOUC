package cn.nahco3awa.naouc.network.weouc.response.data.kb;

import com.google.gson.*;
import okhttp3.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ClassData {
    private final String name;
    private final String classRoom;
    private final String credit;
    private final String origin;
    private final String status;
    private final String teacher;
    private final int begin;
    private final int num;
    private final int day;
    private final List<String> weeks;

    public static final Map<Integer, Integer> DAY_OF_WEEK_MAP = new HashMap<>();
    public static final int[] CLASS_MINUTES = {
            0,
            8 * 60,
            9 * 60,
            10 * 60 + 10,
            11 * 60 + 10,
            // rest
            13 * 60 + 30,
            14 * 60 + 30,
            15 * 60 + 30,
            16 * 60 + 30,
            17 * 60 + 30,
            18 * 60 + 30,
            19 * 60 + 30,
            20 * 60 + 30
    };

    public ClassData(String name, String classRoom, String credit, String origin, String status, String teacher, int begin, int num, int day, List<String> weeks) {
        this.name = name;
        this.classRoom = classRoom;
        this.credit = credit;
        this.origin = origin;
        this.status = status;
        this.teacher = teacher;
        this.begin = begin;
        this.num = num;
        this.day = day;
        this.weeks = weeks;
    }

    public List<String> getWeeks() {
        return weeks;
    }

    public int getDay() {
        return day;
    }

    public int getNum() {
        return num;
    }

    public int getBegin() {
        return begin;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getStatus() {
        return status;
    }

    public String getOrigin() {
        return origin;
    }

    public String getCredit() {
        return credit;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public String getName() {
        return name;
    }

    public int getMinutesOfDay() {
        return CLASS_MINUTES[getBegin()];
    }

    public static ClassData fromJson(JsonObject jsonObject) {
        return new Gson().fromJson(jsonObject, ClassData.class);
    }

    public static List<ClassData> parseClassesData(JsonArray data) {
        List<ClassData> ret = new ArrayList<>();
        for (JsonElement datum : data) {
            for (JsonElement classes : datum.getAsJsonObject().getAsJsonArray("classes")) {
                if (classes.isJsonArray()) {
                    for (JsonElement classData : classes.getAsJsonArray()) {
                        if (classData.isJsonObject()) {
                            ret.add(fromJson(classData.getAsJsonObject()));
                        }
                    }
                }
            }
        }
        return ret;
    }

    public static int getClassDayByDayOfWeek(int dayOfWeek) {
        return DAY_OF_WEEK_MAP.get(dayOfWeek);
    }

    static {
        DAY_OF_WEEK_MAP.put(Calendar.SUNDAY, 6);
        DAY_OF_WEEK_MAP.put(Calendar.MONDAY, 0);
        DAY_OF_WEEK_MAP.put(Calendar.TUESDAY, 1);
        DAY_OF_WEEK_MAP.put(Calendar.WEDNESDAY, 2);
        DAY_OF_WEEK_MAP.put(Calendar.THURSDAY, 3);
        DAY_OF_WEEK_MAP.put(Calendar.FRIDAY, 4);
        DAY_OF_WEEK_MAP.put(Calendar.SATURDAY, 5);
    }
}
