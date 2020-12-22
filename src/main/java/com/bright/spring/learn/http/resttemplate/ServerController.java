package com.bright.spring.learn.http.resttemplate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ServerController {

    @PostMapping("/post")
    @ResponseBody
    public String post(@RequestBody Map<String, String> body, HttpServletRequest request) {
        return body.toString();
    }
}
