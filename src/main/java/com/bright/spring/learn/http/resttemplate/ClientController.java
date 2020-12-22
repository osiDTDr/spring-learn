package com.bright.spring.learn.http.resttemplate;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ClientController {

    @GetMapping("/get")
    public String get() {
        String url = "http://localhost:8080/post";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Accept", "application/json");
        HttpEntity entity = new HttpEntity(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("test", "test");
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class, map);
        return response.getStatusCode().toString();
    }
}
