package week1;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tangmi
 * @title: MyClassLoader
 * @projectName hellojike
 * @description: TODO
 * @date 2021/6/26 20:41
 */
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = getFileName(name);
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = inputStream.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = bos.toByteArray();
            byte[] targetArray = new byte[data.length];
            for (int i = 0; i < data.length; i++) {
                targetArray[i] = (byte) (255 - data[i]);
            }
            inputStream.close();
            bos.close();

            return defineClass(name, targetArray, 0, data.length);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    //获取要加载 的xlass文件名
    private String getFileName(String name) {
        int index = name.lastIndexOf('.');
        if (index == -1) {
            return name + ".xlass";
        } else {
            return name.substring(index + 1) + ".xlass";
        }
    }
    public static void main(String[] args) {
        MyClassLoader myClassLoader = new MyClassLoader();
        try {
            // 加载相应的类
            Class<?> clazz = myClassLoader.loadClass("Hello");
            // 看看里面有些什么方法
            for (Method m : clazz.getDeclaredMethods()) {
                System.out.println(clazz.getSimpleName() + "." + m.getName());
            }
            // 创建对象
            Object instance = clazz.getDeclaredConstructor().newInstance();
            // 调用实例方法
            Method method = clazz.getMethod("hello");
            method.invoke(instance);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
