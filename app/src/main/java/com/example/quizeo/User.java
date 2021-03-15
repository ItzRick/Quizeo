package com.example.quizeo;

import java.util.UUID;

public class User {

    /** nickName associated with this user: */
    private String nickName;

    /** userId associated with this user: */
    private UUID userId;

    /** I
     * Initialization which directly passes a nickname and userId.
     *
     * @param nickName nickName associated with this user.
     * @param userId userId associated with this user.
     */
    public User(String nickName, UUID userId) {
        this.nickName = nickName;
        this.userId = userId;
    }

    /**
     * Initialize this user with empty variables.
     */
    public User() {
    }

    /**
     * Set the nickname of this user to an(other) value.
     *
     * @param nickName the nickname, to which the nickName variable should be set.
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Set the userId of this user to an(other) value.
     *
     * @param userId Id to which the userId of this user should be set.
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Retrieve the nickName of this user.
     *
     * @return the nickname.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Retrieve the userId of this user.
     *
     * @return userId of the current user.
     */
    public UUID getUserId() {
        return userId;
    }
}
