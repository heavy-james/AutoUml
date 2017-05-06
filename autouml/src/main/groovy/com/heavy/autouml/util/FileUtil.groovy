package com.heavy.autouml.util

import org.gradle.internal.classpath.DefaultClassPath


public class FileUtil {

    public static URL[] toUrls(String str) {
        List<File> files = getAllFiles(new File(str));
        return toUrls(files);
    }

    public static URL[] toUrls(String[] fileNames) {
        List<File> files = new ArrayList<File>();
        for (String name : fileNames) {
            files.add(new File(name));
        }
        return toUrls(files);
    }

    public static List<URL> toUrls(List<File> files) {
        List<URL> urls = new DefaultClassPath(files).getAsURLs();
        return urls;
    }

    public static List<File> getAllFiles(File file) {
        List<File> files = new LinkedList<File>();
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File tempfile : file.listFiles()) {
                    files.addAll(getAllFiles(tempfile));
                }
            } else {
                files.add(file);
            }
        }
        return files;
    }

    public static List<File> getAllJARS(File file) {
        List<File> files = getAllFiles(file);
        for (File temp : files) {
            if (!temp.isFile() || !temp.exists() || !temp.getName().endsWith(".jar")) {
                files.remove(temp);
            }
        }
        return files;
    }

    public static List<File> getAllJARS(String[] paths) {
        List<File> result = new LinkedList<File>();
        for (String path : paths) {
            result.addAll(getAllJARS(new File(path)));
        }
        return result;
    }

    public static String path2Package(String path) {
        String result = path.replaceAll(File.separator, ".");
        return result;
    }

    public static String package2Path(String path) {
        String result = path.replaceAll(".", File.separator);
        return result;
    }

    public static void writeFile(String fileName, String content, boolean append) {
        if (content != null) {
            writeFile(fileName, content.toCharArray(), append);
        }
    }

    public static File createFile(String fileName) {
        File result = new File(fileName);
        File path = new File(result.getParent());
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!result.exists()) {
            try {
                result.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void writeFile(String fileName, char[] datas, boolean append) {
        writeFile(createFile(fileName), datas, append);
    }

    public static void writeFile(File file, char[] datas, boolean append) {
        if (!file.exists() || !file.isFile()) {
            return;
        }
        FileWriter filerWriter = null;
        BufferedWriter bufWriter = null;
        try {
            filerWriter = new FileWriter(file, append);
            bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(datas);
            bufWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufWriter != null) {
                try {
                    bufWriter.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (filerWriter != null) {
                try {
                    filerWriter.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

}
