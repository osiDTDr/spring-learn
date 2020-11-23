package com.bright.spring.learn.mybatisplus.service.impl;

import com.bright.spring.learn.mybatisplus.entity.User;
import com.bright.spring.learn.mybatisplus.mapper.UserMapper;
import com.bright.spring.learn.mybatisplus.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhengyuan
 * @since 2020-11-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
