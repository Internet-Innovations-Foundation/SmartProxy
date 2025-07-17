package org.iif.smartproxy.domain

/**
 * Proxy config. This class is responsible for providing proxy configuration.
 * The class can be used to write custom implementations of a configuration.
 * @param T - config type (Since each proxy may have its own config format, we use generics).
 */
abstract class ProxyConfig<T> {
    abstract fun getConfig(): T
}