package com.agent.le.enums;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: 添加位置 前置、后置、替换
 */
public enum AddTypeEnum {
    /**
     * before
     */
    BEFORE(1, "前置增强"),
    /**
     * after
     */
    AFTER(3, "后置增强"),
    /**
     * body
     */
    BODY(5, "方法体添加"),;


    private Integer code;

    private String desc;


    AddTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
