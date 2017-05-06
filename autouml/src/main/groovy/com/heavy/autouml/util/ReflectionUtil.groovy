package com.heavy.autouml.util

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

public class ReflectionUtil {

    public static Object callStaticVoidMethod(Class<?> clazz, String methodName) {
        return callStaticMethod(clazz, methodName, null);
    }

    public static Object staticInvoke(Method method, Object... args) {
        try {
            return method.invoke(null, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... types) {
        try {
            return clazz.getDeclaredMethod(methodName, types);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args) {
        List<Class<?>> argTypes = new ArrayList<Class<?>>();
        for (Object arg : args) {
            argTypes.add(arg.getClass());
        }
        try {
            Method method = clazz.getDeclaredMethod(methodName, argTypes.toArray(new Class<?>[args.length]));
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
