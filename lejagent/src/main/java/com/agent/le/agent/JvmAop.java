package com.agent.le.agent;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author Caffe
 * @date 2020/6/26
 * @description: TODO
 */
public class JvmAop {
    /**
     * 在main方法之前jvm加载，执行
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new DefineTransformer(), true);
    }

    /**
     * 这个类的作用就是类转化器，按照类加载器去加载执行名称的类字节码文件，并使用字节码编辑器编写文件，然后在将字节码流加载并返回
     * 就是在类加载器加载字节码之后，创建Class对象前对字节码进行修改
     */
    static class DefineTransformer implements ClassFileTransformer {
        /**
         * 父类判断
         */
        private static final String SUP_CLASS = "com.sb.test.AbstractClass";
        /**
         * 要处理方法
         */
        private static final String METHOD = "operation";


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
            try {
                CtClass ctClass = pool.get(newClassName);
                String supplierClassName = ctClass.getSuperclass().getName();
                String methodName = null;
                if (SUP_CLASS.equals(supplierClassName)) {
                    CtMethod ctMethod = ctClass.getDeclaredMethod(METHOD);
                    methodName = ctMethod.getName();
                    //如果是继承了这个抽象父类则重写这个方法
                    String name = methodName;
                    String newName = "operation$" + 1;
                    //老方法修改名称
                    ctMethod.setName(newName);

                    CtMethod newCtMethod = CtNewMethod.copy(ctMethod, name, ctClass, null);
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("{org.slf4j.MDC.put(\"traceId\",\"1\");");
                    stringBuffer.append("try{");
                    stringBuffer.append("System.out.println(org.slf4j.MDC.get(\"traceId\"));");
                    //方法调用($$)方法所有参数
                    stringBuffer.append(newName).append("($$);");
                    stringBuffer.append("}catch(Exception e){throw  new Exception (e.getMessage());}");
                    stringBuffer.append("finally{org.slf4j.MDC.remove(\"traceId\");}");
                    stringBuffer.append("}");
                    //方法体设置
                    newCtMethod.setBody(stringBuffer.toString());
                    ctClass.addMethod(newCtMethod);
                    ctClass.writeFile("/Users/wanghaidong/Documents/workspace/projects/ownProjects/inter-sbl/springboot-socks/all");
                    return ctClass.toBytecode();
                }
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
