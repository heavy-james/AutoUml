package com.heavy.autouml.gradle

import com.heavy.autouml.util.LogUtil
import org.gradle.api.Plugin
import org.gradle.api.Project

public class PluginImpl implements Plugin<Project> {

    void apply(Project project) {

        LogUtil.init(getLogLevel(project), false, null);

        UmlConfig umlConfig = new UmlConfig();
        umlConfig.restorePath = project.buildDir.name;
        project.extensions.add("umlConfig", umlConfig)

        project.task('createUml') {

            doLast() {
                UmlConfig config = project.extensions.getByName("umlConfig");
                UmlCreater creater = new UmlCreater(config);
                creater.create(false);
            }

        }

    }

    private int getLogLevel(Project project) {

        if (project.getLogger().isDebugEnabled()) {
            return LogUtil.LEVEL_DEBUG;
        }
        if (project.getLogger().isInfoEnabled()) {
            return LogUtil.LEVEL_INFO;
        }
        return LogUtil.LEVEL_ERROR;
    }
}