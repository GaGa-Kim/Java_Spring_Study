package com.example.examplespring.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/example/parameter")
public class ExampleParameterController {

    Logger logger = LoggerFactory.getLogger(getClass());

    /* RequestParam String으로 파라미터 받기
       http://localhost:8080/example/parameter/example1?id=test123&code=B123
     */
    @GetMapping("/example1")
    public void example1(@RequestParam String id, @RequestParam String code, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("code", code);
    }

    /* RequestParam Map으로 파라미터 받기
       http://localhost:8080/example/parameter/example2?id=test123&code=B123
     */
    @GetMapping("/example2")
    public void example2(@RequestParam Map<String, Object> paramMap, Model model) {
        model.addAttribute("paramMap", paramMap);
    }

    /* Class로 파라미터 받기
       http://localhost:8080/example/parameter/example3?id=test123&code=B123
    */
    @GetMapping("/example3")
    public void example3(ExampleParameter parameter, Model model) {
        model.addAttribute("parameter", parameter);
    }

    /* PathVariable로 파라미터 받기
       http://localhost:8080/example/parameter/example4/test123/B123
    */
    @GetMapping("/example4/{id}/{code}")
    public String example4(@PathVariable String id, @PathVariable String code, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("code", code);
        return "/example/parameter/example4";
    }

    /* String[]로 파라미터 받기1
       http://localhost:8080/example/parameter/example5?ids=12&ids=34&ids=56
    */
    @GetMapping("/example5")
    public String example5(@RequestParam String[] ids, Model model) {
        model.addAttribute("ids", ids);
        return "/example/parameter/example5";
    }

    /* String[]로 파라미터 받기2
       http://localhost:8080/example/parameter/example5?ids=12&ids=34&ids=56
    @GetMapping("/example5")
    public String example5(HttpServletRequest request, Model model) {
        model.addAttribute("ids", request.getParameterValues("ids"));
        return "/example/parameter/example5";
    }
     */

    /* JSON을 RequestBody로 받기
       http://localhost:8080/example/parameter/example6/form
    */
    @GetMapping("/example6/form")
    public void form() {

    }

    @PostMapping("/example6/saveData")
    @ResponseBody
    public Map<String, Object> example6(@RequestBody Map<String, Object> requestBody) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", true);
        logger.info("requestBody : {}", requestBody);
        return resultMap;
    }

    /* JSON을 Class로 받기
       http://localhost:8080/example/parameter/example6/form
    @PostMapping("/example7/saveData")
    @ResponseBody
    public Map<String, Object> example6(@RequestBody ExampleRequestBodyUser requestBody) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result", true);
        logger.info("requestBody : {}", requestBody);
        return resultMap;
    }
     */
}
