package com.heavy.autouml.model

import com.heavy.autouml.util.TextUtil;

/**
 * Created by heavy on 2017/5/3.
 */

public class AddableStringConfig {

    Set<String> contents = new HashSet<>();

    public void configure(Closure closure1) {
        closure1.resolveStrategy = Closure.DELEGATE_FIRST;
        closure1.delegate = this;
        closure1.run();
    }

    public void add(String content) {
        if (contents == null) {
            contents = new ArrayList<String>();
        }
        contents.add(content);
    }

    public String[] getContents() {
        return contents.toArray(new String[0]);
    }

    public boolean contains(String str){
        if(!TextUtil.isEmpty(str)){
            return contents.contains(str);
        }
        return false;
    }

    @Override
    int hashCode() {
        int result = -1;
        if(contents != null){
            for(String content : contents){
                result += content.hashCode();
            }
        }
        return result;
    }

    @Override
    boolean equals(Object o) {
        return o instanceof  AddableStringConfig && o.hashCode() == hashCode();
    }
}
