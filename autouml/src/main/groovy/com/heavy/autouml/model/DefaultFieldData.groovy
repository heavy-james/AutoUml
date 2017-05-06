package com.heavy.autouml.model

import com.heavy.autouml.interf.Particepantable

import java.lang.reflect.Field

public class DefaultFieldData extends Particepantable {

    private Field mField;

    public DefaultFieldData(Field field) {
        mField = field;
    }

    @Override
    String getName() {
        return mField.getName();
    }

    @Override
    String getReturnTypeDes() {
        return mField.getType().getSimpleName();
    }

    @Override
    int getModifiers() {
        return mField.getModifiers();
    }

    public static DefaultFieldData[] convertFieldsToDatas(Field[] fields) {
        if (fields != null) {
            DefaultFieldData[] result = new DefaultFieldData[fields.length];
            for (int i = 0; i < fields.length; i++) {
                result[i] = new DefaultFieldData(fields[i]);
            }
            return result;
        }
        return null;
    }
}