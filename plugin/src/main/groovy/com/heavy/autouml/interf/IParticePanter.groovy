package com.heavy.autouml.interf;

import com.heavy.autouml.view.UMLClassDiagramView;

public interface IParticePanter {

	public String getParticepantName(IClassData data);

	public void writeParticipate(IClassData data, UMLClassDiagramView view);

	public String buildContent(IClassData data);

	public String convertToParticepant(IClassData data);

	public String getClassKeyWord(IClassData data);

}
