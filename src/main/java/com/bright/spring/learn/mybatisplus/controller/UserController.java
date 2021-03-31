package com.bright.spring.learn.mybatisplus.controller;


import com.bright.spring.learn.mybatisplus.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhengyuan
 * @since 2020-11-23
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public String addUser(@RequestBody User user) {
        logger.debug("param user is {}", user);
        return "success";
    }

    @GetMapping
    public String getUser() {
        return "success";
    }
}

