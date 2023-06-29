package com.example.examplespring.mvc.controller;

import com.example.examplespring.configuration.properties.FacebookProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facebook/*")
@RequiredArgsConstructor
public class FacebookController {

    private final FacebookProperties facebookProperties;

    @GetMapping("/test")
    @ResponseBody
    public FacebookProperties test() {
        return facebookProperties;
    }
}
