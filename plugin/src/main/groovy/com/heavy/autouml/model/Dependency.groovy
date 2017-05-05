package com.heavy.autouml.model

import com.heavy.autouml.util.LogUtil
import com.heavy.autouml.util.TextUtil;

import com.heavy.autouml.interf.IClassData;

public class Dependency implements Comparable<Dependency> {
	IClassData leftData;
	IClassData rightData;
	private String relationSymbol;
	private String relationDescription;
	private int relationType;
	private static final String SYMBOL_EXTENTION = "--|>";
	private static final String SYMBOL_BELONG = "--#";
	private static final String SYMBOLE_COMPOSITION = "--*";
	private static final String SYMBOL_AGGREGATION = "--o";

	private static final String DES_IMPLEMENT = "implemented";
	private static final String DES_INHERIT = "inherited";
	private static final String DES_BELONG = "declares";
	private static final String DES_METHOD_ARGS = "uses";
	private static final String DES_MEMBER = "contains";

	public static final int RELATION_TYPE_IMPLEMENT = 1;
	public static final int RELATION_TYPE_INHERIT = 2;
	public static final int RELATION_TYPE_BELONG = 3;
	public static final int RELATION_TYPE_COMPOSITE = 4;
	public static final int RELATION_TYPE_AGGREGATION = 5;

	private static Map<String, Dependency> cache = new HashMap<String, Dependency>();

	private Dependency(IClassData left, IClassData right, int type) {
		leftData = left;
		rightData = right;
		relationType = type;
		init();
	}

	public IClassData getLeftData() {
		return leftData;
	}

	public IClassData getRightData() {
		return rightData;
	}

	public int getRelationType() {
		return relationType;
	}

	public String getRelationTypeName() {
		return relationDescription;
	}

	private void init() {
		switch (relationType) {
		case RELATION_TYPE_IMPLEMENT:
			relationSymbol = SYMBOL_EXTENTION;
			relationDescription = DES_IMPLEMENT;
			break;
		case RELATION_TYPE_INHERIT:
			relationSymbol = SYMBOL_EXTENTION;
			relationDescription = DES_INHERIT;
			break;
		case RELATION_TYPE_BELONG:
			relationSymbol = SYMBOL_BELONG;
			relationDescription = DES_BELONG;
			break;
		case RELATION_TYPE_COMPOSITE:
			relationSymbol = SYMBOLE_COMPOSITION;
			relationDescription = DES_MEMBER;
			break;
		case RELATION_TYPE_AGGREGATION:
			relationSymbol = SYMBOL_AGGREGATION;
			relationDescription = DES_METHOD_ARGS;
		default:
			break;
		}
	}

	public String getDependencyDes() {
		StringBuilder builder = new StringBuilder();
		builder.append(leftData.getMemberNameAsNormal()).append(" ")
				.append(relationSymbol).append(" ")
				.append(rightData.getMemberNameAsNormal()).append(" : ")
				.append(relationDescription);
		return builder.toString();
	}

	public String getPackageIgnoredDes() {
		StringBuilder builder = new StringBuilder();
		builder.append(leftData.getSimpleName()).append(" ")
				.append(relationSymbol).append(" ")
				.append(rightData.getSimpleName()).append(" : ")
				.append(relationDescription);
		return builder.toString();
	}

	private static Dependency createDependency(IClassData left, IClassData right, int type) {
		String key = left.getName() + right.getName() + type;
		if (!cache.containsKey(key)) {
			cache.put(key, new Dependency(left, right, type));
		}
		return cache.get(key);
	}

	public static Dependency buildMethodDependency(IClassData left, IClassData right) {
		if (left == null || right == null || left.compareTo(right) == 0) {
			return null;
		}
		return createDependency(left, right, RELATION_TYPE_AGGREGATION);
	}

	public static Dependency buildMemberDependency(IClassData left, IClassData right) {
		if (left == null || right == null || left.compareTo(right) == 0) {
			return null;
		}
		return createDependency(left, right, RELATION_TYPE_COMPOSITE);
	}

	public static Dependency buildImplDependency(IClassData left, IClassData right) {
		if (left == null || right == null || left.compareTo(right) == 0) {
			return null;
		}
		return createDependency(left, right, RELATION_TYPE_IMPLEMENT);
	}

	public static Dependency buildInheritDependency(IClassData left, IClassData right) {
		if (left == null || right == null || left.compareTo(right) == 0) {
			return null;
		}
		return createDependency(left, right, RELATION_TYPE_INHERIT);
	}

	public static Dependency buildBelongDependency(IClassData left, IClassData right) {
		if (left == null || right == null || left.compareTo(right) == 0) {
			return null;
		}
		return createDependency(left, right, RELATION_TYPE_BELONG);
	}

	@Override
	public int compareTo(Dependency o) {
		if (TextUtil.equals(leftData.getName(), o.getLeftData().getName())
				&& TextUtil.equals(rightData.getName(), o.getRightData().getName())
				&& relationType == o.getRelationType()) {
			return 0;
		}
		return -1;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Dependency) {
			return compareTo((Dependency) obj) == 0;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int code = leftData.getName().hashCode() + rightData.getName().hashCode() + relationType;
		return code;
	}
}
