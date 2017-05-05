package com.heavy.autouml.gradle
import com.heavy.autouml.util.LogUtil
import org.gradle.api.Plugin
import org.gradle.api.Project

public class PluginImpl implements Plugin<Project> {

    void apply(Project project) {

        String logFileName = "$project.rootProject.buildDir.absolutePath/runtime.log";
        LogUtil.init(LogUtil.LEVEL_DEBUG, true, logFileName);

        UmlConfig umlConfig = new UmlConfig();

        project.extensions.add("umlConfig", umlConfig)

        project.task('createUml') {

            doLast(){
                UmlConfig config = project.extensions.getByName("umlConfig");
                LogUtil.d("PluginImpl", "task  createUml run");
                UmlCreater creater = new UmlCreater(config);
                creater.create(false);
            }

        }

    }
}