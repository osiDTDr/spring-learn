package com.bright.spring.learn.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bright.spring.learn.mybatisplus.entity.User;
import com.bright.spring.learn.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MybatisPlusTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        assert (5 == userList.size());
        userList.forEach(System.out::println);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", 1, 2);
        Page<User> page = new Page<>(1, 1, false);
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        List<User> records = userPage.getRecords();
    }
}
