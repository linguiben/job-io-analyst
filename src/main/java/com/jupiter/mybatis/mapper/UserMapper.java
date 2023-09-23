package com.jupiter.mybatis.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.jupiter.mybatis.po.User;

/**
 * @Description:
 * @author: Jupiter.Lin
 * @version: V1.0
 * @date: 2021年4月14日 下午4:32:21
 */

//@Repository("userMapper")
@Mapper /*3.4.0支持*/
public interface UserMapper {
    User selectUserByName(String name);

    User checkUser(User user);

    User selectUserById(int id);

    ArrayList<User> selectAllUser();

    int addUser(User user);

    int updateUserById(User user);

    int deleteUserById(int id);
}
