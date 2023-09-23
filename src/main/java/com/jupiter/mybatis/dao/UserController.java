package com.jupiter.mybatis.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jupiter.mybatis.mapper.UserMapper;
import com.jupiter.mybatis.po.User;

/**
* @Description:
* @author: Jupiter.Lin
* @version: V1.0 
* @date: 2021年4月15日 下午12:14:22
*/
@Controller("userController")
public class UserController {

    @Autowired
    private UserMapper userDao;
    public void test() {
        // 查询一个用户
        User auser = userDao.selectUserById(1);
        System.out.println(auser);
        System.out.println("============================");
        // 添加一个用户
        User addmu = new User();
        addmu.setUsername("陈恒");
        addmu.setRemark("男");
        int add = userDao.addUser(addmu);
        System.out.println("添加了" + add + "条记录");
        System.out.println("============================");
        // 修改一个用户
        User updatemu = new User();
        updatemu.setId(1);
        updatemu.setUsername("张三");
        updatemu.setRemark("女");
        int up = userDao.updateUserById(updatemu);
        System.out.println("修改了" + up + "条记录");
        System.out.println("============================");
        // 删除一个用户
        int dl = userDao.deleteUserById(0);
        System.out.println("删除了" + dl + "条记录");
        System.out.println("============================");
        // 查询所有用户
        List<User> list = userDao.selectAllUser();
        for (User User : list) {
            System.out.println(User);
        }
    }
}
