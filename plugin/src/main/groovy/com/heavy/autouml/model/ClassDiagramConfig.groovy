package com.heavy.autouml.model;

import com.heavy.autouml.model.AddableStringConfig
import com.heavy.autouml.util.LogUtil
import com.heavy.autouml.util.TextUtil

/**
 * Created by heavy on 2017/5/3.
 */

public class ClassDiagramConfig {


    public static final String ORGANIZATION_CLASS = "organization_class";
    public static final String ORGANIZATION_PACKAGE = "organization_package";
    public static final String EXTENSION = "extension";
    public static final String IMPLEMENT = "implement";
    public static final String COMPOSITION = "composition";
    public static final String AGGREGATION = "aggregation"
    public static final String DECLARATION = "declaration";

    /**
     * if show field in class entity or not,default false;
     */
    boolean withFieldInfo;
    /**
     * if show method in class entity ort not,default false
     */
    boolean withMethodInfo;
    /**
     * if print package info with class name or not,default false
     */
    boolean withPackageInfo;

    /**
     * the relation between classes that diagram shows
     */
    AddableStringConfig supportedRelations;


    private String mStorePath = null;

    public ClassDiagramConfig(){
        LogUtil.d("ClassDiagramConfig", "new instance constructed...");
        supportedRelations = new AddableStringConfig();
    }

    def configure(Closure closure){
        closure.run();
    }

    def supportedRelation(Closure closure){
        closure.resolveStrategy = Closure.DELEGATE_FIRST;
        closure.delegate = supportedRelations;
        supportedRelations.configure(closure);
    }

    public boolean isExtensionSupported(){
        return supportedRelations.contains(EXTENSION);
    }


    public boolean isImplementSupported(){
        return supportedRelations.contains(IMPLEMENT);
    }


    public boolean isCompositionSupported(){
        return supportedRelations.contains(COMPOSITION);
    }


    public boolean isAggregationSupported(){
        return supportedRelations.contains(AGGREGATION);
    }

    public boolean isDeclarationSupported(){
        return supportedRelations.contains(DECLARATION);
    }

    public String toStorePath(){

        if(mStorePath != null){
            return mStorePath;
        }

        String path = "uml/";
        if(withPackageInfo){
            path += "packaged_";
        }
        if(withMethodInfo){
            path += "methoded_";
        }
        if(withMethodInfo){
            path += "fielded_";
        }

        path = path.substring(0, path.length() -1) + "/";

        if(isExtensionSupported()){
            path += "ext_"
        }
        if(isImplementSupported()){
            path += "impl_"
        }
        if(isCompositionSupported()){
            path += "comp_"
        }
        if(isAggregationSupported()){
            path += "agg_"
        }
        if(isDeclarationSupported()){
            path += "dec_"
        }
        mStorePath = path.substring(0, path.length() -1);
        return mStorePath;
    }

    @Override
    String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("class diagram config ")
        .append("withFiledInfo--> $withFieldInfo").append("; withMethodInfo--> $withMethodInfo")
        .append("; withPackageInfo--> $withPackageInfo")
        .append("; isExtensionSupported--> ${isExtensionSupported()}")
        .append("; isImplementSupported--> ${isImplementSupported()}")
        .append("; isCompositionSupported--> ${isCompositionSupported()}")
        .append("; isAggregationSupported--> ${isAggregationSupported()}")
        .append("; isDeclarationSupported--> ${isDeclarationSupported()}");
        return builder.toString();
    }

    @Override
    int hashCode() {
        int result = 1;
        result *= withMethodInfo.hashCode();
        result *= (4 * withFieldInfo.hashCode());
        result *= (7 * withPackageInfo.hashCode());
        if(supportedRelations != null){
            result *= supportedRelations.hashCode()
        }
        return result;
    }

    @Override
    boolean equals(Object o) {
        return o instanceof  ClassDiagramConfig && hashCode() == o.hashCode();
    }
}

