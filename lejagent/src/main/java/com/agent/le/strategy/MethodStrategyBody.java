package com.agent.le.strategy;

import com.agent.le.pojo.dto.ConfigInfoDTO;
import javassist.*;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: TODO
 */
public class MethodStrategyBody extends AbstractMethodStrategy<ConfigInfoDTO> {
    @Override
    public CtClass getCtClass(ConfigInfoDTO configInfoDTO, CtClass ctClass) throws NotFoundException, CannotCompileException {
        String name = configInfoDTO.getMethodName();
        String newName = configInfoDTO.getNewMethodName();

        String content = configInfoDTO.getMethodContent();

        CtMethod ctMethod = ctClass.getDeclaredMethod(name);
        ctMethod.setName(newName);
        CtMethod ctMethodNew = CtNewMethod.copy(ctMethod, name, ctClass, null);
        ctMethodNew.setBody(content);
        ctClass.addMethod(ctMethodNew);
        return ctClass;
    }
}