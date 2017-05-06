package com.heavy.autouml.interf

import com.heavy.autouml.model.Dependency
import com.heavy.autouml.view.UMLClassDiagramView

public interface IDependencier {

    public void writeDependency(Collection<Dependency> dependencies, UMLClassDiagramView view);

    public String getDependencyDes(Dependency dpcy);

    public boolean isRelationSupported(int relation);
}
