package com.agent.le.factory;

import com.agent.le.pojo.dto.ConfigInfoDTO;
import com.agent.le.strategy.AbstractMethodStrategy;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: TODO
 */
public class CtClassFactory {

    /**
     *
     * @param configInfoDTO
     * @param ctClass
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static CtClass getCtClass(ConfigInfoDTO configInfoDTO, CtClass ctClass) throws NotFoundException,
            CannotCompileException {
        boolean flag = (null == configInfoDTO || null == configInfoDTO.getClassName() || null == configInfoDTO
                .getMethodName() || null == configInfoDTO.getAddType());

        if (flag) {
            System.out.println("get CtMethod null flag:" + flag);
            return null;
        }
        return doGetCtClass(configInfoDTO, ctClass);
    }


    /**
     *
     * @param configInfoDTO
     * @param ctClass
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private static CtClass doGetCtClass(ConfigInfoDTO configInfoDTO, CtClass ctClass) throws NotFoundException,
            CannotCompileException {
        AbstractMethodStrategy abstractMethodStrategy = StrategyFactory.getStrategy(configInfoDTO);
        return abstractMethodStrategy.getCtClass(configInfoDTO, ctClass);
    }
}
