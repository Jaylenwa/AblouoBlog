package com.jack_wang.service;


import com.jack_wang.po.User;

public interface UserService {
    public User checkUser(String username,String password);
}
