package com.heavy.autouml.view

import com.heavy.autouml.interf.IClassData
import org.json.JSONException
import org.json.JSONObject

public class JsonView {

    private static final String TAG = "DefaultClassJsonBean";
    private IClassData mData;

    public String name;
    public String packageName;
    public String file;
    public JsonView superClass;
    public JsonView[] interfaces;
    public boolean isInnerClass;
    public JsonView[] innerClasses;

    public static final String KEY_CLASS_NAME = "name";
    public static final String KEY_PAKCAGE_NAME = "package";
    public static final String KEY_FILE_NAME = "file";
    public static final String KEY_SUPER_CLASS = "inherited";
    public static final String KEY_IMPLEMENTED_INTERFACES = "implements";
    public static final String KEY_INNER_CLASSES = "members";
    public static final String KEY_ATTRI = "attris";
    public static final String KEY_ISMEMBER = "is_member";

    private static Map<String, JsonView> caches = new HashMap<String, JsonView>();

    private JsonView(IClassData data) {
        mData = data;
        init();
    }

    public IClassData getData() {
        return mData;
    }

    public static JsonView convert(IClassData data) {
        if (data == null) {
            return null;
        }
        if (!caches.containsKey(data.getName())) {
            JsonView bean = new JsonView(data);
            caches.put(data.getName(), bean);
        }
        return caches.get(data.getName());
    }

    public static JsonView[] convert(IClassData[] datas) {
        if (datas != null) {
            JsonView[] beans = new JsonView[datas.length];
            for (int i = 0; i < datas.length; i++) {
                beans[i] = convert(datas[i]);
            }
            return beans;
        }
        return null;
    }

    private void init() {
        name = mData.getSimpleName();
        packageName = mData.getPackageName();
        file = mData.getFileName();
        superClass = JsonView.convert(mData.getSuperClassDiagramData());
        interfaces = JsonView.convert(mData.getInterfacesDiagramDatas());
        isInnerClass = mData.isInnerClass();
        innerClasses = JsonView.convert(mData.getInnerClassDiagramDatas());
    }

    public JSONObject build() {
        JSONObject root = new JSONObject();
        try {


            JSONObject attriObj = new JSONObject();
            // attriObj.putOpt(KEY_CLASS_NAME, name);
            // attriObj.putOpt(KEY_FILE_NAME, file);
            attriObj.putOpt(KEY_PAKCAGE_NAME, packageName);
            attriObj.putOpt(KEY_ISMEMBER, mData.isInnerClass());
            root.put(KEY_ATTRI, attriObj);
            if (superClass != null) {
                root.putOpt(KEY_SUPER_CLASS, superClass.build());
            }

            if (interfaces != null && interfaces.length > 0) {
                JSONObject interfaceObj = new JSONObject();
                for (JsonView bean : interfaces) {
                    interfaceObj.putOpt(bean.getData().getSimpleName(), bean.build());
                }
                root.putOpt(KEY_IMPLEMENTED_INTERFACES, interfaceObj);
            }

            if (innerClasses != null && innerClasses.length > 0) {
                JSONObject memberClassObj = new JSONObject();
                for (JsonView bean : innerClasses) {
                    memberClassObj.putOpt(bean.getData().getSimpleName(), bean.build());
                }
                root.putOpt(KEY_INNER_CLASSES, memberClassObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root;
    }

    public String getJsonDescription() {
        return build().toString();
    }

    public static List<JsonView> convertToViews(List<IClassData> datas,
                                                Class<? extends JsonView> clazz) {
        List<JsonView> beans = new LinkedList<JsonView>();
        for (IClassData data : datas) {
            JsonView bean = JsonView.convert(data);
            beans.add(bean);
        }
        return beans;
    }
}
