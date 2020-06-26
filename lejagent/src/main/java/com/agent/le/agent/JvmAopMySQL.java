package com.agent.le.agent;

import com.agent.le.db.MySQLConfig;
import com.agent.le.factory.CtClassFactory;
import com.agent.le.pojo.dto.ConfigInfoDTO;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: TODO
 */
public class JvmAopMySQL {

    /**
     * 在main方法之前jvm加载，执行
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new JvmAopMySQL.DefineTransformer(), true);
    }

    /**
     * 这个类的作用就是类转化器，按照类加载器去加载执行名称的类字节码文件，并使用字节码编辑器编写文件，然后在将字节码流加载并返回
     * 就是在类加载器加载字节码之后，创建Class对象前对字节码进行修改
     */
    static class DefineTransformer implements ClassFileTransformer {


        private static final ConcurrentHashMap<String, List<ConfigInfoDTO>> CONFIG_INFO_CLASS = new ConcurrentHashMap();

        private static final Set<String> CLASS_NAME = new HashSet();

        static {
            System.out.println("ClassFileTransformer static block");
            try {
                Map<String, List<ConfigInfoDTO>> confInfoMap = MySQLConfig.getConfInfo();
                CONFIG_INFO_CLASS.putAll(confInfoMap);
                CLASS_NAME.addAll(confInfoMap.keySet());
            } catch (SQLException e) {
                System.out.println("获取配置文件异常e" + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * 一个类转化器，作用是将加载的class 文件进行修改
         *
         * @param loader
         * @param className
         * @param classBeingRedefined
         * @param protectionDomain
         * @param classfileBuffer
         * @return
         * @throws IllegalClassFormatException
         */
        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            String newClassName = className.replace("/", ".");
            ClassPool pool = ClassPool.getDefault();
            pool.importPackage("org.slf4j.Logger");
            pool.importPackage("org.slf4j.LoggerFactory");
            pool.importPackage("com.alibaba.fastjson.JSON");

            try {
                CtClass ctClass = pool.get(newClassName);
                String ctClassName = ctClass.getName();
                boolean classFlag = CLASS_NAME.contains(ctClassName);
                if (!classFlag) {
                    //不修改
                    return classfileBuffer;
                }
                List<ConfigInfoDTO> methodList = CONFIG_INFO_CLASS.get(ctClassName);
                //不修改
                if (null == methodList) {
                    return classfileBuffer;
                }

                for (ConfigInfoDTO configInfo : methodList) {
                    CtClassFactory.getCtClass(configInfo, ctClass);
                }
                System.out.println("新方法持久化");
                ctClass.writeFile("/Users/wanghaidong/Documents/workspace/projects/ownProjects/inter-sbl/springboot-socks/all");
                //返回字节码
                return ctClass.toBytecode();
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return classfileBuffer;
        }
    }
}
