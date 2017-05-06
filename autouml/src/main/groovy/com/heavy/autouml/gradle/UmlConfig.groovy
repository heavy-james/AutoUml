package com.heavy.autouml.gradle

import com.heavy.autouml.model.AddableStringConfig
import com.heavy.autouml.model.ClassDiagramConfig
import com.heavy.autouml.model.JsonConfig
import com.heavy.autouml.util.LogUtil


public class UmlConfig {

    String restorePath;

    AddableStringConfig classpaths;

    AddableStringConfig packages;

    AddableStringConfig classes;

    Formats formats;

    public UmlConfig() {
        this.packages = new AddableStringConfig();
        this.classpaths = new AddableStringConfig();
        this.classes = new AddableStringConfig();
        this.formats = new Formats();
    }

    public void restorePath(String path) {
        restorePath = path;
    }

    def classpaths(Closure closure) {
        classpaths.configure(closure);
    }

    def packages(Closure closure) {
        packages.configure(closure);
    }

    def classes(Closure closure) {
        classes.configure(closure);
    }

    def formats(Closure closure) {
        formats.configure(closure);
    }

    public static class Formats {

        Set<JsonConfig> jsonConfigs;

        Set<ClassDiagramConfig> diagramConfigs;

        public void configure(Closure closure1) {
            closure1.resolveStrategy = Closure.DELEGATE_FIRST;
            closure1.delegate = this;
            closure1.run();
        }

        def json(Closure closure1) {
            LogUtil.d("Formats", "json configuring...");
            if (jsonConfigs == null) {
                jsonConfigs = new HashSet<>();
            }
            JsonConfig config = new JsonConfig();
            config.configure(closure1);
            if (!jsonConfigs.contains(config)) {
                jsonConfigs.add(config);
            } else {
                LogUtil.w("Formats", "json closure figure out same json config, ignored--> ${config.toString()}");
            }
        }

        def classDiagram(Closure closure1) {
            LogUtil.d("Formats", "classDiagrams closure configuring...");
            if (diagramConfigs == null) {
                diagramConfigs = new HashSet<>();
            }
            ClassDiagramConfig config = new ClassDiagramConfig();
            closure1.resolveStrategy = Closure.DELEGATE_FIRST;
            closure1.delegate = config;
            config.configure(closure1);
            if (!diagramConfigs.contains(config)) {
                diagramConfigs.add(config);
            } else {
                LogUtil.w("Formats", "classDiagram closure figure out same diagram , ingnored config --> ${config.toString()}");
            }
        }
    }

}