package com.sbb.sbb_kotlin

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class MainController {
    @GetMapping("/")
    fun root(): String {
        return "redirect:/question/list"
    }    

    @GetMapping("/sbb")
    @ResponseBody
    fun index(): String {
        return "Welcome to SBB"
    }    
}
