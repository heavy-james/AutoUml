package com.heavy.autouml.controller.partice

import com.heavy.autouml.interf.Particepantable
import com.heavy.autouml.model.ClassDiagramConfig;
import com.heavy.autouml.util.TextUtil;

import com.heavy.autouml.interf.IClassData;
import com.heavy.autouml.interf.IParticePanter;
import com.heavy.autouml.view.UMLClassDiagramView

import java.lang.reflect.Field;

public class ParticePanter implements IParticePanter {

	ClassDiagramConfig mConfig;

	public ParticePanter(ClassDiagramConfig config) {
        mConfig = config;
	}

	@Override
	public void writeParticipate(IClassData data, UMLClassDiagramView view) {
		if (!TextUtil.isEmpty(convertToParticepant(data))) {
			view.write(TextUtil.endLine(convertToParticepant(data)));
		}
	}

	@Override
	public String buildContent(IClassData data) {
		StringBuilder builder = new StringBuilder();
		if(mConfig.withFieldInfo){
			builder.append(buildFieldInfo(data));
		}
		if(mConfig.withMethodInfo){
			builder.append(buildMethodInfo(data));
		}
		return builder.toString();
	}

	@Override
	public String convertToParticepant(IClassData data) {
		StringBuilder result = new StringBuilder();
		result.append(getClassKeyWord(data)).append(" ").append(getParticepantName(data));
		String content = buildContent(data);
		if (!TextUtil.isEmpty(content)) {
			result.append("{\n").append(content).append("}");
		}
		return result.toString();
	}

	@Override
	public String getClassKeyWord(IClassData data) {
		if (data.isInterface()) {
			return "interface ";
		} else {
			return "class ";
		}
	}

	@Override
	public String getParticepantName(IClassData data) {
		if(mConfig.withPackageInfo){
			return data.getMemberNameAsNormal();
		}else{
			return data.getSimpleName();
		}
	}


	public static String buildFieldInfo(IClassData data){
        if (data.getFieldDatas() != null) {
            StringBuilder builder = new StringBuilder();
            for (Particepantable field : data.getFieldDatas()) {
                builder.append(TextUtil.STD_INDENT).append(TextUtil.endLine(field.getDescription()));
            }
            return builder.toString();
        }
		return "";

	}

	public static String buildMethodInfo(IClassData data) {

		StringBuilder builder = new StringBuilder();

		if (data.getMethodDatas() != null) {
			for (Particepantable method : data.getMethodDatas()) {
				builder.append(TextUtil.STD_INDENT).append(TextUtil.endLine(method.getDescription()));
			}
		}
		return builder.toString();
	}

	public static String buildMemberClassInfo(IClassData data){
		StringBuilder builder = new StringBuilder();

		if (data.getInnerClassDiagramDatas() != null) {
			builder.append("__Member Classes__\n");
			for (IClassData member : data.getInnerClassDiagramDatas()) {
				builder.append(TextUtil.STD_INDENT).append(TextUtil.endLine(member.getSimpleName()));
			}
		}
		return builder.toString();
	}

}
