package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    User user;

    @Before
    public void setUp() {
        user = new User();
    }

    @Test
    public void setNickName() {
        System.out.println("setNickName()");
        String nickName = "test";
        user.setNickName(nickName);
        Assert.assertEquals(nickName, user.getNickName());
    }

    @Test
    public void setUserId() {
        System.out.println("setUserId()");
        int id = 12345;
        user.setUserId(id);
        Assert.assertEquals(id, user.getUserId());
    }

    @Test
    public void initialization() {
        System.out.println("Initialization()");
        String nickName = "test";
        int id = 12345;
        User user1 = new User(nickName, id);
        Assert.assertEquals(nickName, user1.getNickName());
        Assert.assertEquals(id, user1.getUserId());
    }
}