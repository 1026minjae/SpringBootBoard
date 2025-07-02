package com.sbb.sbb_kotlin

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
class DataNotFoundException(message: String): RuntimeException(message)