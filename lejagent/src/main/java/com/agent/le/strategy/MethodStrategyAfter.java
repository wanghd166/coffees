package com.agent.le.strategy;

import com.agent.le.enums.DefaultValueEnum;
import com.agent.le.pojo.dto.ConfigInfoDTO;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: System.out.println("result:"+$_); 如果方法有返回值则会输出具体返回值，如果没有返回值则打印null
 */

public class MethodStrategyAfter extends AbstractMethodStrategy<ConfigInfoDTO> {

    @Override
    public CtClass getCtClass(ConfigInfoDTO configInfoDTO, CtClass ctClass) throws NotFoundException, CannotCompileException {
        String name = configInfoDTO.getMethodName();
        CtMethod ctMethod = ctClass.getDeclaredMethod(name);
        String content = getContent(ctMethod, configInfoDTO);
        System.out.println("MethodStrategyAfter content:" + content);
        ctMethod.insertAfter(content);
        return ctClass;
    }


    /**
     * 获取添加内容
     *
     * @param ctMethod
     * @param configInfoDTO
     * @return
     */
    private String getContent(CtMethod ctMethod, ConfigInfoDTO configInfoDTO) {
        Integer defaultValue = configInfoDTO.getDefaultValue();
        if (DefaultValueEnum.DEFAULT_VALUE.equals(defaultValue)) {
            return getDefaultContent(ctMethod);
        }
        return configInfoDTO.getMethodContent();
    }


    /**
     * logger.info("方法返回值)
     *
     * @param ctMethod
     * @return
     */
    private String getDefaultContent(CtMethod ctMethod) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("logger.info(");
        stringBuffer.append("\"").append(ctMethod.getName()).append("方法返回值:{}").append("\"");
        stringBuffer.append(",");
        stringBuffer.append("JSON.toJSONString(").append("$_").append(")");
        stringBuffer.append(");");
        String result = stringBuffer.toString();
        System.out.println(result);
        return result;
    }

}