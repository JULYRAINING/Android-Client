package com.md.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SECONDHEAVEN on 2015/9/15.
 */
public class UserBO implements Parcelable {
    private int userId;
    private String name;
    private int gender;
    private String userImage;
    private int grade;
    private String major;
    private String signature;
    private String minor;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.name);
        dest.writeInt(this.gender);
        dest.writeString(this.userImage);
        dest.writeInt(this.grade);
        dest.writeString(this.major);
        dest.writeString(this.signature);
        dest.writeString(this.minor);
    }

    public UserBO() {
    }

    protected UserBO(Parcel in) {
        this.userId = in.readInt();
        this.name = in.readString();
        this.gender = in.readInt();
        this.userImage = in.readString();
        this.grade = in.readInt();
        this.major = in.readString();
        this.signature = in.readString();
        this.minor = in.readString();
    }

    public static final Creator<UserBO> CREATOR = new Creator<UserBO>() {
        public UserBO createFromParcel(Parcel source) {
            return new UserBO(source);
        }

        public UserBO[] newArray(int size) {
            return new UserBO[size];
        }
    };

    @Override
    public String toString() {
        return "UserBO{" +
                "gender=" + gender +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", userImage='" + userImage + '\'' +
                ", grade=" + grade +
                ", major='" + major + '\'' +
                ", signature='" + signature + '\'' +
                ", minor='" + minor + '\'' +
                '}';
    }
}
