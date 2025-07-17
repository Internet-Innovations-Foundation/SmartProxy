package org.iif.smartproxy.utils

class ByeDpiHelper {
    companion object {
        init {
            System.loadLibrary("byedpi")
        }
    }

    fun start(config: Array<String>): Int {
        return jniStartProxy(config)
    }

    fun stop(): Int {
        return jniStopProxy()
    }

    private external fun jniStartProxy(config: Array<String>): Int
    private external fun jniStopProxy(): Int
}