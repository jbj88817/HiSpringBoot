package us.bojie.springdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import us.bojie.springdemo.entity.UserEntity;
import us.bojie.springdemo.mapper.UserMapper;
import us.bojie.springdemo.util.DateUtil;

@Repository
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void addUser(String userName, String password, String imoocId, String orderId) {
        userMapper.addUser(userName, password, imoocId, orderId, DateUtil.currentDate());
    }

    public List<UserEntity> findUser(String userName) {
        return userMapper.findUser(userName);
    }

    public List<UserEntity> getUserList() {
        return userMapper.getUserList();
    }

    public void updateUser(String id, String forbid) {
        userMapper.updateUser(id, forbid);
    }
}
