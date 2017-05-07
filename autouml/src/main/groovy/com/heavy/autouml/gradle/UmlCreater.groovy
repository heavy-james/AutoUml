package com.heavy.autouml.gradle

import com.heavy.autouml.controller.JsonViewCreater
import com.heavy.autouml.controller.UmlViewCreater
import com.heavy.autouml.interf.IClassData
import com.heavy.autouml.model.ClassDiagramConfig
import com.heavy.autouml.model.DefaultClassData
import com.heavy.autouml.model.JsonConfig
import com.heavy.autouml.util.ClassPathUtil
import com.heavy.autouml.util.FileUtil
import com.heavy.autouml.util.LogUtil
import com.heavy.autouml.view.JsonView
import com.heavy.autouml.view.UMLClassDiagramView
import org.gradle.internal.classloader.ClasspathUtil
import org.gradle.internal.classloader.VisitableURLClassLoader

public class UmlCreater {

    private final String TAG = "UmlCreater";
    private String[] classPaths = null;
    private String[] packageNames = null;
    private String[] classNames = null;
    private String resotrePath = null;
    private ClassLoader loader = null;
    private List<JsonView> beans = null;
    private List<Class<?>> classes = null;
    private List<IClassData> datas = null;
    private Collection<JsonConfig> jsonConfigs;
    private Collection<ClassDiagramConfig> classDiagramConfigs;

    public UmlCreater(UmlConfig config) {
        resotrePath = config.restorePath + "/AutoUml/";
        LogUtil.i(TAG, "resotrePath -->" + resotrePath);

        classPaths = config.classpaths.getContents();
        printData(classPaths, "classPaths");

        packageNames = config.packages.getContents();
        printData(packageNames, "packageNames");

        classNames = config.classes.getContents();
        printData(classNames, "classNames");

        jsonConfigs = config.formats.jsonConfigs;
        printJsonConfig();

        classDiagramConfigs = config.formats.diagramConfigs;
        printClassDiagramConfigs();

        setUpCommonData();
    }


    private void printJsonConfig() {
        for (JsonConfig config : jsonConfigs) {
            LogUtil.d(TAG, "printJsonConfig json config-->" + config.orgnization);
        }
    }

    private void printClassDiagramConfigs() {
        for (ClassDiagramConfig config : classDiagramConfigs) {
            LogUtil.d(TAG, "printClassDiagramConfigs class diagram config-->" + config.toString());
        }
    }

    private void printData(String[] datas, String tag) {
        LogUtil.d(TAG, "$tag -->" + datas.size());
        if (datas != null) {
            for (String data : datas) {
                LogUtil.d(TAG, "$tag -->" + data);
            }
        }
    }

    private void setUpClasses(){
        Set<String> resultClassNames = new HashSet<>();
        for(String packageName : packageNames){
            resultClassNames.addAll(ClassPathUtil.getClassName(packageName, loader, true));
        }
        resultClassNames.addAll(classNames);
        classNames = resultClassNames.toArray(new String[0]);
        classes = ClassPathUtil.collectClasses(classNames, loader);
    }

    private void setUpClassLoader(){
        List<URL> urls = FileUtil.fileNamesToUrls(classPaths).toList();
        urls.addAll(FileUtil.filesToUrls(FileUtil.getAllJARS(classPaths)));
        if (urls != null) {
            for (URL url : urls) {
                LogUtil.d("UmlCreater", "setUpClassLoader classpath url-->" + url.toString());
            }
        } else {
            LogUtil.w("UmlCreater", "setUpClassLoader classpath empty");
        }

        loader = new VisitableURLClassLoader(this.getClass().getClassLoader(), urls);

    }

    private void setUpClassDatas(){
        for (Class<?> clazz : classes) {
            LogUtil.d("UmlCreater", "setUpCommonData result class-->" + clazz.getName());
        }
        datas = DefaultClassData.convertToClassDiagramDatas(classes);
    }

    private void setUpCommonData() {
        setUpClassLoader();
        setUpClasses();
        setUpClassDatas();
    }

    private void setUpJsonViewData() {
        beans = JsonView.convertToViews(datas, JsonView.class);
    }

    public void create(boolean cover) {
        for (JsonConfig config : jsonConfigs) {
            createClassDiagramWithJson(config);
        }

        for (ClassDiagramConfig config : classDiagramConfigs) {
            createClassDiagramWithUML(config, cover);
        }
    }

    private void createClassDiagramWithJson(JsonConfig config) {
        if (config.isPackageView()) {
            setUpJsonViewData();
            JsonViewCreater creater = new JsonViewCreater("all", beans);
            creater.createView();
            creater.restore(resotrePath + config.toStorePath());
        } else if (config.isClassView()) {
            for (JsonView view : beans) {
                JsonViewCreater creater = new JsonViewCreater(view.getData().getName(), view);
                creater.createView();
                creater.restore(resotrePath + config.toStorePath());
            }
        }
    }

    private void createClassDiagramWithUML(ClassDiagramConfig config, boolean cover) {
        List<UMLClassDiagramView> views = new ArrayList<>();
        for (IClassData data : datas) {
            UmlViewCreater creater = new UmlViewCreater(data, config);
            UMLClassDiagramView view = creater.createView();
            view.restorePumlFile(resotrePath + config.toStorePath(), cover);
            views.add(view);
        }
        UmlViewCreater.createViewPngBatch(views);
    }
}
