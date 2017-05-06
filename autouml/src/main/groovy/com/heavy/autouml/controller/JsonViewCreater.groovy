package com.heavy.autouml.controller

import com.heavy.autouml.util.FileUtil
import com.heavy.autouml.util.LogUtil
import com.heavy.autouml.view.JsonView
import org.json.JSONObject

public class JsonViewCreater {


    public static final String FILE_SUFFIX = ".json";
    private Collection<JsonView> mViews;
    private JsonView mView;
    private JSONObject mResult;
    private String mRestoreName;
    private long mSourceCompiledTime;

    public JsonViewCreater(String name, Collection<JsonView> views) {
        mRestoreName = name;
        mViews = views;
        for (JsonView view : views) {
            mSourceCompiledTime = view.getData().getCompiledTime() > mSourceCompiledTime ?
                    view.getData().getCompiledTime() : mSourceCompiledTime;
        }
    }

    public JsonViewCreater(String name, JsonView view) {
        mView = view;
        mRestoreName = name;
        mSourceCompiledTime = view.getData().getCompiledTime();
    }


    public void createView() {
        if (null != mViews) {
            mResult = convertToPackageJson(mViews);
        } else if (null != mView) {
            mResult = mView.build();
        }
        LogUtil.v("JsonViewCreater", "creatView result-->" + mResult.toString());
    }

    private JSONObject convertToPackageJson(Collection<JsonView> beans) {
        JSONObject result = new JSONObject();
        for (JsonView bean : beans) {
            try {
                JSONObject obj = new JSONObject(bean.getJsonDescription());
                String packageName = bean.getData().getPackageName();
                if (result.optJSONObject(packageName) == null) {
                    result.putOpt(packageName, new JSONObject());
                }
                String className = bean.getData().getSimpleName();
                if (result.optJSONObject(packageName).optJSONObject(className) == null) {
                    result.getJSONObject(packageName).putOpt(className, obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void restore(String path) {
        String restoreJsonFileName = path + mRestoreName + FILE_SUFFIX;
        File restoreJsonFile = new File(restoreJsonFileName);
        if (restoreJsonFile.lastModified() < mSourceCompiledTime) {
            FileUtil.writeFile(restoreJsonFileName, mResult.toString(), false);
        }
    }

}


