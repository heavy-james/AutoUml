package com.heavy.autouml.util

import java.text.SimpleDateFormat
import java.util.concurrent.ConcurrentLinkedQueue;

public class LogUtil {
    private static final String TAG = "AutoUmlLog";
    private static boolean mDebugMode = false;
    private static boolean mSaveLog = true;
    private static int mLogLevel = 1;
    public static
    final String DEFAULT_LOG_PATH = System.getProperty("user.dir") + File.separator + "runtime.log";
    public static String mSavePath = DEFAULT_LOG_PATH;
    public static boolean mAppTerminated;
    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARN = 3;
    public static final int LEVEL_ERROR = 4;

    public static void init(int logLevel, boolean saveLog, String savePath) {
        mLogLevel = logLevel;
        mSavePath = savePath == null ? DEFAULT_LOG_PATH : savePath;
        mSaveLog = saveLog;
        Writer.deleteLogFile();
        LogUtil.d(TAG, "LogUtil init level-->" + logLevel + "; savepath-->" + savePath);
    }

    public static void setMode(boolean debug, boolean saveLog) {
        mDebugMode = debug;
        mSaveLog = saveLog;
        LogUtil.d(TAG, "LogUtil setMode debug-->" + debug + "; saveLog-->" + saveLog);
    }

    public static void v(String tag, String content) {
        if (canPrint(LEVEL_VERBOSE)) {
            Log.print(TAG, "[" + tag + "]:" + content);
            if (mSaveLog) {
                Writer.recordActionInfoLog(new ActionInfo("VERBOSE", TAG, "[" + tag + "]:" + content));
            }
        }
    }

    public static void d(String tag, String content) {
        if (canPrint(LEVEL_DEBUG)) {
            Log.print(TAG, "[" + tag + "]:" + content);
            if (mSaveLog) {
                Writer.recordActionInfoLog(new ActionInfo("DEBUG", TAG, "[" + tag + "]:" + content));
            }
        }
    }

    public static void i(String tag, String content) {
        if (canPrint(LEVEL_INFO)) {
            Log.print(TAG, "[" + tag + "]:" + content);
            if (mSaveLog && canPrint(LEVEL_INFO)) {
                Writer.recordActionInfoLog(new ActionInfo("INFO", TAG, "[" + tag + "]:" + content));
            }
        }
    }

    public static void w(String tag, String content) {
        if (canPrint(LEVEL_WARN)) {
            Log.print(TAG, "[" + tag + "]:" + content);
            if (mSaveLog) {
                Writer.recordActionInfoLog(new ActionInfo("WARN", TAG, "[" + tag + "]:" + content));
            }
        }
    }

    public static void e(String tag, String content) {
        if (canPrint(LEVEL_ERROR)) {
            Log.print(TAG, "[" + tag + "]:" + content);
            if (mSaveLog) {
                Writer.recordActionInfoLog(new ActionInfo("ERROR", TAG, "[" + tag + "]:" + content));
            }
        }
    }

    private static boolean canPrint(int logLevel) {
        return mDebugMode || logLevel >= mLogLevel;
    }

    public static void releaseSource() {
        mAppTerminated = true;
    }

    static class ActionInfo {
        private static SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        public String mLevel = "";
        public String mTag = "";
        public String mContent = "";

        public ActionInfo(String level, String tag, String content) {
            mLevel = level;
            mTag = tag;
            mContent = content;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append(mDateFormat.format(new Date()) + " ");
            sb.append(Thread.currentThread().getId() + " ");
            sb.append(mLevel + "/" + mTag + ": " + mContent);
            return sb.toString();
        }
    }

    static class Writer {

        public
        static ConcurrentLinkedQueue<ActionInfo> tempQueue = new ConcurrentLinkedQueue<ActionInfo>();

        /**
         * 记录行为信息
         *
         * @param ai
         */
        public static synchronized void recordActionInfoLog(ActionInfo ai) {
            if (!LogUtil.mAppTerminated) {
                tempQueue.add(ai);
            }
            if (!WriteThread.isWriteThreadLive) {
                new WriteThread().start();
            }
        }

        /**
         * 打开日志文件并写入日志
         *
         * @return
         * */
        public static void recordStringLog(String text) {
            FileUtil.writeFile(LogUtil.mSavePath, text, true);
        }

        /**
         * 判断日志文件是否存在
         *
         * @return
         */
        public static boolean isExitLogFile() {
            File file = new File(LogUtil.mSavePath);
            return file.exists() && file.length() > 3;
        }

        /**
         * 删除日志文件
         */
        public static void deleteLogFile() {
            File file = new File(LogUtil.mSavePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    static class WriteThread extends Thread {
        // 写日志线程是否已经在运行了
        public static boolean isWriteThreadLive;

        public WriteThread() {
            isWriteThreadLive = true;
        }

        @Override
        public void run() {
            isWriteThreadLive = true;
            while (!Writer.tempQueue.isEmpty()) {// 对列不空时
                try {
                    // 写日志到SD卡
                    Writer.recordStringLog(Writer.tempQueue.poll().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 队列中的日志都写完了，关闭线程（也可以常开 要测试下）
            isWriteThreadLive = false;
        }
    }

    public static boolean saveLogMode() {
        return LogUtil.mSaveLog;
    }

    public static String getLogFileName() {
        return LogUtil.mSavePath;
    }

    public static boolean isLogSaveCompleted() {
        return !WriteThread.isWriteThreadLive;
    }

    private static class Log {
        static void print(String tag, String msg) {
            System.out.println("[ " + TAG + "] : " + msg);
        }
    }
}
