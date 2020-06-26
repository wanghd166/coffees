package com.agent.le.pojo.dto;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: 数据库配置bean
 */

public class ConfigInfoDTO {
    /**
     * id
     */
    private Long id;
    /**
     * 类全限定名
     */
    private String className;
    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 新方法名称
     */
    private String newMethodName;
    /**
     * 添加类型 1:before  3:after  5: 方法体
     */
    private Integer addType;
    /**
     * 添加内容
     */
    private String methodContent;

    /**
     * 0:使用默认值 1： 使用用户配置值
     */
    private Integer defaultValue;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getAddType() {
        return addType;
    }

    public void setAddType(Integer addType) {
        this.addType = addType;
    }

    public String getMethodContent() {
        return methodContent;
    }

    public void setMethodContent(String methodContent) {
        this.methodContent = methodContent;
    }

    public String getNewMethodName() {
        return newMethodName;
    }

    public void setNewMethodName(String newMethodName) {
        this.newMethodName = newMethodName;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }
}