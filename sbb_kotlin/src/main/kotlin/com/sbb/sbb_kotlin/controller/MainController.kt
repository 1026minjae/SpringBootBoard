package com.sbb.sbb_kotlin.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class MainController {
    @GetMapping("/sbb")
    @ResponseBody
    fun index(): String {
        return "Welcome to SBB"
    }    
}
