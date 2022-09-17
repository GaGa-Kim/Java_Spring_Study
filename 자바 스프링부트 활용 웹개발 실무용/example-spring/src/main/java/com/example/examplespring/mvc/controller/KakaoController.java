package com.example.examplespring.mvc.controller;

import com.example.examplespring.configuration.properties.KakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kakao/*")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoProperties kakaoProperties;

    @GetMapping("/test")
    @ResponseBody
    public KakaoProperties test() {
        return kakaoProperties;
    }
}
