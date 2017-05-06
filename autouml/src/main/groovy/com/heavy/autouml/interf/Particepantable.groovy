package com.heavy.autouml.interf

import com.heavy.autouml.util.TextUtil

import java.lang.reflect.Modifier;

public abstract class Particepantable {

    protected String mModifierDes = "";
    protected String mDescription;

    public static final String MOD_PUBLIC = "+";
    public static final String MOD_PRIVATE = "-";
    public static final String MOD_PROTECTED = "#";
    public static final String MOD_DEFAULT = "~";
    public static final String MOD_STATIC = "{static}";
    public static final String MOD_ABSTRACT = "{abstract}";

    public abstract String getName();

    public String getModifierDescription() {

        if (!TextUtil.isEmpty(mModifierDes)) {
            return mModifierDes;
        }
        int modifiers = getModifiers();
        if (Modifier.isPublic(modifiers)) {
            mModifierDes += MOD_PUBLIC;
        } else if (Modifier.isPrivate(modifiers)) {
            mModifierDes += MOD_PRIVATE;
        } else if (Modifier.isProtected(modifiers)) {
            mModifierDes += MOD_PROTECTED;
        } else {
            mModifierDes += MOD_DEFAULT;
        }
        mModifierDes += " ";
        if (Modifier.isStatic(modifiers)) {
            mModifierDes += MOD_STATIC;
        } else if (Modifier.isAbstract(modifiers)) {
            mModifierDes += MOD_ABSTRACT;
        }
        return mModifierDes;
    }

    public abstract String getReturnTypeDes();

    public String getDescription() {
        if (mDescription == null) {
            mDescription = TextUtil.STD_INDENT + getModifierDescription() + " " + getReturnTypeDes() + " " + getName();
        }
        return mDescription;
    }

    public abstract int getModifiers();
}
