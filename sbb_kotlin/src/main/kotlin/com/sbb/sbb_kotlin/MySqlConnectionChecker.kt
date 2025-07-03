package com.sbb.sbb_kotlin

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class MySqlConnectionChecker(private val jdbcTemplate: JdbcTemplate) {

    @EventListener(ApplicationReadyEvent::class)
    fun testConnection() {
        val version: String? = jdbcTemplate.queryForObject("SELECT VERSION()", String::class.java)
        println("MySQL version: $version")
    }
}