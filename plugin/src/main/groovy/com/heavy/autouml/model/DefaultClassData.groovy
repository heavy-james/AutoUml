package com.heavy.autouml.model

import com.heavy.autouml.util.LogUtil;
import com.heavy.autouml.util.TextUtil;

import com.heavy.autouml.interf.IClassData;
import com.heavy.autouml.interf.Particepantable

public class DefaultClassData implements IClassData {

	Class<?> mClass;
    private long mCompiledTime;
	private IClassData[] mInnerClassData;
	private IClassData[] mInterfaceDatas;
	private Particepantable[] mMethoDatas = null;
	private Particepantable[] mFieldDatas = null;
	private static final String TAG = "DefaultClassDiagramData";
	private static Map<String, DefaultClassData> caches = new HashMap<String, DefaultClassData>();

	private DefaultClassData(Class<?> clazz) throws IllegalArgumentException {
		mClass = clazz;
		LogUtil.v(TAG, "DefaultClassDiagramData construct class-->" + getName());
		if (mClass == null) {
			throw new IllegalArgumentException("DefaultClassDiagramData argument clazz is null");
		}
		init();

	}

	private void init() {
		mMethoDatas = DefaultMethodData.convertMethodsToDatas(mClass.getDeclaredMethods());
		mInnerClassData = convertClassToDatas(mClass.getDeclaredClasses());
		mInterfaceDatas = convertClassToDatas(mClass.getInterfaces());
        mCompiledTime = getClassCompiledTime(mClass);
		mFieldDatas = DefaultFieldData.convertFieldsToDatas(mClass.getDeclaredFields());
	}

    @Override
    public long getCompiledTime() {
        LogUtil.v(TAG, "getCompiledTime class-->" + getName() + " ;time--> $mCompiledTime");
        return mCompiledTime;
    }

    public static long getClassCompiledTime(Class<?> clazz){
        if(null != clazz){
            URL resource = clazz.getResource(clazz.getSimpleName() + ".class");
            if(resource != null){
                String classFileName = resource.getFile();
                if(classFileName.contains("!")){
                    classFileName = classFileName.substring(0, classFileName.indexOf("!"));
                }
                File file =  new File(new URL(classFileName).toURI());
                return file.lastModified();
            }else{
                return getClassCompiledTime(clazz.getDeclaringClass());
            }
        }
        return 0;
    }

	public static IClassData[] convertClassToDatas(Class<?>[] classes) {
		if (classes != null) {
			List<IClassData> result = new ArrayList<IClassData>();
            for (int i = 0; i < classes.length; i++) {
                IClassData data = loadForClass(classes[i]);
				if (data != null) {
					result.add(data);
				}
			}
			return result.size() > 0 ? result.toArray(new IClassData[result.size()]) : null;
		}
		return null;
	}

	public static DefaultClassData loadForClass(Class<?> clazz) {
		if (clazz == null || clazz.getClassLoader() == null) {
			return null;
		}
		if (!caches.containsKey(clazz.getName())) {
			DefaultClassData data = new DefaultClassData(clazz);
			caches.put(clazz.getName(), data);
		}
		return caches.get(clazz.getName());
	}

	@Override
	public String getPackageName() {
		return mClass.getPackage().getName();
	}

	@Override
	public String getSimpleName() {
		return mClass.getSimpleName();
	}

	@Override
	public String getFileName() {
		return mClass.isMemberClass() ? null : getName() + ".java";
	}

	@Override
	public IClassData getSuperClassDiagramData() {
		return DefaultClassData.loadForClass(mClass.getSuperclass());
	}

	@Override
	public IClassData[] getInterfacesDiagramDatas() {
		return mInterfaceDatas;
	}

	@Override
	public boolean isInnerClass() {
		return mClass.isMemberClass();
	}

	@Override
	public IClassData[] getInnerClassDiagramDatas() {
		return mInnerClassData;
	}

	@Override
	public String getName() {
		return mClass.getName();
	}

	@Override
	public boolean isInterface() {
		return mClass.isInterface();
	}

	@Override
	public int compareTo(IClassData o) {
		if (o != null && TextUtil.equals(getName(), o.getName())) {
			return 0;
		} else {
			return -1;
		}
	}

	@Override
	public String getMemberNameAsNormal() {
		String result = "";
		if (isInnerClass()) {
			result += getPackageName() + "." + getSimpleName();
		} else {
			result += getName();
		}
		return result;
	}

	@Override
	public Particepantable[] getMethodDatas() {
		return mMethoDatas;
	}

	@Override
	Particepantable[] getFieldDatas() {
		return mFieldDatas;
	}

	public static List<IClassData> convertToClassDiagramDatas(List<Class<?>> classes) {
		List<IClassData> datas = new LinkedList<IClassData>();
		for (Class<?> tempClass : classes) {
			IClassData data = DefaultClassData.loadForClass(tempClass);
			if (null != data) {
				datas.add(data);
			}
		}
		return datas;
	}

	@Override
	public Class<?> getWrappingClas() {
		// TODO Auto-generated method stub
		return mClass;
	}
}
