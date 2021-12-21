package com.cqut.addressBook.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.cqut.addressBook.util.PinYinUtil;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2021/12/5
 */
public class User implements Comparable<User>{
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "label")
    private Character label;
    @JSONField(name = "pinYin")
    private String pinYin;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "phone")
    private ArrayList<String> phone;
    @JSONField(name = "address")
    private String address;

    public Character getLabel() {
        return label;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
        char ch = pinYin.charAt(0);
        if (!('A' <= ch && ch <= 'Z')) {
            this.label = '#';
        } else {
            this.label = ch;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPhone() {
        return phone;
    }

    public void setPhone(ArrayList<String> phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
//        Objects.equals(name, user.name) && Objects.equals(phone, user.phone) && Objects.equals(address, user.address)
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", label=" + label +
                ", pinYin='" + pinYin + '\'' +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public int compareTo(User o) {
        return PinYinUtil.comparePinYin(this.getPinYin(), o.getPinYin());
    }
}
