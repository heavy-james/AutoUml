package com.heavy.autouml.controller.organization;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type

import com.heavy.autouml.util.TextUtil;

import com.heavy.autouml.interf.IClassData;
import com.heavy.autouml.model.DefaultClassData;
import com.heavy.autouml.model.Dependency;

public class DiagramDataOrganiser {

	public static Set<IClassData> collectAndTrimAllDatas(IClassData data) {
		if (data != null) {
			Set<IClassData> result = new HashSet<IClassData>();
			Set<IClassData> temp = null;
			result.add(data);
			if (data.getSuperClassDiagramData() != null) {
				temp = collectAndTrimAllDatas(data.getSuperClassDiagramData());
				if (temp != null) {
					result.addAll(temp);
				}
			}
			if (data.getInterfacesDiagramDatas() != null) {
				temp = collectAndTrimAllDatas(data.getInterfacesDiagramDatas());
				if (temp != null) {
					result.addAll(temp);
				}
			}
			if (data.getInnerClassDiagramDatas() != null) {
				temp = collectAndTrimAllDatas(data.getInnerClassDiagramDatas());
				if (temp != null) {
					result.addAll(temp);
				}
			}
			return result;
		}
		return null;
	}

	public static Set<IClassData> collectAndTrimAllDatas(IClassData[] datas) {
		if (datas != null) {
			Set<IClassData> result = new HashSet<IClassData>();
			for (IClassData data : datas) {
				result.addAll(collectAndTrimAllDatas(data));
			}
			return result;
		}
		return null;
	}

	public static Set<Dependency> buildAllDependencies(IClassData data) {
		Set<Dependency> result = buildExtensionDependencies(data);
		result.addAll(buildDeclarationDependencies(data));
		result.addAll(buildCompositionDependencies(data));
		result.addAll(buildAggregationDependencies(data));
		return result;
	}

	public static Set<IClassData> getAllRelativeDatas(IClassData data) {
		if (data != null) {
			Set<IClassData> result = new HashSet<IClassData>();
			result.addAll(collectAndTrimAllDatas(data));
			for (Method method : data.getWrappingClas().getDeclaredMethods()) {
				for (Type type : method.getParameterTypes()) {
					if (type instanceof Class<?>) {
						Class<?> clazz = (Class<?>) type;
						IClassData d = DefaultClassData.loadForClass(clazz);
						if (null != d) {
							result.add(d);
						}
					}
				}
				IClassData d = DefaultClassData.loadForClass(method.getReturnType());
				if (null != d) {
					result.add(d);
				}
			}
			for (Field field : data.getWrappingClas().getDeclaredFields()) {
				IClassData d = DefaultClassData.loadForClass(field.getType());
				if (null != d) {
					result.add(d);
				}
			}
			return result;
		}
		return null;
	}

	private static Set<Dependency> buildExtensionDependencies(IClassData data) {
		IClassData left = data;
		Set<Dependency> result = new HashSet<Dependency>();
		if (data.getInterfacesDiagramDatas() != null) {
			for (IClassData interf : data.getInterfacesDiagramDatas()) {
				Dependency d = Dependency.buildImplDependency(left, interf);
				if (d != null) {
					result.add(d);
				}
			}
		}
		Dependency d = Dependency.buildInheritDependency(left, data.getSuperClassDiagramData());
		if (d != null) {
			result.add(d);
		}
		return result;
	}

	private static Set<Dependency> buildDeclarationDependencies(IClassData data) {
		IClassData left = data;
		Set<Dependency> result = new HashSet<Dependency>();
		if (data.getInnerClassDiagramDatas() != null) {
			for (IClassData inner : data.getInnerClassDiagramDatas()) {
				Dependency d = Dependency.buildBelongDependency(left, inner);
				if (d != null) {
					result.add(d);
				}
			}
		}
		return result;
	}

	private static Set<Dependency> buildAggregationDependencies(IClassData data) {
		IClassData left = data;
		Set<Dependency> result = new HashSet<Dependency>();
		if (data.getWrappingClas().getDeclaredMethods() != null) {
			for (Method method : data.getWrappingClas().getDeclaredMethods()) {
				for (Class<?> right : method.getParameterTypes()) {
					Dependency d = Dependency.buildMethodDependency(left, DefaultClassData.loadForClass(right));
					if (d != null) {
						result.add(d);
					}
				}
				Dependency d = Dependency.buildMethodDependency(left,
						DefaultClassData.loadForClass(method.getReturnType()));
				if (d != null) {
					result.add(d);
				}
			}
		}

		return result;
	}

	private static Set<Dependency> buildCompositionDependencies(IClassData data) {
		Set<Dependency> result = new HashSet<Dependency>();
		if (data.getWrappingClas().getDeclaredFields() != null) {
			for (Field field : data.getWrappingClas().getDeclaredFields()) {
				Dependency d = Dependency.buildMemberDependency(data, DefaultClassData.loadForClass(field.getType()));
				if (d != null) {
					result.add(d);
				}
			}
		}
		return result;
	}

	public static Collection<Dependency> buildDependencies(Collection<IClassData> datas){
		Set<Dependency> result = new HashSet<>();
		for(IClassData left : datas){
			for(IClassData right : datas){
				Dependency d = buildDependency(left, right);
				if(d != null){
					result.add(d);
				}
			}
		}
		return result;
	}

	public static Dependency buildDependency(IClassData left, IClassData right) {
		if (left == null || right == null || left.compareTo(right) == 0) {
			return null;
		}

		if (left.getSuperClassDiagramData() != null) {
			if (TextUtil.equals(left.getSuperClassDiagramData().getName(), right.getName())) {
				return Dependency.buildInheritDependency(left, right);
			}
		}

		if (left.getInterfacesDiagramDatas() != null) {
			for (IClassData interf : left.getInterfacesDiagramDatas()) {
				if (interf != null && TextUtil.equals(interf.getName(), right.getName())) {
					return Dependency.buildImplDependency(left, interf);
				}
			}
		}

		if (left.getInnerClassDiagramDatas() != null) {
			for (IClassData interf : left.getInnerClassDiagramDatas()) {
				if (interf != null && TextUtil.equals(interf.getName(), right.getName())) {
					return Dependency.buildBelongDependency(left, interf);
				}
			}
		}
		return null;
	}

	public static long getNewestClassModifiedTime(Collection<IClassData> datas){
		long result = 0;
		for(IClassData data : datas){
			result = data.getCompiledTime() > result ? data.getCompiledTime() : result;
		}
		return result;
	}
}
