package com.example.quizeo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class UserTest {

    User user;

    @Before
    public void setUp() {
        user = new User();
    }

    /** Test the setNickName() and getNickName() methods. */
    @Test
    public void setNickName() {
        System.out.println("setNickName()");
        // Create a new nickname and set this nickname:
        String nickName = "test";
        user.setNickName(nickName);

        // check if the nickName was correctly set:
        Assert.assertEquals(nickName, user.getNickName());
    }

    /** Test the setUserId() and getUserId() methods. */
    @Test
    public void setUserId() {
        System.out.println("setUserId()");
        // Create a new userId and set this userId:
        String id = UUID.randomUUID().toString();
        user.setUserId(id);
        // Check if the userId was correctly set:
        Assert.assertEquals(id, user.getUserId());
    }

    /** test if the initialization works with a nickName and Id. */
    @Test
    public void initialization() {
        System.out.println("Initialization()");
        // Create a nickName and userId and pass this while initializing:
        String nickName = "test";
        String id = UUID.randomUUID().toString();
        User user1 = new User(nickName, id);
        // Check if both the userId and nickName were correctly passed:
        Assert.assertEquals(nickName, user1.getNickName());
        Assert.assertEquals(id, user1.getUserId());
    }
}