package com.example.kmp.monitoring

/**
 * Monitoring interface for analytics and error tracking
 */
interface Monitoring {
    /**
     * Trace an event with attributes
     */
    fun trace(name: String, attributes: Map<String, String> = emptyMap())
    
    /**
     * Log an error with attributes
     */
    fun error(message: String, attributes: Map<String, String> = emptyMap())
    
    /**
     * Log a warning with attributes
     */
    fun warning(message: String, attributes: Map<String, String> = emptyMap())
    
    /**
     * Log info with attributes
     */
    fun info(message: String, attributes: Map<String, String> = emptyMap())
}

/**
 * Default implementation that logs to console (for development)
 */
class ConsoleMonitoring : Monitoring {
    override fun trace(name: String, attributes: Map<String, String>) {
        val attrs = if (attributes.isNotEmpty()) {
            attributes.entries.joinToString(", ") { "${it.key}=${it.value}" }
        } else ""
        println("TRACE: $name${if (attrs.isNotEmpty()) " [$attrs]" else ""}")
    }
    
    override fun error(message: String, attributes: Map<String, String>) {
        val attrs = if (attributes.isNotEmpty()) {
            attributes.entries.joinToString(", ") { "${it.key}=${it.value}" }
        } else ""
        println("ERROR: $message${if (attrs.isNotEmpty()) " [$attrs]" else ""}")
    }
    
    override fun warning(message: String, attributes: Map<String, String>) {
        val attrs = if (attributes.isNotEmpty()) {
            attributes.entries.joinToString(", ") { "${it.key}=${it.value}" }
        } else ""
        println("WARNING: $message${if (attrs.isNotEmpty()) " [$attrs]" else ""}")
    }
    
    override fun info(message: String, attributes: Map<String, String>) {
        val attrs = if (attributes.isNotEmpty()) {
            attributes.entries.joinToString(", ") { "${it.key}=${it.value}" }
        } else ""
        println("INFO: $message${if (attrs.isNotEmpty()) " [$attrs]" else ""}")
    }
}
