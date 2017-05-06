package com.heavy.autouml.view

import com.heavy.autouml.util.FileUtil
import com.heavy.autouml.util.LogUtil
import com.heavy.autouml.util.TextUtil

public class UMLClassDiagramView {

    public static final String TAG = "UMLClassDiagramView";
    private static final String UML_FILE_SUFFIX = ".puml";
    private String mHeader = "@startuml";
    private String mFooter = "@enduml";

    StringBuilder mBuilder;
    private String mName;
    private String mRestorePath;
    private String mRestoredPumlFileName;
    private long mSourceModifiedTime;

    public UMLClassDiagramView(String name, long sourceFileTime) {
        mName = name;
        mSourceModifiedTime = sourceFileTime;
        mBuilder = new StringBuilder();
    }

    public String getName() {
        return mName;
    }

    public String getFileName() {
        return mName + UML_FILE_SUFFIX;
    }

    public void writeHeader() {
        write(TextUtil.endLine(mHeader));
    }

    public void writeFooter() {
        write(mFooter);
    }

    public void write(String content) {
        mBuilder.append(content);
    }

    public void clear() {
        mBuilder = new StringBuilder();
    }

    public String getRestoredPumlFileName() {
        return mRestoredPumlFileName;
    }

    public void restorePumlFile(String pathName, boolean cover) {
        if (!pathName.endsWith("/")) {
            pathName += "/";
        }
        mRestoredPumlFileName = pathName + getFileName();
        File target = new File(mRestoredPumlFileName);
        if (cover || target.lastModified() < mSourceModifiedTime) {
            LogUtil.d("UMLClassDiagramView", "restorePumlFile name-->" + mRestoredPumlFileName
                    + ";time-->" + target.lastModified() + "; source time-->" + mSourceModifiedTime);
            FileUtil.writeFile(mRestoredPumlFileName, mBuilder.toString(), false);
        }
    }

}
