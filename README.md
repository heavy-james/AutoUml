# AutoUml
this is a gradle plugin for uml generation, also support json description of classes' relationship.

## Set Up Enviroment

### step1. install graphviz

```
//on osx
brew install graphviz
``` 
* to install on other system, you can find package on
[graphviz official website](http://www.graphviz.org/Download..php)

### step2. install plantuml

```
//on osx
brew install plantuml
```

* to install on other system, you can find package on
[plantuml official website](http://plantuml.com/download)

## How to Use

### step1. add dependency

```
buildscript {


    dependencies {
        //add this line to use plugin
        classpath 'com.boyan.heavy:autouml:1.0.0'

    }
}

```

### step2. apply plugin

```
//add this line to your module's 'build.gradle' to apply plugin
apply plugin: 'org.gradle.autouml'

```

### step3. configure classes and formats

```
umlConfig {

    //string format of absolute path to restore the result, default is the project buildDir.
    restorePath '/Users/heavy/data'

    //paths contains the class files or jar files.
    classpaths {
        add project.rootProject.projectDir.name + "autouml/build/libs"
    }

    //all classes's diagram in the specified packages will be create in the result.
    packages {
        add "com.heavy.autouml.model"
    }

    //specify classes to create the diagram in the result. together with classes specified by packages
    classes {
        add "com.heavy.autouml.controller.organization.DiagramDataOrganiser"
    }

    //the same type diagram with different config will be create once.
    formats {

        json {
            setOrgnization "package"
        }

        json {
            setOrgnization "class"
        }


        classDiagram {
            //if show fields in class entity or not
            setWithFieldInfo true
            //if show methods in class entity ort not
            setWithMethodInfo true
            //if print package info with class name or not
            setWithPackageInfo true
            supportedRelation {
                //show super class or not,described with : extends
                add "extension"
                //show implemented interfaces or not,described with : implements
                add "implement"
                //show fields' types or not, described with : contains
                add "composition"
                //show method return add parameters' types or not, described with : uses
                add "aggregation"
                //show declared member classes , described with : declares
                add "declaration"
            }
        }

        classDiagram {
            setWithFieldInfo false
            setWithMethodInfo false
            setWithPackageInfo false
            supportedRelation {
                add "extension"
                add "implement"
                //add "composition"
                //add "aggregation"
                //add "declaration"
            }
        }

    }
}

```

### step4. build result

```
//in the terminal
./gradlew clean build createUml

```
