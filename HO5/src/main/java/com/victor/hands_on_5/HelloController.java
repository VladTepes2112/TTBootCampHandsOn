package com.victor.hands_on_5;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "SPRING BOOT HANDS-ON-1 (5)";
    }

}
