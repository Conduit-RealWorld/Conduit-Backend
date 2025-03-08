package com.conduit;

import com.conduit.domain.user.UserEntity;
import com.conduit.infrastructure.persistence.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testFindByEmail() {
        UserEntity user = userMapper.findByEmail("charlie@example.com");
        System.out.println(user);
    }
}
