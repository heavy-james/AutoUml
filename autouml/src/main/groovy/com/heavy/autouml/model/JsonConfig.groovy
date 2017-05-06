package com.heavy.autouml.model

import com.heavy.autouml.util.TextUtil;

/**
 * Created by heavy on 2017/5/3.
 */

public class JsonConfig {

    public static final String ORG_CLASS = "class";
    public static final String ORG_PACKAGE = "package";

    String orgnization;

    public void configure(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST;
        closure.delegate = this;
        closure.run();
    }

    public void orgnization(String org) {
        orgnization = org;
    }

    public boolean isClassView() {
        return TextUtil.equals(ORG_CLASS, orgnization);
    }

    public boolean isPackageView() {
        return TextUtil.equals(ORG_PACKAGE, orgnization);
    }

    public String toStorePath() {
        String result = "json/";
        if (isClassView()) {
            result += "classes/";
        }
        return result;
    }

    @Override
    int hashCode() {
        int result = -1;
        if (orgnization != null) {
            result = orgnization.hashCode();
        }
        return result; ;
    }

    @Override
    boolean equals(Object o) {
        return o instanceof JsonConfig && o.hashCode() == hashCode();
    }

    @Override
    String toString() {
        return orgnization;
    }
}
