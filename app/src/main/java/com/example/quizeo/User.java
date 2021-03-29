package com.example.quizeo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class User implements Parcelable {

    /** nickName associated with this user: */
    private String nickName;

    /** userId associated with this user: */
    private String userId;

    /** I
     * Initialization which directly passes a nickname and userId.
     *
     * @param nickName nickName associated with this user.
     * @param userId userId associated with this user.
     */
    public User(String nickName, String userId) {
        this.nickName = nickName;
        this.userId = userId;
    }

    /**
     * Initialize this user with empty variables.
     */
    public User() {
    }

    protected User(Parcel in) {
        nickName = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nickName);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
    public void setUserId(String userId) {
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
    public String getUserId() {
        return userId;
    }
}
