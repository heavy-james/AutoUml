package com.heavy.autouml.model;

import java.lang.reflect.Method;
import java.lang.reflect.Type
import com.heavy.autouml.util.TextUtil;
import com.heavy.autouml.interf.Particepantable;

public class DefaultMethodData extends Particepantable {

    protected Method mMethod = null;
    protected String mArgTypesDes = "";
    protected String mDescription = "";

    public DefaultMethodData(Method method) {
        mMethod = method;
        initArgTypesDes();
    }


    private void initArgTypesDes() {
        Type[] args = mMethod.getGenericParameterTypes();

        for (Type arg : args) {
            if (arg instanceof Class<?>) {
                Class<?> clazz = (Class<?>) arg;
                mArgTypesDes += clazz.getSimpleName() + ", ";
            }
        }
        if (mArgTypesDes.length() > 0) {
            mArgTypesDes = mArgTypesDes.substring(0, mArgTypesDes.length() - ", ".length());
        }
    }

    public String getArgTypesDes() {
        return mArgTypesDes;
    }

    @Override
    public String getName() {
        return mMethod.getName();
    }

    @Override
    public String getDescription() {
        if (TextUtil.isEmpty(mDescription)) {
            StringBuilder result = new StringBuilder();
            result.append(super.getDescription())
                    .append("(").append(getArgTypesDes()).append(")");
            mDescription = result.toString();
        }
        return mDescription;
    }

    @Override
    public int getModifiers() {
        return mMethod.getModifiers();
    }

    @Override
    public String getReturnTypeDes() {
        return mMethod.getReturnType().getSimpleName();
    }

    public static DefaultMethodData[] convertMethodsToDatas(Method[] methods) {
        if (methods != null) {
            DefaultMethodData[] result = new DefaultMethodData[methods.length];
            for (int i = 0; i < methods.length; i++) {
                result[i] = new DefaultMethodData(methods[i]);
            }
            return result;
        }
        return null;
    }

}
