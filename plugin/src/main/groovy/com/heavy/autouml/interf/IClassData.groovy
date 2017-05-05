package com.heavy.autouml.interf;

public interface IClassData extends Comparable<IClassData> {

	long getCompiledTime();

	public String getName();

	public String getSimpleName();

	public String getMemberNameAsNormal();

	public String getFileName();

	public String getPackageName();

	public IClassData getSuperClassDiagramData();

	public IClassData[] getInterfacesDiagramDatas();

	public boolean isInnerClass();

	public boolean isInterface();

	public IClassData[] getInnerClassDiagramDatas();

	public Particepantable[] getMethodDatas();

	public Particepantable[] getFieldDatas();

	public Class<?> getWrappingClas();
}
