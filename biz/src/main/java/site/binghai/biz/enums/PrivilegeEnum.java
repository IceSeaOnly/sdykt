package site.binghai.biz.enums;

import java.util.HashMap;
import java.util.Map;

public enum PrivilegeEnum {
    ArtRegister4Examination("艺术志愿报考"),

    ;

    private String projectName;

    private static Map<String, PrivilegeEnum> maps;

    static {
        maps = new HashMap<>();
        for (PrivilegeEnum value : values()) {
            maps.put(value.name(), value);
        }
    }

    PrivilegeEnum(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public static PrivilegeEnum valueOfName(String name) {
        return maps.get(name);
    }


}
