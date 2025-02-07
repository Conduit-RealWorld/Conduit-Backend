package com.conduit.demo.service;

import com.conduit.demo.dao.UserMapper;
import com.conduit.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserById(Long id) {
        return userMapper.findById(id);
    }

}
