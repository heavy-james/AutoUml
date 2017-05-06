package com.heavy.autouml.controller

import com.heavy.autouml.controller.depency.Dependencier
import com.heavy.autouml.controller.organization.DiagramDataOrganiser
import com.heavy.autouml.controller.partice.ParticePanter
import com.heavy.autouml.interf.IClassData
import com.heavy.autouml.interf.IDependencier
import com.heavy.autouml.interf.IParticePanter
import com.heavy.autouml.model.ClassDiagramConfig
import com.heavy.autouml.model.Dependency
import com.heavy.autouml.util.CommandHelper
import com.heavy.autouml.util.LogUtil
import com.heavy.autouml.view.UMLClassDiagramView

public class UmlViewCreater {

    public static final String COMMAND_PUML = "plantuml";

    Set<Dependency> mDependencies;
    private UMLClassDiagramView mView;
    private IParticePanter mParticePanter;
    private IDependencier mDependencier;
    private Collection<IClassData> mDatas;

    //constructor for single class diagram
    public UmlViewCreater(IClassData data, ClassDiagramConfig config) {
        this(data.getName(), DiagramDataOrganiser.collectAndTrimAllDatas(data), config);
    }

    //constructor for given classes diagram, always using in package view
    public UmlViewCreater(String name, Collection<IClassData> datas, ClassDiagramConfig config) {
        this(name, datas, DiagramDataOrganiser.buildDependencies(datas), config);
    }

    public UmlViewCreater(String name, Collection<IClassData> datas,
                          Set<Dependency> dependencies, ClassDiagramConfig config) {
        mDatas = datas;
        mDependencies = dependencies;
        mView = new UMLClassDiagramView(name, DiagramDataOrganiser.getNewestClassModifiedTime(datas));
        mParticePanter = new ParticePanter(config);
        mDependencier = new Dependencier(config);
    }

    public UMLClassDiagramView createView() {
        mView.writeHeader();
        for (IClassData data : mDatas) {
            mParticePanter.writeParticipate(data, mView);
        }
        mDependencier.writeDependency(mDependencies, mView);
        mView.writeFooter();
        return mView;
    }

    public static void createViewPngBatch(List<UMLClassDiagramView> views) {
        StringBuilder argsBuilder = new StringBuilder();
        for (UMLClassDiagramView view : views) {
            String pumlName = view.getRestoredPumlFileName();

            //create png file if source exists and is newer than target
            File source = new File(pumlName);
            File tartget = new File(pumlName.substring(0, pumlName.length() - ".puml".length()) + ".png");
            if (!source.exists() || source.lastModified() < tartget.lastModified()) {
                continue;
            }
            argsBuilder.append(view.getRestoredPumlFileName() + " ");
        }
        if (argsBuilder.toString().length() > 0) {
            LogUtil.d("", "createViewPngBatch files-->" + argsBuilder.toString())
            CommandHelper.callAsyncVoidCommand(COMMAND_PUML + " " + argsBuilder.toString());
        }
    }
}
