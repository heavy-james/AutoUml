package com.heavy.autouml.controller.depency

import com.heavy.autouml.interf.IDependencier
import com.heavy.autouml.model.ClassDiagramConfig
import com.heavy.autouml.model.Dependency
import com.heavy.autouml.util.TextUtil
import com.heavy.autouml.view.UMLClassDiagramView

public class Dependencier implements IDependencier {

    private ClassDiagramConfig mConfig;

    public Dependencier(ClassDiagramConfig config) {
        mConfig = config;
    }

    @Override
    public void writeDependency(Collection<Dependency> dependencies, UMLClassDiagramView view) {
        Iterator<Dependency> iter = dependencies.iterator();
        while (iter.hasNext()) {
            Dependency d = iter.next();
            if (isRelationSupported(d.getRelationType())) {
                view.write(TextUtil.endLine(getDependencyDes(d)));
            }
        }
    }

    @Override
    public String getDependencyDes(Dependency dependency) {
        if (mConfig.withPackageInfo) {
            return dependency.getDependencyDes();
        } else {
            return dependency.getPackageIgnoredDes();
        }
    }

    @Override
    public boolean isRelationSupported(int relation) {
        boolean supported = false;
        switch (relation) {
            case Dependency.RELATION_TYPE_INHERIT:
                supported = mConfig.isExtensionSupported();
                break;
            case Dependency.RELATION_TYPE_IMPLEMENT:
                supported = mConfig.isImplementSupported();
                break;
            case Dependency.RELATION_TYPE_COMPOSITE:
                supported = mConfig.isCompositionSupported();
                break;
            case Dependency.RELATION_TYPE_AGGREGATION:
                supported = mConfig.isAggregationSupported();
                break;
            case Dependency.RELATION_TYPE_BELONG:
                supported = mConfig.isDeclarationSupported();
                break;
        }
        return supported;
    }

}
