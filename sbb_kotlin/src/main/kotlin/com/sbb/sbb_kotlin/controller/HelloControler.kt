package com.sbb.sbb_kotlin.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class HelloController {
    @GetMapping("/hello")
    @ResponseBody
    fun hello(): String {
        return "Hello World!"
    }    
}
