package com.heavy.autouml.util

public class CommandHelper {

    public static void callAsyncVoidCommand(String command) {
        callAsyncVoidCommand(command, null);
    }

    public static void callAsyncVoidCommand(String command, String[] evns) {
        try {
            Runtime.getRuntime().exec(command, evns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
