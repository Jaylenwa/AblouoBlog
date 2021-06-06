package com.jack_wang.service.impl;

import com.jack_wang.dao.UserRepository;
import com.jack_wang.po.User;
import com.jack_wang.service.UserService;
import com.jack_wang.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override

    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username,MD5Util.code(password));
        return user;
    }
}
