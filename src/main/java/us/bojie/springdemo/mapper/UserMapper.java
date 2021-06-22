package us.bojie.springdemo.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

import us.bojie.springdemo.entity.UserEntity;

@Repository
public interface UserMapper {

    void addUser(String userName, String password, String imoocId, String orderId, String createTime);

    List<UserEntity> findUser(String userName);

    List<UserEntity> getUserList();

    void updateUser(String id, String forbid);
}
