package org.iif.smartproxy.utils

object Utils {
    const val OUTLINE_DEFAULT_CONFIG = "{ \"dns\": [ { \"https\": { \"name\": \"1.1.1.1\" } }, { \"tls\": { \"name\": \"8.8.8.8\" } }, { \"udp\": { \"address\": \"[2620:fe::fe]:9953\" } } ], \"tls\": [ \"split:2\", \"tlsfrag:1\" ] }"
}