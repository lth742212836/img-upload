package com.lucode.img.enums;

/**
 * @author liutianhao
 * @date 2022/05/13
 **/
public enum SystemPropertyEnum {
    LINUX(1, "Linux", ""),
    WINDOWS(2, "Windows", "C:\\Users\\Administrator"),
    MAC(3, "Mac", ""),

    ;

    public Integer getCode() {
        return code;
    }


    public String getSystem() {
        return system;
    }


    public String getPath() {
        return path;
    }

    SystemPropertyEnum(Integer code, String system, String path) {
        this.code = code;
        this.system = system;
        this.path = path;
    }

    private Integer code;
    private String system;
    private String path;
}
