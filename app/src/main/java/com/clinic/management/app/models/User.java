package com.clinic.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private String imageProfile;
    private boolean isDeleted;

    private int accountType;

    private boolean active;

    private double wallet;

    public User() {
    }

    public User(String id) {
        this(id, "", "", "", "", "", "", false, 0);
    }

    public User(String username, String password) {
        this("", username, password, "", "", "", "", false, 0);
    }

    public User(String username, String password, String fullName, String phone) {
        this("", username, password, fullName, phone, "", "", false, 0);
    }

    public User(String id, String username, String password, String fullName, String phone, String address, String imageProfile, boolean isDeleted, int accountType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.imageProfile = imageProfile;
        this.isDeleted = isDeleted;
        this.accountType = accountType;
        this.active = false;
        this.wallet = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public double getWallet() {
        return wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.fullName);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeString(this.imageProfile);
        dest.writeByte(this.isDeleted ? (byte) 1 : (byte) 0);
        dest.writeInt(this.accountType);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.wallet);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.username = source.readString();
        this.password = source.readString();
        this.fullName = source.readString();
        this.phone = source.readString();
        this.address = source.readString();
        this.imageProfile = source.readString();
        this.isDeleted = source.readByte() != 0;
        this.accountType = source.readInt();
        this.active = source.readByte() != 0;
        this.wallet = source.readDouble();
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.fullName = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.imageProfile = in.readString();
        this.isDeleted = in.readByte() != 0;
        this.accountType = in.readInt();
        this.active = in.readByte() != 0;
        this.wallet = in.readDouble();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
