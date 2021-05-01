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
* @date: 2021��4��15�� ����12:14:22
*/
@Controller("userController")
public class UserController {

    @Autowired
    private UserMapper userDao;
    public void test() {
        // ��ѯһ���û�
        User auser = userDao.selectUserById(1);
        System.out.println(auser);
        System.out.println("============================");
        // ���һ���û�
        User addmu = new User();
        addmu.setUsername("�º�");
        addmu.setRemark("��");
        int add = userDao.addUser(addmu);
        System.out.println("�����" + add + "����¼");
        System.out.println("============================");
        // �޸�һ���û�
        User updatemu = new User();
        updatemu.setId(1);
        updatemu.setUsername("����");
        updatemu.setRemark("Ů");
        int up = userDao.updateUserById(updatemu);
        System.out.println("�޸���" + up + "����¼");
        System.out.println("============================");
        // ɾ��һ���û�
        int dl = userDao.deleteUserById(0);
        System.out.println("ɾ����" + dl + "����¼");
        System.out.println("============================");
        // ��ѯ�����û�
        List<User> list = userDao.selectAllUser();
        for (User User : list) {
            System.out.println(User);
        }
    }
}
