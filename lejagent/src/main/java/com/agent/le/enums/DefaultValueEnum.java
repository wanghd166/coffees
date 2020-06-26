package com.agent.le.enums;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: TODO
 */
public enum DefaultValueEnum {
    /**
     * system default
     */
    DEFAULT_VALUE(0, "使用系统模式实现"),

    /**
     * 用户值
     */
    USER_VALUE(1, "使用用户配置值"),;


    private Integer code;

    private String desc;

    DefaultValueEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
