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
    public User selectUserByName(String name);
    public User checkUser(User user);
    public User selectUserById(int id);
    public ArrayList<User> selectAllUser();
    public int addUser(User user);
    public int updateUserById(User user);
    public int deleteUserById(int id);
}
