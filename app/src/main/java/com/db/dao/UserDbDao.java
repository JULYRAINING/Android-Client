package com.db.dao;

import com.md.entity.UserBO;

import java.util.List;

/**
 * Created by SECONDHEAVEN on 2016/1/21.
 */
public interface UserDbDao {

    public void addUser(UserBO userBO);

    public void deleteUserById(int id);

    public List<UserBO> getUser();

    UserBO getUserById(int id);
}
