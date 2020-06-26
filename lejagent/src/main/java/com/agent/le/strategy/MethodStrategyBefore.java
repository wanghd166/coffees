package com.agent.le.strategy;

import com.agent.le.enums.DefaultValueEnum;
import com.agent.le.pojo.dto.ConfigInfoDTO;
import com.alibaba.fastjson.JSON;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: TODO
 */
public class MethodStrategyBefore extends AbstractMethodStrategy<ConfigInfoDTO> {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CtClass getCtClass(ConfigInfoDTO configInfoDTO, CtClass ctClass) throws NotFoundException, CannotCompileException {
        String name = configInfoDTO.getMethodName();
        CtMethod ctMethod = ctClass.getDeclaredMethod(name);
        String content = this.getContent(ctMethod, configInfoDTO);
        ctMethod.insertBefore(content);
        return ctClass;
    }


    /**
     * 获取添加内容
     *
     * @param ctMethod
     * @param configInfoDTO
     * @return
     * @throws NotFoundException
     */
    private String getContent(CtMethod ctMethod, ConfigInfoDTO configInfoDTO) throws NotFoundException {
        Integer defaultValue = configInfoDTO.getDefaultValue();
        if (DefaultValueEnum.DEFAULT_VALUE.equals(defaultValue)) {
            return getBeforeOfLog(ctMethod);
        }
        return configInfoDTO.getMethodContent();
    }


    /**
     * 方法参数名称、参数值
     *
     * @param ctMethod
     * @return
     */
    private String[] getParamName(CtMethod ctMethod) throws NotFoundException {
        System.out.println("getParamName begin");
        CtClass[] paramType = ctMethod.getParameterTypes();
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        //方法字段属性(字段属性获取)
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        int maxLocal = codeAttribute.getMaxLocals();
        int maxStack = codeAttribute.getMaxStack();
        System.out.println("maxLocal:" + maxLocal + "  maxStack:" + maxStack);

        String[] paramNames = new String[paramType.length];
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

        if (null != attr) {
            //非静态变量第一个参数即$0 = this
            int position = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
            System.out.println("position:" + position + "  paramType.length:" + paramType.length);
            for (int i = 0; i < paramType.length; i++) {
                String name = attr.variableName(i + position);
                System.out.println("method param name:" + name);
                paramNames[i] = name;
            }
        } else {
            System.out.println("LocalVariableAttribute is null");
        }
        return paramNames;
    }

    /**
     * 打印入参
     *
     * @param params
     * @param ctMethod
     * @return
     */
    private String addBefore(String[] params, CtMethod ctMethod) {
        StringBuffer stringBuffer = new StringBuffer();
        int beginIndex = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        for (int i = 0; i < params.length; i++) {
            int index = i + beginIndex;
            stringBuffer.append("\"").append(params[i]).append(":").append("\"").append("+").append("$").append
                    (index);
            if (i < params.length - 1) {
                stringBuffer.append("+");
                stringBuffer.append("\t");
            }
        }
        return stringBuffer.toString();
    }

    /**
     * get  add content
     *
     * @param ctMethod
     * @return
     * @throws NotFoundException
     */
    private String getBefore(CtMethod ctMethod) throws NotFoundException {
        // get parameter
        String[] paramNames = this.getParamName(ctMethod);
        String addContent = this.addBefore(paramNames, ctMethod);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("System.out.println(").append(addContent).append(")").append(";");
        String result = stringBuffer.toString();
        System.out.println("add before content: " + result);
        return result;
    }


    /**
     * log4j 打印
     * logger.info("id:{},name:{}",JSON.TOJsonString(id),JSON.TOJsonString(name));
     *
     * @param params
     * @param ctMethod
     * @return
     */
    private String addBeforeOfLog(String[] params, CtMethod ctMethod) {
        StringBuffer stringBuffer = new StringBuffer();
        int beginIndex = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        stringBuffer.append("\"");
        String methodName = ctMethod.getName();
        stringBuffer.append(methodName).append("方法请求参数 ");
        int len = params.length;
        for (int i = 0; i < params.length; i++) {
            stringBuffer.append(params[i]).append(":").append("{}");
            if (i < len - 1) {
                stringBuffer.append(",");
            }
        }
        stringBuffer.append("\"");
        stringBuffer.append(",");
        for (int i = 0; i < params.length; i++) {
            stringBuffer.append("JSON.toJSONString(").append("$").append(beginIndex + i).append(")");
            if (i < (params.length - 1)) {
                stringBuffer.append(",");
            }
        }
        return stringBuffer.toString();
    }


    /**
     * log 输出
     *
     * @param ctMethod
     * @return
     * @throws NotFoundException
     */
    private String getBeforeOfLog(CtMethod ctMethod) throws NotFoundException {
        String[] paramNames = this.getParamName(ctMethod);
        String addContent = addBeforeOfLog(paramNames, ctMethod);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("logger.info(").append(addContent).append(")").append(";");
        String result = stringBuffer.toString();
        logger.info("get before of log result:{}", JSON.toJSONString(result));
        return result;
    }
}
